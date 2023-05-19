#Spring Semester OOP in JAVA
Contains old practice in 2022 Summer

The "diamond problem" is an ambiguity that arises when two classes B and C inherit from A, and class D inherits from both B and C. If there is a method in A that B and C have overridden, and D does not override it, then which version of the method does D inherit: that of B, or that of C?

List<Integer> res turn into int[]

int[] arr = res.stream().mapToInt(Integer::intValue).toArray();
  
why java collection needs to implement iterable, not iterator straightforward?

1.  Multiple Iterators: An iterable object can create multiple independent iterators, each with its own iteration state. This allows multiple iterations to occur simultaneously or at different positions within the iterable object.

2.  Lazy Evaluation: Iterators can implement lazy evaluation, where items are generated on-the-fly as they are requested, rather than pre-generating the entire sequence. This is especially useful when working with large or infinite sequences.

3.  Resource Management: Iterators can handle resource management efficiently. For example, an iterator might be used to read data from a file or a network stream, and it can be designed to open and close the necessary resources as needed, without requiring the entire sequence to be loaded into memory.
