package basetest.java21;

public class LanguageFeatureTest {
    public static void main(String...args) {

    }

    record Point(int x, int y) {}
    public static int afterRecordPattern(Object obj) {
        if(obj instanceof Point(int x, int y)) { // Record Pattern Matching
            return x+y;
        }
        return 0;
    }


    enum Color {RED, GREEN, BLUE}
    record ColoredPoint(Point point, Color color) {} // Nested Record
    record RandomPoint(ColoredPoint cp) {}
    public static Color getRamdomPointColor(RandomPoint r) {
        if(r instanceof RandomPoint(ColoredPoint cp)) { // Nested Record Pattern Matching
            return cp.color();
        }
        return null;
    }


    static class Account{
        double getBalance(){
            return 0;
        }
    }
    static class SavingsAccount extends Account {
        double getSavings() {
            return 100;
        }
    }
    static class TermAccount extends Account {
        double getTermAccount() {
            return 1000;
        }
    }
    static class CurrentAccount extends Account {
        double getCurrentAccount() {
            return 10000;
        }
    }

    // Switch Pattern Matching
    static double getBalanceWithSwitchPattern(Account account) {
        double result = 0;
        switch (account) {
            case null -> throw new RuntimeException("Oops, account is null");
            case SavingsAccount sa -> result = sa.getSavings();
            case TermAccount ta -> result = ta.getTermAccount();
            case CurrentAccount ca -> result = ca.getCurrentAccount();
            default -> result = account.getBalance();
        };
        return result;
    }

//    static void stringTemplateExample() {
//        // String templates
//        String name = "Baeldung";
//        String welcomeText = STR."Welcome to \{name}"; // String template example, STR. should be prefixed as it is template processor
//        System.out.println(welcomeText);
//    }

    // Switch expression with 'when' guards
    static void switchExpressionWithWhen() {
        Object obj = "HelloWorld";
        switch (obj) {
            case String s when s.length() >= 5 -> System.out.println(s.toUpperCase());
            case Integer i                     -> System.out.println(i * i);
            case null, default                 -> System.out.println(obj);
        }
    }
}
