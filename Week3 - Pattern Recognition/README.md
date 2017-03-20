# **Deques and Randomized Queues** 

Write a program to recognize line patterns in a given set of points. 

**BruteCollinearPoints**  
It iterates through all combinations of 4 points and is not applicable to the case with more than 4 points.  
As an optimization, the method stops comparison immediately when finding three points are not collinear.  
The order of growth of the running time of **BruteCollinearPoints** is $n^4$ in the worst case

**FastCollinearPoints**  
Different from **BruteCollinearPoints**, it sort the array before the for loop so taht each point is a origin point compared to the latter.  
For each origin point p, continue to sort its latter points according to the slopes they makes with p. Points that have equal slopes with respect to p are collinear, and sorting brings such points together.  
When a matching result is found, a sub-segment check is required. For efficiency, here uses the Binary Search.  
The order of growth of the running time of **FastCollinearPoints** is $n^2\log n$ in the worst case.

```
ASSESSMENT SUMMARY 

Compilation: PASSED 
API: PASSED 

Findbugs: FAILED (2 warnings)  
Checkstyle: PASSED 

Correctness: 41/41 tests passed 
Memory: 1/1 tests passed 
Timing: 41/41 tests passed 

Aggregate score: 100.00% 
[Compilation: 5%, API: 5%, Findbugs: 0%, Checkstyle: 0%, Correctness: 60%, Memory: 10%, Timing: 20%]
```