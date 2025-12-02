package basetest.java12;

import java.nio.file.Files;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LanguageFeatureTest {

    public static void main(String[] args) throws Exception {

        var message = "Hello, Java 12!";
        message.indent(5); // Indent each line by 5 spaces

        long mismatch = Files.mismatch(Path.of("file1.txt"), Path.of("file2.txt")); // Find the first byte that differs between two files

        double mean = Stream.of(1, 2, 3, 4, 5)
                            .collect(Collectors.teeing(Collectors.summingDouble(i -> i), //teeing - combine two collectors
                                                       Collectors.counting(), (sum, count) -> sum / count));

        //Number format short and long
        var likesShort = NumberFormat.getCompactNumberInstance(new Locale("en", "US"), NumberFormat.Style.SHORT);
        likesShort.setMaximumFractionDigits(2);
        var likesLong = NumberFormat.getCompactNumberInstance(new Locale("en", "US"), NumberFormat.Style.LONG);
        likesLong.setMaximumFractionDigits(2);
        String shortFormat = likesShort.format(15300); // "15.3K"
        String longFormat = likesLong.format(15300);   // "15.3 thousand


        // Example of using the new switch expression introduced in Java 12
        int day = 3;
        String dayType = switch (day) {
            case 1, 2, 3, 4, 5 -> "Weekday";
            case 6, 7 -> "Weekend";
            default -> "Invalid day";
        };

        // instanceof pattern matching
        Object obj = "Hello, World!";
        if (obj instanceof String str) {
            System.out.println("String length: " + str.length());
        }
    }

}
