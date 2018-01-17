package sudoku.newgame;

import sudoku.newgame.draw.DrawCell;

/**
 * Created by sanya on 17.01.2018.
 */

public class CellPosition {
    public int x;
    public int y;
    public DrawCell drawCell;
    public CellPosition(int x, int y, DrawCell drawCell){
        this.x = x;
        this.y = y;
        this.drawCell = drawCell;
    }
}
