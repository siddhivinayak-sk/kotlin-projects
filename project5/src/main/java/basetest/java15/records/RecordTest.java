package basetest.java15.records;

public class RecordTest {

}

record Person(String name, int age) {  // Pojo with setter and getter
    public Person { //Allows validation logic
        if(age < 0) {
            throw new IllegalArgumentException("Age cannot be negative");
        }
    }
}
