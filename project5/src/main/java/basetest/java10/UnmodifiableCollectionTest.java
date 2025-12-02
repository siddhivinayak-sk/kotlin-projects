package basetest.java10;

import java.util.List;
import java.util.stream.Collectors;

public class UnmodifiableCollectionTest {
    public static void main(String[] args) {
        List<String> oneList = List.of("one");
        List<String> twoList = List.copyOf(oneList);
        List<String> unmodifialble = oneList.stream().collect(Collectors.toUnmodifiableList());

    }
}
