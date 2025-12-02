package basetest.java9;

public class TryWithResourceTest {

    public static void main(String[] args) {
        try (Resource resource1 = new Resource("Resource1"); Resource resource2 = new Resource("Resource2")) {
            resource1.use();
            resource2.use();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class Resource implements AutoCloseable {

        private final String name;

        Resource(String name) {
            this.name = name;
            System.out.println(name + " opened");
        }

        void use() {
            System.out.println(name + " used");
        }

        @Override
        public void close() {
            System.out.println(name + " closed");
        }
    }
}