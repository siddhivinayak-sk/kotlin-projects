package basetest.java19;

import java.util.HashMap;
import java.util.Map;

public class LanguageFeatureTest {
    public static void main(String...args) {

        // Pre-allocated HashMap with initial capacity (without using loadfactor)
        Map<String, Integer> map = HashMap.newHashMap(120);


    }

    // Record Pattern Matching improvements with instaceof and switch
    record Position(int x, int y) {}
    private void print(Object object) {
        if (object instanceof Position position) {
            System.out.println("object is a position, x = " + position.x()
                               + ", y = " + position.y());
        }
    }

    private void print2(Object object) {
        if (object instanceof Position(int x, int y)) {
            System.out.println("object is a position, x = " + x + ", y = " + y);
        }
        // else ...
    }

    private void print3(Object object) {
        switch (object) {
            case Position position
                    -> System.out.println("object is a position, x = " + position.x()
                                          + ", y = " + position.y());
            default -> {}
        }
    }

    private void print4(Object object) {
        switch (object) {
            case Position(int x, int y)
                    -> System.out.println("object is a position, x = " + x + ", y = " + y);

            default -> {}
        }
    }

    public record Path(Position from, Position to) {}
    private void print5(Object object) {
        if (object instanceof Path(Position(int x1, int y1), Position(int x2, int y2))) {
            System.out.println("object is a path, x1 = " + x1 + ", y1 = " + y1
                               + ", x2 = " + x2 + ", y2 = " + y2);
        }
    }

    private void print6(Object object) {
        switch (object) {
            case Path(Position(int x1, int y1), Position(int x2, int y2))
                    -> System.out.println("object is a path, x1 = " + x1 + ", y1 = " + y1
                                          + ", x2 = " + x2 + ", y2 = " + y2);
            default -> {}
        }
    }
}
