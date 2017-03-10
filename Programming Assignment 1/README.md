# **Percolation** 

Write a program to estimate the value of the percolation threshold via **Monte Carlo** simulation.  

The following are two solutions to the **backwash** problem. 

## **Percolation with top virtual site**

It generates two **WeightedQuickUnionUF** objects to solve the **backwash** problem.  
One has two virtual sites, used for checking wheather the grid percolates.  
And the other has only one top site, checking wheather the site is full.   
This method doesn't work well in terms of memory usage.

```
ASSESSMENT SUMMARY

Compilation: PASSED
API: PASSED

Findbugs: PASSED
Checkstyle: FAILED (2 warnings)

Correctness: 26/26 tests passed
Memory: 5/8 tests passed
Timing: 9/9 tests passed

Aggregate score: 96.25%
[Compilation: 5%, API: 5%, Findbugs: 0%, Checkstyle: 0%, Correctness: 60%, Memory: 10%, Timing: 20%]
```    

## **Percolation without virtual sites**  

It generates only one **WeightedQuickUnionUF** object, without any virtual site.  
Uses some flag bits to indicate _BLOCKED_, _OPEN_, _TOPCONNECTED_, _BOTTOMCONNECTED_,  
and _PERCOLATED_ state. The grid will be _PERCOLATED_ when it is _TOPCONNECTED_ and  
_BOTTOMCONNECTED_.  
This method works well on memory use.
  
```
ASSESSMENT SUMMARY

Compilation: PASSED
API: PASSED

Findbugs: PASSED
Checkstyle: FAILED (8 warnings)

Correctness: 26/26 tests passed
Memory: 9/8 tests passed
Timing: 9/9 tests passed   

Aggregate score: 101.25%  
[Compilation: 5%, API: 5%, Findbugs: 0%, Checkstyle: 0%, Correctness: 60%, Memory: 10%, Timing: 20%]
```    
