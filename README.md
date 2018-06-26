# Sudoku on Non Standard Fields #

<table border="0">

<tr><td>![alt text](https://raw.githubusercontent.com/boscatov/sudoku-on-non-standard-fields/master/images/game.png)</td><td>![alt text](https://raw.githubusercontent.com/boscatov/sudoku-on-non-standard-fields/master/images/generator.png)</td></tr>

<tr><td>Game</td><td>Generator</td></tr>

<tr><td>![alt text](https://raw.githubusercontent.com/boscatov/sudoku-on-non-standard-fields/master/images/history.png)</td><td>![alt text](https://raw.githubusercontent.com/boscatov/sudoku-on-non-standard-fields/master/images/statistics.png)</td></tr>

<tr><td>History</td><td>Statistics</td></tr>

</table>

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
