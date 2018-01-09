package sudoku;

/**
 * Created by boscatov on 06.12.2017.
 */
public class Board {
    public Cell[][] cells;
    public byte[] areas;
    public byte N;
    public byte emptyCells;
    public Board(byte N, byte[] areas){
        this.N = N;
        this.areas = areas;
        this.cells = new Cell[N][N];
        this.emptyCells = (byte)(N*N);
    }
    public Board(Byte N, byte[] areas, byte[] input){
        this(N,areas);
        for(int i = 0; i < input.length; ++i){
            cells[i/N][i%N].isInput = true;
            cells[i/N][i%N].value = input[i];
            emptyCells--;
        }
    }
}
