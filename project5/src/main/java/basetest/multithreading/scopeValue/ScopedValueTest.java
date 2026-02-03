package basetest.multithreading.scopeValue;

import java.util.concurrent.StructuredTaskScope;

import static java.lang.System.out;
import static java.lang.System.err;

/**
 * ScopedValue - Store and share immutable data within and across threads
 * Introduced in Java 20, Once ScopedValue written, then it becomes immutable
 * It is alternative to ThreadLocal for sharing data in a more controlled manner. ThreadLocal has following drawbacks:
 * 1. Mutable - Can be set and changed anywhere in the thread, leading to potential inconsistencies.
 * 2. Data Retained - Data remains in memory until the thread ends, or explicitly removed, leading to memory leaks.
 * 3. Inheritance Issues - Thread-local variables of a parent thread can be inherited by child threads
 */
public class ScopedValueTest {

    static void main(String...args) {
        //useScopedValue();
        //rebindingScopedValue();
        //throwWhenNotBound();
        inheritanceWithStructuredTaskScope();
    }

    record User(String name, String role) {}
    public final static ScopedValue<User> LOGGED_IN_USER = ScopedValue.newInstance();
    public static void useScopedValue() {
        try {
            out.println("Outside Thread Name: " + Thread.currentThread().getName());
            out.println("Trying to access outside the scope:" + LOGGED_IN_USER.get());
        } catch (Exception ex) {
            err.println(ex.getMessage());
        }
        var user = new User("A101", "ADMIN");
        ScopedValue.where(LOGGED_IN_USER, user).run(() -> {
            out.println("Thread Name: " + Thread.currentThread().getName());
            out.println("User Name: " + LOGGED_IN_USER.get().name());
            out.println("User Role: " + LOGGED_IN_USER.get().role());
        });
    }

    //The ScopedValue API allows a new binding to be established for nested dynamic scopes. This is known as rebinding.
    public static void rebindingScopedValue() {
        ScopedValue.where(LOGGED_IN_USER, new User("user-1", "role-1")).run(() -> {
            out.println("Thread Name: " + Thread.currentThread().getName());
            out.println("Outer User Name: " + LOGGED_IN_USER.get().name());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            ScopedValue.where(LOGGED_IN_USER, new User("user-2", "role-2")).run(() -> {
                out.println("Thread Name: " + Thread.currentThread().getName());
                out.println("Inner User Name: " + LOGGED_IN_USER.get().name());
            });
            out.println("Outer User Name: " + LOGGED_IN_USER.get().name());
        });
    }

    public static void throwWhenNotBound() {
        ScopedValue.where(LOGGED_IN_USER, new User("parent-user", "parent-role")).run(() -> {
            out.println("Parent Thread Name: " + Thread.currentThread().getName());
            out.println("Parent User Name: " + LOGGED_IN_USER.get().name());

            Thread childThread = new Thread(() -> {
                out.println("Child Thread Name: " + Thread.currentThread().getName());
                try {
                    out.println("Child User Name: " + LOGGED_IN_USER.get().name());
                } catch (Exception ex) {
                    err.println("Child thread cannot access parent's ScopedValue: " + ex.getMessage());
                }
            });

            childThread.start();
            try {
                childThread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            out.println("Parent Thread Name: " + Thread.currentThread().getName());
            out.println("Parent User Name: " + LOGGED_IN_USER.get().name());
        });
    }

    //When using StructuredTaskScope, child threads can inherit ScopedValue bindings from their parent thread.
    public static void inheritanceWithStructuredTaskScope() {
        ScopedValue.where(LOGGED_IN_USER, new User("parent-user", "parent-role")).run(() -> {
            out.println("Parent Thread Name: " + Thread.currentThread().getName());
            out.println("Parent User Name: " + LOGGED_IN_USER.get().name());

            try (var scope = StructuredTaskScope.open()) {
                scope.fork(() -> {
                    out.println("Child Thread 1 Name: " + Thread.currentThread().getName());
                    out.println("Child User Name: " + LOGGED_IN_USER.get().name());
                });
                scope.fork(() -> {
                    out.println("Child Thread 2 Name: " + Thread.currentThread().getName());
                    out.println("Child User Name: " + LOGGED_IN_USER.get().name());
                });
                scope.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            out.println("Parent Thread Name: " + Thread.currentThread().getName());
            out.println("Parent User Name: " + LOGGED_IN_USER.get().name());
        });
    }

}


