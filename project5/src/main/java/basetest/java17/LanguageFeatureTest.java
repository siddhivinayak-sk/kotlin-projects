package basetest.java17;

import java.util.random.RandomGeneratorFactory;
import java.util.stream.IntStream;

public class LanguageFeatureTest {
    public static void main(String...args) {

    }


    // Enhanced Pseudo-Random Number Generators
    // Legacy random classes, such as java.util.Random, SplittableRandom and SecureRandom now extend the new RandomGenerator interface
    public IntStream getPseudoInts(String algorithm, int streamSize) {
        // returns an IntStream with size @streamSize of random numbers generated using the @algorithm
        // where the lower bound is 0 and the upper is 100 (exclusive)
        return RandomGeneratorFactory.of(algorithm)
                                     .create()
                                     .ints(streamSize, 0,100);
    }

    // Pattern match for Switch
    static record Human (String name, int age, String profession) {}
    static class Shape { public int getNumberOfSides() { return 0; } }
    static class Circle extends Shape {}
    static class Triangle extends Shape {}
    public String checkObject(Object obj) {
        return switch (obj) {
            case Human h -> "Name: %s, age: %s and profession: %s".formatted(h.name(), h.age(), h.profession());
            case Circle c -> "This is a circle";
            case Shape s -> "It is just a shape";
            case null -> "It is null";
            default -> "It is an object";
        };
    }

    /*
    public String checkShape(Shape shape) {
        return switch (shape) {
            case Triangle t && (t.getNumberOfSides() != 3) -> "This is a weird triangle";
            case Circle c && (c.getNumberOfSides() != 0) -> "This is a weird circle";
                default -> "Just a normal shape";
        };
    }
    */
}
