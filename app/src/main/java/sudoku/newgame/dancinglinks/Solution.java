package sudoku.newgame.dancinglinks;

import java.util.LinkedList;

/**
 * Created by boscatov on 02.01.2018.
 */
public class Solution {
    private int moves;
    boolean isMultiple = false;
    public int[] solution;

    Solution(int moves,int[] solution, boolean isMultiple){
        this.moves = moves;
        this.solution = solution.clone();
        this.isMultiple = isMultiple;
    }
}
