# Sudoku on Non Standard Fields #

There are three ways to work on:

+ The algorithm of Sudoku solving
+ The generation of single-solution field
+ The interface

## Game rules ##
Game *field* of NxN size divided on N *section*, bounded by bold lines.

**The goal:**

Write numbers from 1 to N in *cells*. Numbers are located randomly but with only condition: number can not repeat in every *column*, *line* and inside *section*.

**Explanations:** Size of the *section* is N.

I have chosen the dancing links algorithm by Donald Knuth. It is a fast and simple algorithm to solve Sudoku.

For the algorithm I have to choose *demands*. And here it is:

+ Every *line* has to contain **each** number in sequence 1..N
+ Every *column* has to contain **each** number in sequence 1..N
+ Every *section* has to contain **each** number in sequence 1..N
+ Every *cell* has to contain **unique** number in sequence 1..N



## Traveler's Diary ##

**14.11.2017 9:30** Woke up with a thought that I don't know how to generate game field and I must to think about this problem if I don't like to be murdered on the defense of course work (and I don't like : D). The game begins...

**20.11.2017 16:05** Must introduce a concept *cell's potential*. We have to write number in *cell* with highest *cell's potential*. Also, it dawned on me that I can parallelize solution to cells with the same *cell's potential*.

**02.12.2017** Create base structures for algorithm.

**06.12.2017 17:06** Already done most part of structure implementation. Have to code only structure-to-array method.

​		**17:19** Completed structure-to-array method. Ready to test!

**29.12.2017 14:00** Start working on the algorithmic part of the solution. 

**11.01.2018** Finish the algorithmic part and start thinking how to store and compare sudoku boards.
