package sudoku.newgame;

import android.util.Log;
import java.util.Stack;

/**
 * Created by sanya on 03.02.2018.
 */

public class History {
    Stack<Event> history;
    public History(){
        history = new Stack<>();
    }
    public void addEvent(int value, boolean isPen, boolean isEnter, int posx, int posy){
        String s = (isEnter?"Write ":"Delete ") + value + " by " + (isPen?"pen":"pencil") + ".";
        Log.d("addEvent", s);
        history.push(new Event(value, isPen, isEnter, posx, posy));
    }
    public Event getLastEvent() {
        if(history.empty()) {
            return null;
        }
        else {
            return history.pop();
        }
    }
}
