package basetest.java9;

public interface TestableInterfaceWithPrivateMethod {

    String myFunction();

    private String privateHelper() {
        return "Hello from private method!";
    }

    default String publicHelper() {
        return privateHelper();
    }
}