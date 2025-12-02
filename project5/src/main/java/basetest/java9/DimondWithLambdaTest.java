package basetest.java9;

public class DimondWithLambdaTest {

    public static void main(String[] args) {
        FooClass<Integer> fc = new FooClass<>(1) { // anonymous inner class
        };

        FooClass<? extends Integer> fc0 = new FooClass<>(1) {
            // anonymous inner class
        };

        FooClass<?> fc1 = new FooClass<>(1) { // anonymous inner class
        };

    }

    static class FooClass<T> {

        T value;

        FooClass(T value) {
            this.value = value;
        }

    }

}
