package basetest.java10;

import java.util.ArrayList;
import java.util.List;

public class OptionalElseThrowTest {
    public static void main(String[] args) {
        List<Integer> someIntList = new ArrayList<>();
        Integer firstEven = someIntList.stream()
                                       .filter(i -> i % 2 == 0)
                                       .findFirst()
                                       .orElseThrow();
    }
}
