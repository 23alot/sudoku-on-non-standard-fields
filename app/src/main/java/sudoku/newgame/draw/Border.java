package sudoku.newgame.draw;

/**
 * Created by sanya on 13.01.2018.
 */

public class Border {
    public boolean left;
    public boolean right;
    public boolean up;
    public boolean down;
    public Border(boolean left, boolean right, boolean up, boolean down){
        this.left = left;
        this.right = right;
        this.down = down;
        this.up = up;
    }
}
