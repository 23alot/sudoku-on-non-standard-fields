package sudoku.newgame.datahelpers;

import sudoku.newgame.draw.DrawBoard;

/**
 * Created by sanya on 03.03.2018.
 */

public class BoardBitmap {
    DrawBoard db;
    public BoardBitmap(byte[] structure, int n, float length) {
        float size = (length - n*10)/2/n;
        db = new DrawBoard(size, structure, n);
    }
}
