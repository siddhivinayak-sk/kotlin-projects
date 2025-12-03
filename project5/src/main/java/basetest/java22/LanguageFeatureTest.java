package basetest.java22;

import java.lang.classfile.ClassElement;
import java.lang.classfile.ClassFile;
import java.lang.classfile.ClassModel;
import java.lang.classfile.MethodModel;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Gatherers;

public class LanguageFeatureTest {

    public static void main(String...args) {

        // Unnamed variables (_)
        int somenumber = 10;
        try {
            int value = somenumber / 0;
        } catch (ArithmeticException _) {
            System.out.println("Using '_' as a variable name is not allowed in Java 22.");
        }
    }

    // Statement before super() call in constructor
    static class Shape { int size; int length; Shape(int size, int length) { this.size = size; this.length = length; } }
    static class Circle extends Shape {
        Circle(int size, int length) {
            if (size < 0 || length < 0)
                throw new IllegalArgumentException("Size and length must be non-negative"); // Validation can be added before super call
            super(size, length);
        }
    }

    // Implicitly Declared Classes and Instance Main Methods
    // Simply add this into any java file (unnamed package and unnamed module) and it will run with 'java UnnamedFile.java'
    /*
    void main() {
        System.out.println("Instance main method in Java 22");
    }
     */

    // ClassFile API - All changes into .class files programmatically
    private static void classFileTest() throws Exception{
        ClassFile cf = ClassFile.of();
        ClassModel classModel = cf.parse(Path.of("SomeClass.class"));
        byte[] newBytes = cf.build(classModel.thisClass().asSymbol(), classBuilder -> {
            for (ClassElement ce : classModel) {
                if (!(ce instanceof MethodModel mm && mm.methodName()
                                                        .stringValue()
                                                        .startsWith("get"))) {
                    classBuilder.with(ce);
                }
            }
        });
    }

    // Stream gather() method
    public static List<List<String>> gatherIntoWindowsSliding(List<String> countries) {
        List<List<String>> windows = countries
                .stream()
                .gather(Gatherers.windowSliding(3))
                .toList();
        return windows;
    }

    public static List<List<String>> gatherIntoWindowsFixed(List<String> countries) {
        List<List<String>> windows = countries
                .stream()
                .gather(Gatherers.windowFixed(3))
                .toList();
        return windows;
    }




}
