package basetest.foreign;

import java.io.RandomAccessFile;
import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.GroupLayout;
import java.lang.foreign.Linker;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SequenceLayout;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import static java.nio.channels.FileChannel.MapMode.READ_WRITE;

/*
* The foreign function and memory access API provides a supported, safe, and efficient API to access both heap and native memory and invoke native code. It provides several key components:
* Arena: controls the lifecycle of native memory segments
* MemorySegment: represents a contiguous region of memory, either on-heap or off-heap
* MemoryLayout: describes the structure of memory segments
* FunctionDescriptor: models the signature of foreign functions
* Linker: facilitates linking Java code with native functions
* SymbolLookup: looks up native symbols (functions, variables) by name
*/
public class ForeignTest {
    public static void main(String...args) {

    }

    static void arenaAndMemorySegment() throws Exception {
        // Arena - Manage lifecycle of native memory segments
        Arena globalArena = Arena.global(); // Unbounded lifetime, cannot be closed manually
        Arena autoArena = Arena.ofAuto(); // Bounded lifetime, managed by garbage collector, cleared when arena and its segments become unreachable
        Arena confinedArena = Arena.ofConfined(); // Bounded lifetime and restricts access to the creating thread
        Arena sharedArena = Arena.ofShared(); // Bounded lifetime and allows access from multiple threads

        // MemorySegment - Represents a contiguous region of memory, it has two boundaries
        // 1. Spatial Boundary: has lower and upper bounds (size)
        // 2. Temporal Boundary: governs creating, using, and closing a memory segment
        MemorySegment segment = globalArena.allocate(100); // Contiguous memory segment of 100 bytes
        MemorySegment heapAllocatedMemorySegment = MemorySegment.ofArray(new long[100]); // Heap-allocated (existing) Java array as memory segment
        MemorySegment memorySegment = MemorySegment.ofBuffer(ByteBuffer.allocateDirect(200)); // ByteBuffer (existing) as memory segment

        //Mapped Memory Segment - Memory-mapped file as memory segment
        RandomAccessFile file = new RandomAccessFile("/tmp/memory.txt", "rw");
        FileChannel fc = file.getChannel();
        MemorySegment fileMemorySegment = fc.map(READ_WRITE, 0, 200, confinedArena);

        // Slice a memory segment into multiple smaller blocks (to avoid multiple memory blocks if we want to store values with different layout)
        MemorySegment singleMemorySegment = Arena.ofAuto().allocate(12);
        MemorySegment segment1 = singleMemorySegment.asSlice(0, 4); // First 4 bytes
        MemorySegment segment2 = singleMemorySegment.asSlice(4, 4); // Next 4 bytes
        MemorySegment segment3 = singleMemorySegment.asSlice(8, 4); // Last 4 bytes
        VarHandle intHandle = ValueLayout.JAVA_INT.varHandle(); // VarHandle to access int values
        intHandle.set(segment1, 0, Integer.MIN_VALUE);
        intHandle.set(segment2, 0, 0);
        intHandle.set(segment3, 0, Integer.MAX_VALUE);
        assert intHandle.get(segment1, 0).equals(Integer.MIN_VALUE);
        assert intHandle.get(segment2, 0).equals(0);
        assert intHandle.get(segment3, 0).equals(Integer.MAX_VALUE);
    }

    static void memoryLayouts() {
        // MemoryLayout - Describe the contents of a memory segment
        int numberOfPoints = 10;
        MemoryLayout pointLayout = MemoryLayout.structLayout(ValueLayout.JAVA_INT.withName("x"), ValueLayout.JAVA_INT.withName("y")); // Struct layout for a point (x, y)
        SequenceLayout pointsLayout = MemoryLayout.sequenceLayout(numberOfPoints, pointLayout); // Sequence layout for an array of points (x, y)

        // ValueLayout - A ValueLayout models a memory layout for basic data types such as integer and floating types
        ValueLayout intLayout = ValueLayout.JAVA_INT;
        ValueLayout charLayout = ValueLayout.JAVA_CHAR;
        assert intLayout.byteSize() == 4;
        assert charLayout.byteSize() == 2;

        // SequenceLayout - A SequenceLayout models a memory layout for an array of elements of the same type
        SequenceLayout sequenceLayout = MemoryLayout.sequenceLayout(10, ValueLayout.JAVA_INT);

        // GroupLayout - A GroupLayout can combine multiple member layouts, which can be similar or a combination of different types
        GroupLayout similarTypeGroupLayout = MemoryLayout.structLayout(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT); // Struct layout with similar types
        GroupLayout differentTypeGroupLayout = MemoryLayout.unionLayout(ValueLayout.JAVA_INT, ValueLayout.JAVA_LONG); // Union layout with different types
        MemoryLayout memoryLayout1 = ValueLayout.JAVA_INT;
        MemoryLayout memoryLayout2 = MemoryLayout.structLayout(ValueLayout.JAVA_LONG);
        MemoryLayout complexLayout = MemoryLayout.structLayout(memoryLayout1, MemoryLayout.paddingLayout(4), memoryLayout2); // Complex layout with padding
    }

    static void varHandles() {
        // VarHandle - A VarHandle provides a way to access and manipulate memory segments based on their layouts
        int value = 10;
        MemoryLayout pointLayout = MemoryLayout.structLayout(ValueLayout.JAVA_INT.withName("x"), ValueLayout.JAVA_INT.withName("y"));
        VarHandle xHandle = pointLayout.varHandle(MemoryLayout.PathElement.groupElement("x"));
        Arena arena = Arena.ofAuto();
        MemorySegment segment = arena.allocate(pointLayout);
        xHandle.set(segment, 0, (int) value);
        int xValue = (int) xHandle.get(segment, 0);
        assert xValue == value;

        // VarHandle with Offset
        int numberOfPoints = 10;
        SequenceLayout pointsLayout = MemoryLayout.sequenceLayout(numberOfPoints, pointLayout);
        VarHandle xHandle2 = pointsLayout.varHandle(MemoryLayout.PathElement.sequenceElement(), MemoryLayout.PathElement.groupElement("x"));
        Arena arena2 = Arena.ofAuto();
        MemorySegment segment2 = arena2.allocate(pointsLayout);
        for (int i = 0; i < numberOfPoints; i++) {
            xHandle2.set(segment2, 0, i, i);
        }
        for (int i = 0; i < numberOfPoints; i++) {
            assert xHandle2.get(segment2, 0, i).equals(i);
        }
    }

    static void invokingNativeFunctions() throws Throwable {
        Linker linker = Linker.nativeLinker(); // The Linker class manages the loading and unloading of native libraries
        var symbol = linker.defaultLookup().find("strlen").orElseThrow(); // symbolLookup class resolves function symbols within those libraries, enabling the establishment of a connection between Java code and foreign functions defined in native libraries
        // MethodHandle class serves as a bridge between Java and foreign functions, representing a reference to a function that can be invoked from Java code
        MethodHandle strlen = linker.downcallHandle(symbol, FunctionDescriptor.of(ValueLayout.JAVA_LONG, ValueLayout.ADDRESS)); // The FunctionDescriptor class describes the signature of a foreign function, specifying the return type, parameter types, and relevant information
        Arena arena = Arena.ofAuto();
        MemorySegment str = arena.allocateFrom("Hello");
        long len = (long) strlen.invoke(str);

        assert 5 == len;
    }

}
