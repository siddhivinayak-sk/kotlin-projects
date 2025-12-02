package basetest.java13;

public class LanguageFeatureTest {
    public static void main(String[] args) {

        //Switch Expressions with yield
        var me = 4;
        var operation = "squareMe";
        var result = switch (operation) {
            case "doubleMe" -> {
                yield me * 2;
            }
            case "squareMe" -> {
                yield me * me;
            }
            default -> me;
        };

        // Text block
        String TEXT_BLOCK_JSON = """
        {
            "name" : "Baeldung",
            "website" : "https://www.%s.com/"
        }
        """;
    }
}
