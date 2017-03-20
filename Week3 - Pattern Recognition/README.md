# **Deques and Randomized Queues** 

Write a program to recognize line patterns in a given set of points. 

**BruteCollinearPoints**  
It iterates through all combinations of 4 points and is not applicable to the case with more than 4 points.  
The method stops comparison immediately when finding three points are not collinear.  

**FastCollinearPoints**  

```
ASSESSMENT SUMMARY 

Compilation: PASSED 
API: PASSED 

Findbugs: FAILED (3 warnings) 
Checkstyle: PASSED 

Correctness: 41/41 tests passed 
Memory: 1/1 tests passed 
Timing: 39/41 tests passed 

Aggregate score: 99.02% 
[Compilation: 5%, API: 5%, Findbugs: 0%, Checkstyle: 0%, Correctness: 60%, Memory: 10%, Timing: 20%]
```