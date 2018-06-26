# Sudoku on Non Standard Fields #

First version

|||
|:-:|:-:|
![alt text](https://raw.githubusercontent.com/boscatov/sudoku-on-non-standard-fields/master/images/game.png "") | ![alt text](https://raw.githubusercontent.com/boscatov/sudoku-on-non-standard-fields/master/images/generator.png "")
Game | Generator
|![alt text](https://raw.githubusercontent.com/boscatov/sudoku-on-non-standard-fields/master/images/history.png "") | ![alt text](https://raw.githubusercontent.com/boscatov/sudoku-on-non-standard-fields/master/images/statistics.png "")
History | Statistics


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
