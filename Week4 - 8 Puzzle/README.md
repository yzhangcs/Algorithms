# **8 Puzzle** 

Write a program to solve the 8-puzzle problem (and its natural generalizations) using the A* search algorithm. 

**Broad**  
It represents a n-by-n grid with *n<sup>2</sup>-1* square blocks labeled 1 through *n<sup>2</sup>-1* and a blank square.  
As an optimization, the class uses a 1d array instead of a 2d array to save the blocks.

**Solver**  
It provides solution for the 8-puzzle problem (and its natural generalizations) using the A* search algorithm.  
There is one priority queue used here. The PQ performs insert and delMin operations according to the manhattan priority of each board.  

```
ASSESSMENT SUMMARY 

Compilation: PASSED 
API: PASSED 

Findbugs: PASSED  
Checkstyle: PASSED 

Correctness: 42/42 tests passed 
Memory: 11/11 tests passed 
Timing: 17/17 tests passed 

Aggregate score: 100.00% 
[Compilation: 5%, API: 5%, Findbugs: 0%, Checkstyle: 0%, Correctness: 60%, Memory: 10%, Timing: 20%]
```
