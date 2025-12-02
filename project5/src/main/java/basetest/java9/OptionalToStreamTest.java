package basetest.java9;

import java.util.Optional;
import java.util.stream.Stream;

public class OptionalToStreamTest {

    public static void main(String... args) {
        Optional<String> optional = Optional.of("Hello, World!");
        Stream<String> stream = optional.stream();
        stream.forEach(System.out::println);
    }
}
