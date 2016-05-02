# Stream Primes

This repostiry contains an own implmentation of a Stream inspired by [Functional Programming in Scala](https://www.manning.com/books/functional-programming-in-scala).
It provides an implementation for
- `constant`: Create a infinite stream with given integer
- `from`: Create an infinite stream counting from the given integer
- `empty`: Empty stream
- `filter`: Filter a stream for elements which yield true for a given predicate
- `foldRight`: Reduce the stream to one value with given aggreagate `BiFunction` and neutral element `z`.
- `primes`: Infinite stream of prime numbers
- `rangeClosed`: Range from `start` to `end` (inlcusive)
- `noneMatch`: True if none of the elements in a stream fullfills the predicate
- `anyMatch`: True if at least one element in a stream fullfills the predicate
- `allMatch`: True if all elements in a stream fullfill the predicate
- `limit`: limits the amount elements in the stream 

Primes generation can also be done with Java 8s Stream implementation (look into test source package).

# Examples
```Java
// Following code constructs a stream of one element
Stram<String> one = Stream.cons(() -> "Just one", () -> Stream.empty());

// Filter stream
Stream<String> one = Stream.cons(() -> "one", () -> Stream.cons(() -> "second", () -> Stream.empty())).filter("one"::equals); // Stream with one element

// Constant stream of ones
Stream<Integer> ones = Stream.constant(1);
```
