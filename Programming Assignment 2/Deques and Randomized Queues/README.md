# **Deques and Randomized Queues** 

Write a generic data type for a deque and a randomized queue. The goal of this assignment is to implement elementary data structures using arrays and linked lists, and to introduce you to generics and iterators.   

The implementation of **Deque** uses a double-linked list.  
The implementation of **RandomizedQueue** uses a resizing array.  
In **Permutation**, Create a **StringBuilder** object to read in a sequence of strings. Then convert the **StringBuilder** object to a string. Split the converted string into a string array and use a `permutation()` method in **StdRandom** to select randomized string indices. Finally, enqueue the selected randomized strings and traverse them. I've got a bonus here because of using only one **RandomizedQueue** object of maximum size at most k.

```
ASSESSMENT SUMMARY 

Compilation: PASSED (0 errors, 2 warnings) 
API: PASSED 

Findbugs: PASSED 
Checkstyle: PASSED 

Correctness: 43/43 tests passed 
Memory: 54/53 tests passed 
Timing: 110/110 tests passed 

Aggregate score: 100.19% 
[Compilation: 5%, API: 5%, Findbugs: 0%, Checkstyle: 0%, Correctness: 60%, Memory: 10%, Timing: 20%]
```