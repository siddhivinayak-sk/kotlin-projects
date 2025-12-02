package basetest.java15;

public class SealedClassTest {

    abstract sealed class Person permits Employee, Manager {} //Sealed class permits only specified subclasses
    final class Employee extends Person {} //Final class cannot be extended
    non-sealed class Manager extends Person {} //Non-sealed class can be extended freely
    class SeniorManager extends Manager {} //Valid since Manager is non-sealed
}
