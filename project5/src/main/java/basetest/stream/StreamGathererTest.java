package basetest.stream;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.stream.Gatherer;
import java.util.stream.Gatherers;
import java.util.stream.Stream;

/**
 * Stream Gatherer can do:
 * - Transform elements in a one-to-one, one-to-many, many-to-one, or many-to-many fashion
 * - Track previously seen elements to influence the transformation of later elements
 * - Short-circuit, or stop processing input elements to transform infinite streams to finite ones
 * - Process a stream in parallel (only when combiner function in the gatherer)
 *
 * Basic usage:
 * - Grouping elements into batches
 * - Deduplicating consecutively similar elements
 * - Incremental accumulation functions
 * - Incremental reordering functions
 *
 * There are built-in gatherers in java:
 *  -
 */
public class StreamGathererTest {
    public static void main(String...args) {

    }

    static void builtInGatherers() {
        // Fold - accumulates elements into a single result
        var data = Stream.of(1,2,3,4,5,6,7,8,9);
        data.gather(
                                  Gatherers.fold(
                                      () -> "",
                                      (result, element) -> {
                                          if (result.equals("")) return element.toString();
                                          return result + ";" + element;
                                      }
                              )
                      )
                      .findFirst()
                      .get();
        System.out.println(data);

        //mapConcurrent - One-to-one Gatherer, transformation (map) that processes elements in parallel (upto specified max concurrency, preserves order)
        data.gather(Gatherers.mapConcurrent(5, String::valueOf))
                .forEach(System.out::println);

        // scan - One-to-one Gatherer that performs prefix-scan with incremental accumulation
        data.gather(Gatherers.scan(() -> 0, (accumulator, element) -> Integer.sum(accumulator, element)))
                      .forEach(System.out::println);

        // windowFixed - Many-to-many Gatherer, that gathers elements into fixed-size windows (batches)
        data.gather(Gatherers.windowFixed(3))
                .forEach(batch -> System.out.println(batch));

        // windowSliding - Many-to-many Gatherer, that gathers elements into sliding windows (each subsequent window includes all elements of the previous window except for its first element)
        data.gather(Gatherers.windowSliding(3))
                .forEach(batch -> System.out.println(batch));

        // Compose two or more Gatherers
        Gatherer<Integer, ?, Integer> sc = Gatherers.scan(() -> 100, (current, next) -> current + next);
        Gatherer<Integer, ?, String> fo = Gatherers.fold(() -> "", (result, element) -> {
                                   if (result.equals("")) return element.toString();
                                   return result + ";" + element;
                               });
        data.gather(sc.andThen(fo))
                .forEach(System.out::println);
    }

    static void findLargestInt() {
        System.out.println(Stream.of(5, 4, 2, 1, 6, 12, 8, 9)
                                 .gather(new BiggestInt(11))
                                 .findFirst()
                                 .get());

        System.out.println(Stream.of(5, 4, 2, 1, 6, 12, 8, 9)
                                 .gather(new BiggestInt(11))
                                 .parallel() // test parallel processing
                                 .findFirst()
                                 .get());

    }

    static void gathererBasedOnElementMapping() {
        // One-to-one Gatherer that maps each input string to its length, The only mandatory function of a Gatherer is the integrator()
        List<Integer> expectedOutput = List.of(5, 6, 3);
        Stream<String> inputStrings = Stream.of("apple", "banana", "cat");
        List<Object> resultList = inputStrings.gather(Gatherer.of((state, element, downstream) -> {
                                                  downstream.push(element.length());
                                                  return true;
                                              }))
                                              .toList();

        // One-to-many, initializer, combiner, integrator are required
        class SentenceSplitterGatherer implements Gatherer<String, List<String>,String> {

            @Override
            public Supplier<List<String>> initializer() {
                return ArrayList::new;
            }

            @Override
            public BinaryOperator<List<String>> combiner() {
                return (left, right) -> {
                    left.addAll(right);
                    return left;
                };
            }

            @Override
            public Integrator<List<String>, String, String> integrator() {
                return (state, element, downstream) -> {
                    var words = element.split("\\s+");
                    for (var word : words) {
                        state.add(word);
                        downstream.push(word);
                    }
                    return true;
                };
            }
        }
        Stream<String> sentences = Stream.of("Hello world", "This is a test", "Gatherer example");
        List<String> words = sentences.gather(new SentenceSplitterGatherer()).toList();
        System.out.println(words);

        // Many-to-one
        class NumericSumGatherer implements Gatherer<Integer, ArrayList<Integer>, Integer> {

            @Override
            public Supplier<ArrayList<Integer>> initializer() {
                return ArrayList::new;
            }

            @Override
            public Integrator<ArrayList<Integer>, Integer, Integer> integrator() {
                return new Integrator<>() {
                    @Override
                    public boolean integrate(ArrayList<Integer> state,
                                             Integer element, Downstream<? super Integer> downstream) {
                        if (state.isEmpty()) {
                            state.add(element);
                        } else {
                            state.addFirst(state.getFirst() + element);
                        }
                        return true;
                    }
                };
            }

            @Override
            public BiConsumer<ArrayList<Integer>, Downstream<? super Integer>> finisher() {
                return (state, downstream) -> {
                    if (!downstream.isRejecting() && !state.isEmpty()) {
                        downstream.push(state.getFirst());
                        state.clear();
                    }
                };
            }
        }
        Stream<Integer> inputValues = Stream.of(1, 2, 3, 4, 5, 6);
        List<Integer> result = inputValues.gather(new NumericSumGatherer()).toList();
        System.out.println(result); // Output: [1, 3, 6, 10, 15, 21]

        //Many-to-many
        class SlidingWindowGatherer implements Gatherer<Integer, Deque<Integer>, List<Integer>> {

            @Override
            public Supplier<Deque<Integer>> initializer() {
                return ArrayDeque::new;
            }

            @Override
            public Integrator<Deque<Integer>, Integer, List<Integer>> integrator() {
                return new Integrator<>() {
                    @Override
                    public boolean integrate(Deque<Integer> state,
                                             Integer element, Downstream<? super List<Integer>> downstream) {
                        state.addLast(element);
                        if (state.size() == 3) {
                            downstream.push(new ArrayList<>(state));
                            state.removeFirst();
                        }
                        return true;
                    }
                };
            }

            @Override
            public BiConsumer<Deque<Integer>, Downstream<? super List<Integer>>> finisher() {
                return (state, downstream) -> {};
            }
        }
        Stream<Integer> numbers = Stream.of(1, 2, 3, 4, 5);
        List<List<Integer>> resultList2 = numbers.gather(new SlidingWindowGatherer()).toList();
    }

    // Create Gatherer by using Gatherer.of() static method
    static Gatherer<Integer, List<Integer>, Integer> biggestInt(int limit) {

        return Gatherer.of(

                // Supplier

                () -> { return new ArrayList<Integer>(1); },

                // Integrator

                Gatherer.Integrator.of(
                        (max, element, downstream) -> {
                            System.out.println("Processing " + element);
                            if (max.isEmpty()) max.addFirst(element);
                            else if (element > max.getFirst()) max.set(0, element);

                            if (element >= limit) {
                                downstream.push(element);
                                return false;
                            }
                            return true;
                        }
                ),

                // Combiner

                (leftMax, rightMax) -> {
                    if (leftMax.isEmpty()) return rightMax;
                    if (rightMax.isEmpty()) return leftMax;
                    int leftVal = leftMax.getFirst();
                    int rightVal = rightMax.getFirst();
                    if (leftVal > rightVal) return leftMax;
                    else return rightMax;
                },

                // Finisher

                (max, downstream) -> {
                    if (!max.isEmpty()) {
                        downstream.push(max.getFirst());
                    }
                }
        );
    }
}

/** Create Gatherer that finds the biggest integer in a stream of integers, if any value is same/greater than limit
 * then it returns and stops processing
 *
 * Stream Gatherer can be created by implementing Gatherer interface and overriding its four methods:
 * - initializer(): creates gatherer's private state
 * - integrator(): Integrates a new element from the input stream, possibly inspects the private state object, and possibly emits elements to the output stream
 * - combiner(): combines two private state objects, used during parallel processing
 * - finisher(): Optionally performs an action after the gatherer has processed all input elements; it could inspect the private state object or emit additional output elements
 */
record BiggestInt(int limit) implements Gatherer<Integer, List<Integer>, Integer> {

    // The initializer creates a new private ArrayList to keep track of the
    // largest integer across elements.
    @Override
    public Supplier<List<Integer>> initializer() {
        return () -> new ArrayList<>(1);
    }

    // The integrator
    @Override
    public Integrator<List<Integer>, Integer, Integer> integrator() {
        return Integrator.of(
                (max, element, downstream) -> {

                    // Save the integer if it's the largest so far.
                    if (max.isEmpty()) max.addFirst(element);
                    else if (element > max.getFirst()) max.set(0, element);

                    // If the integer is equal or greater to the limit,
                    // "short-circuit": emit the current integer downstream
                    // and return false to stop processing stream elements
                    if (element >= limit) {
                        downstream.push(element);
                        return false;
                    }

                    // Return true to continue processing stream elements
                    return true;
                }
        );
    }

    // The combiner, which is used during parallel evaluation
    @Override
    public BinaryOperator<List<Integer>> combiner() {
        return (leftMax, rightMax) -> {

            // If either the "left" or "right" ArrayLists contain
            // no value, then return the other
            if (leftMax.isEmpty()) return rightMax;
            if (rightMax.isEmpty()) return leftMax;

            // Return the ArrayList that contains the larger integer
            int leftVal = leftMax.getFirst();
            int rightVal = rightMax.getFirst();
            if (leftVal > rightVal) return leftMax;
            else return rightMax;
        };
    }

    @Override
    public BiConsumer<List<Integer>, Downstream<? super Integer>> finisher() {

        // Emit the largest integer, if there is one, downstream
        return (max, downstream) -> {
            if (!max.isEmpty()) {
                downstream.push(max.getFirst());
            }
        };
    }
}