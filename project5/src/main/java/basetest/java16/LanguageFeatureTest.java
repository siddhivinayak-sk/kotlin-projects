package basetest.java16;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LanguageFeatureTest {
    public static void main(String[] args) {
        // Day period support in DateTimeFormatter
        LocalTime date = LocalTime.parse("15:25:08.690791");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h B");
        String formattedDate = date.format(formatter); // 3 in the afternoon

        //Stream to List
        List<String> integersAsString = Arrays.asList("1", "2", "3");
        List<Integer> ints = integersAsString.stream().map(Integer::parseInt).collect(Collectors.toList());
        List<Integer> intsEquivalent = integersAsString.stream().map(Integer::parseInt).toList(); // Stream to List

        //Multipy two matrix using Vector API
        /*
        int[] a = {1, 2, 3, 4};
        int[] b = {5, 6, 7, 8};
        var vectorA = IntVector.fromArray(IntVector.SPECIES_128, a, 0);
        var vectorB = IntVector.fromArray(IntVector.SPECIES_128, b, 0);
        var vectorC = vectorA.mul(vectorB);
        vectorC.intoArray(c, 0);
         */


    }

}
