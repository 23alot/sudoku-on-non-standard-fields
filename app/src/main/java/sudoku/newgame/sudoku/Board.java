package sudoku.newgame.sudoku;

import sudoku.newgame.sudoku.Cell;

/**
 * Created by boscatov on 06.12.2017.
 */
public class Board {
    public Cell[][] cells;
    public byte[] areas;
    public byte N;
    public byte emptyCells;
    private Board(byte N, byte[] areas) {
        this.N = N;
        this.areas = areas;
        this.cells = new Cell[N][N];
        for(int i = 0; i < N; ++i)
            for(int z = 0; z < N; ++z)
                cells[i][z] = new Cell(N);
        this.emptyCells = (byte)(N*N);
    }
    public Board(byte N, byte[] areas, int[] input,int[][] solution) {
        this(N,areas);
        for(int a: input){
            if(a!=-1) {
                int row = (a / (N * N));
                int column = ((a / N) % N);
                cells[row][column].value = (byte) (a % N + 1);
                cells[row][column].isInput = true;
            }
        }
        for(int i = 0; i < N; ++i)
            for(int z = 0; z < N; ++z)
                cells[i][z].correctValue = (byte)solution[i][z];
    }
    public void resetValues() {
        for(int i = 0; i < N; ++i)
            for(int z = 0; z < N; ++z)
                if(!cells[i][z].isInput) {
                    cells[i][z].value = -1;
                    for(int j = 0; j < N; ++j) {
                        cells[i][z].possibleValues[j] = false;
                    }
                }

    }
}
