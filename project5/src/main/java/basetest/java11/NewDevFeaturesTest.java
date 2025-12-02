package basetest.java11;

import javax.annotation.Nonnull;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class NewDevFeaturesTest {
    public  static void main(String[] args) throws Exception {
        String str = " Hello, Java 11! ";
        str.isBlank(); // true if the string is empty or contains only whitespace
        str.lines().forEach(System.out::println);
        str.strip(); // removes leading and trailing whitespace
        str.stripLeading(); // removes leading whitespace
        str.stripTrailing(); // removes trailing whitespace
        str.repeat(2); // repeats the string n times

        //readString and writeString methods in Files class
        Path filePath = Files.writeString(Files.createTempFile(Path.of("."), "demo", ".txt"), "Sample text");
        String fileContent = Files.readString(filePath);

        //Collection to Array
        var list = List.of("a", "b", "c");
        String[] array = list.toArray(String[]::new);

        //Not Predicate method
        List<String> sampleList = Arrays.asList("Java", "\n \n", "Kotlin", " ");
        List withoutBlanks = sampleList.stream()
                                       .filter(Predicate.not(String::isBlank))
                                       .collect(Collectors.toList());

        //Local variables in Lambda Expressions
        String resultString = sampleList.stream()
                                        .map((@Nonnull var x) -> x.toUpperCase())
                                        .collect(Collectors.joining(", "));

        //Concept of Nestmates
        boolean isNested = NewDevFeaturesTest.class.isNestmateOf(NewDevFeaturesTest.NestedTestClass.class);
        var nestedClasses = NewDevFeaturesTest.class.getNestMembers();

    }


    class NestedTestClass {}
}
