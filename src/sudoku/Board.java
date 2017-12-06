package sudoku;

/**
 * Created by boscatov on 06.12.2017.
 */
public class Board {
    public Cell[][] cells;
    public Board(byte N){
        cells = new Cell[N][N];
    }
}
