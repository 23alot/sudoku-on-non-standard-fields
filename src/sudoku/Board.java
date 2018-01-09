package sudoku;

/**
 * Created by boscatov on 06.12.2017.
 */
public class Board {
    public Cell[][] cells;
    public byte[] areas;
    public byte N;
    public Board(byte N, byte[] areas){
        this.N = N;
        this.areas = areas;
        this.cells = new Cell[N][N];
    }
}
