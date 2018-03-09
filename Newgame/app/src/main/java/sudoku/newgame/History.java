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
    public void addEvent(int value, boolean isPen, boolean isEnter){
        String s = (isEnter?"Write ":"Delete ") + value + " by " + (isPen?"pen":"pencil") + ".";
        Log.d("addEvent", s);
        history.push(new Event(value,isPen,isEnter));
    }
    public Event getLastEvent() {
        return history.pop();
    }
}
