package basetest.multithreading.stucturedConcurrency;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.StructuredTaskScope;
import java.util.function.Function;

import static java.lang.System.out;
import static java.lang.System.err;

public class StructuredConcurrencyTest {

    //Problem Statement: You need to perform multiple tasks concurrently and combine their results, while handling potential errors gracefully.
    void manageTasksUsingExecutorService() {
        Future<Shelter> shelter;
        Future<List<Dog>> dogs;
        long startTime = 0;
        try (ExecutorService executorService = Executors.newFixedThreadPool(3)) {
            shelter = executorService.submit(this::getShelter);
            dogs = executorService.submit(this::getDogs);
            startTime = System.currentTimeMillis();
            Shelter theShelter = shelter.get();   // Join the shelter
            List<Dog> theDogs = dogs.get();  // Join the dogs
            Response response = new Response(theShelter, theDogs); // Combine results
            //Problem statement: Even if getDogs() fails, getShelter() will complete successfully, but we are not handling that scenario here.
            out.println("Response: " + response);
            out.println("Consumed Time: " + (System.currentTimeMillis() - startTime));
        } catch (ExecutionException | InterruptedException e) {
            err.println("Error occurred: " + e.getMessage());
            out.println("Consumed Time: " + (System.currentTimeMillis() - startTime));
        }
    }

    void manageWithStructuredTaskScope() {
        long startTime = 0;
        /*
         * Joiner Policies:
         * 1. awaitAllSuccessfulOrThrow(): The joiner's result method returns null if all subtasks complete successfully, or throws the exception from the first subtask to fail.
         * 2. allSuccessfulOrThrow(): If all subtasks complete successfully. If any subtask failed then the result method throws the exception from the first subtask to fail.
         * 3. anySuccessfulResultOrThrow(): When any subtask completes successfully, it returns that result, If all subtasks fail, it throws an exception.
         * 4. allUntil(): When all subtasks complete or a predicate returns { true} to cancel the scope, it executes for each subtask to evaluate if it should cancel the scope.
         * 5. awaitAll(): Await all for complete, if any sub-task fails joiner does not fail, it returns null result
         */
        var joinerPolicy = StructuredTaskScope.Joiner.awaitAllSuccessfulOrThrow();
        var configFunction = Function.<StructuredTaskScope.Configuration>identity(); // No special configuration
        try (var scope = StructuredTaskScope.open(joinerPolicy, configFunction)) {
            startTime = System.currentTimeMillis();
            StructuredTaskScope.Subtask<Shelter> shelter = scope.fork(this::getShelter);
            StructuredTaskScope.Subtask<List<Dog>> dogs = scope.fork(this::getDogs);
            scope.join();
            Response response = new Response(shelter.get(), dogs.get()); // Combine results
            //Problem solved: If getDogs() fails, the entire scope will be marked as failed, and we can handle it gracefully.
            out.println("Response: " + response);
        } catch (StructuredTaskScope.FailedException | InterruptedException e) {
            err.println("Error occurred: " + e.getMessage());
            out.println("Consumed Time: " + (System.currentTimeMillis() - startTime));
        }
    }

    Shelter getShelter() throws Exception {
        Thread.sleep(2000);
        return new Shelter("Happy Tails Shelter");
    }

    List<Dog> getDogs() throws Exception {
        Thread.sleep(500);
        throw new Exception("Failed to fetch dogs");
        //return List.of(new Dog("Buddy"), new Dog("Max"), new Dog("Bella"));
    }

    record Shelter(String name){}
    record Dog(String name){}
    record Response(Shelter shelter, List<Dog> dogs){}
    static void main(String...args) {
        StructuredConcurrencyTest test = new StructuredConcurrencyTest();
        out.println("---- Using ExecutorService ----");
        test.manageTasksUsingExecutorService();
        out.println("\n---- Using StructuredTaskScope ----");
        test.manageWithStructuredTaskScope();
    }
}
