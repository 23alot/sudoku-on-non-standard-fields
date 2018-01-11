package sudoku;

/**
 * Created by boscatov on 06.12.2017.
 */
public class Board {
    public Cell[][] cells;
    public byte[] areas;
    public byte N;
    public byte emptyCells;
    private Board(byte N, byte[] areas){
        this.N = N;
        this.areas = areas;
        this.cells = new Cell[N][N];
        for(int i = 0; i < N; ++i)
            for(int z = 0; z < N; ++z)
                cells[i][z] = new Cell(N);
        this.emptyCells = (byte)(N*N);
    }
    public Board(byte N, byte[] areas, int[] input,int[][] solution){
        this(N,areas);
        for(int a: input){
            int row = (a / (N * N));
            int column = ((a / N) % N);
            cells[row][column].value = (byte) (a % N + 1);
            if(a!=-1) {
                cells[row][column].isInput = true;
            }
        }
        for(int i = 0; i < N; ++i)
            for(int z = 0; z < N; ++z)
                cells[i][z].correctValue = (byte)solution[i][z];
    }
}
