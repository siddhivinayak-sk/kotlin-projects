package basetest.java23;

import module java.base; // Import complete module instead of class/package/static imports

import static java.time.temporal.ChronoUnit.SECONDS;

/// This is a test class to demonstrate Java 23 language features
/// @version 1.0
/// @uses java.lang.String
/// @provides java.lang.Runnable
/// @module java.base
/// @requires java.logging
public class LanguageFeatureTest {

    /// It is starting point for Java application
    /// @param args command line arguments
    /// @return void
    /// @throws Exception if any error occurs
    /// @see <a href="https://docs.oracle.com/en/java/javase/23/docs/api/java.base/java/lang/String.html#main(java.lang.String...)" target="_blank">String.main(String...)</a>
    /// @since Java 23
    /// @author Me
    /// @deprecated This is just a test method
    /// @implSpec This method is implemented as a simple print statement
    /// @implNote No special implementation notes
    /// @apiNote This is a simple API note
    /// @serialData No serialization data
    /// `{ System.out.println("Hello, Java 23! Code example in documentation"); }`
    /// It was introduced into Java 23 to use markdown style documentation comments.
    public static void main(String...args) throws Exception {

    }

    // Demonstrates pattern matching for primitive types
    static void primitiveTypesInPatternMatching() {
        int obj = 42;
        if (obj instanceof byte i && i > 10) {
            System.out.println("It's an int: " + i);
        } else {
            System.out.println("Not an int");
        }
    }

    static void primitiveTypeInSwitch() {
        double value = 3.14;
        switch (value) {
            case byte   b -> System.out.println(value + " instanceof byte:   " + b);
            case short  s -> System.out.println(value + " instanceof short:  " + s);
            case char   c -> System.out.println(value + " instanceof char:   " + c);
            case int    i -> System.out.println(value + " instanceof int:    " + i);
            case long   l -> System.out.println(value + " instanceof long:   " + l);
            case float  f -> System.out.println(value + " instanceof float:  " + f);
            case double d -> System.out.println(value + " instanceof double: " + d);
        }
    }

    static void consoleWithLocale() {
        Console console = System.console();

        var name = console.readLine(Locale.US, "What's your name (π = %.4f)? ", Math.PI);
        var password = console.readPassword(Locale.US, "Your password (e = %.4f)? ", Math.E);

        console.printf(Locale.US, "Your name is %s%n", name);
        console.format(Locale.US, "Your password starts with %c%n", password[0]);
    }

    static void instantUntilExample() {
        Instant now = Instant.now();
        Instant later = Instant.now().plus(ThreadLocalRandom.current().nextInt(), SECONDS);
        Duration duration = now.until(later);
    }

}
