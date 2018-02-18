package sudoku.newgame;

/**
 * Created by sanya on 03.02.2018.
 */

public class Event {
    boolean isPen;
    boolean isEnter;
    int value;
    public Event(int value, boolean isPen, boolean isEnter) {
        this.value = value;
        this.isPen = isPen;
        this.isEnter = isEnter;
    }
}
