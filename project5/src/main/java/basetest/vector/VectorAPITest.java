//package basetest.vector;
//
///**
// * The Java Vector API is an advanced feature that allows developers to leverage the Single Instruction, Multiple Data (SIMD)
// * capabilities of modern CPUs, resulting in significant performance improvements for data-parallel computations.
// *
// * SIMD machine will see memory as an array, or a range of values, we call these a Vector, and any operation that a SIMD machine performs
// * becomes a vector operation. Types of vector operations classified as lane-wise operations and cross-lane operations:
// * A lane-wise operation, as the name suggests, only performs a scalar operation on a single lane on one or more vectors at a time. For example,
// * adding two vectors together by adding each corresponding lane is a lane-wise operation.
// * A cross-lane operation can compute or modify data from different lanes of a vector. Sorting the components of a vector is an example of
// * a cross-lane operation.
// *
// * Vector<E> class has six abstract subclasses for each of the six supporting types: ByteVector, ShortVector, IntVector, LongVector,
// * FloatVector, and DoubleVector.
// *
// * see links:
// * https://www.baeldung.com/java-vector-api
// */
//public class VectorAPITest {
//    public static void main(String[] args) {
//        System.out.println("Vector API Test Placeholder");
//    }
//
//    // The traditional scalar way to add two arrays element-wise
//    public int[] addTwoScalarArrays(int[] arr1, int[] arr2) {
//        int[] result = new int[arr1.length];
//        for(int i = 0; i< arr1.length; i++) {
//            result[i] = arr1[i] + arr2[i];
//        }
//        return result;
//    }
//
//    static final VectorSpecies<Integer> SPECIES = IntVector.SPECIES_PREFERRED;
//    // Using Vector API to add two arrays element-wise
//    // It runs well and provides the advertised performance only if the number of lanes matches the size of the vectors the SIMD machine can handle
//    public int[] addTwoVectorArrays(int[] arr1, int[] arr2) {
//        var v1 = IntVector.fromArray(SPECIES, arr1, 0);
//        var v2 = IntVector.fromArray(SPECIES, arr2, 0);
//        var result = v1.add(v2);
//        return result.toArray();
//    }
//
//    // Take the help of VectorMasks when we are unable to fill the entire input data into our vector
//    // A mask selects the lane to which an operation is to be applied. The operation is applied if the corresponding
//    // value in the lane is true, or a different fallback action is performed if it is false
//    // These masks help us perform operations independent of the vector shape and size
//    public int[] addTwoVectorsWithMasks(int[] arr1, int[] arr2) {
//        int[] finalResult = new int[arr1.length];
//        int i = 0;
//        for (; i < SPECIES.loopBound(arr1.length); i += SPECIES.length()) {
//            var mask = SPECIES.indexInRange(i, arr1.length);
//            var v1 = IntVector.fromArray(SPECIES, arr1, i, mask);
//            var v2 = IntVector.fromArray(SPECIES, arr2, i, mask);
//            var result = v1.add(v2, mask);
//            result.intoArray(finalResult, i, mask);
//        }
//       // tail cleanup loop
//        for (; i < arr1.length; i++) {
//            finalResult[i] = arr1[i] + arr2[i];
//        }
//        return finalResult;
//    }
//
//
//}
