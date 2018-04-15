package sudoku.newgame;

/**
 * Created by sanya on 03.02.2018.
 */

public class Event {
    boolean isPen;
    boolean isEnter;
    int value;
    int posx;
    int posy;
    public Event(int value, boolean isPen, boolean isEnter, int posx, int posy) {
        this.value = value;
        this.isPen = isPen;
        this.isEnter = isEnter;
        this.posx = posx;
        this.posy = posy;
    }
    public boolean isPen() {
        return isPen;
    }
    public boolean isEnter() {
        return isEnter;
    }
    public int getValue() {
        return value;
    }
    public int getX() {
        return posx;
    }
    public int getY() {
        return posy;
    }
}
