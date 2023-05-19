#Spring Semester OOP in JAVA
Contains old practice in 2022 Summer

The "diamond problem" is an ambiguity that arises when two classes B and C inherit from A, and class D inherits from both B and C. If there is a method in A that B and C have overridden, and D does not override it, then which version of the method does D inherit: that of B, or that of C?

List<Integer> res turn into int[]

int[] arr = res.stream().mapToInt(Integer::intValue).toArray();
  
why java collection needs to implement iterable, not iterator straightforward?

1.  Multiple Iterators: An iterable object can create multiple independent iterators, each with its own iteration state. This allows multiple iterations to occur simultaneously or at different positions within the iterable object.

2.  Lazy Evaluation: Iterators can implement lazy evaluation, where items are generated on-the-fly as they are requested, rather than pre-generating the entire sequence. This is especially useful when working with large or infinite sequences.

3.  Resource Management: Iterators can handle resource management efficiently. For example, an iterator might be used to read data from a file or a network stream, and it can be designed to open and close the necessary resources as needed, without requiring the entire sequence to be loaded into memory.
  
  what's the difference between ***arraylist*** class and **vector** *class*
  
  1.  Thread Safety: One of the main differences is thread safety. Vector is synchronized, which means it is inherently thread-safe. All of its methods are synchronized, ensuring that multiple threads can safely access and modify its elements. On the other hand, ArrayList is not synchronized by default. If you need thread safety with ArrayList, you would need to manually synchronize it using external synchronization mechanisms like Collections.synchronizedList().

  2.  Performance: Due to the synchronization overhead, ArrayList generally performs better in single-threaded scenarios compared to Vector. Since Vector ensures thread safety, it incurs additional synchronization costs that can impact performance.

  3.  Growth Rate: When an ArrayList or Vector needs to grow its internal array to accommodate more elements, they both increase the capacity of the array. However, ArrayList grows by a fixed percentage (typically 50% of the current capacity) using the grow() method, while Vector doubles its capacity using the ensureCapacity() method. This can result in different memory allocation patterns.

  4.  Legacy: Vector is considered part of the older Java Collections Framework and has been around since the early versions of Java. It is generally recommended to use ArrayList instead, unless you specifically require the synchronized behavior of Vector.

In summary, the main differences between ArrayList and Vector are their thread safety, performance characteristics, growth rate, and historical context. If you don't need explicit thread safety, ArrayList is typically preferred due to its better performance.
