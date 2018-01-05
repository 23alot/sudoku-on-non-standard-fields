package dancinglinks;

import java.util.LinkedList;

/**
 * Created by boscatov on 02.01.2018.
 */
public class Solution {
    int moves;
    boolean isMultiple = false;
    int[] solution;

    Solution(int moves,int[] solution, boolean isMultiple){
        this.moves = moves;
        this.solution = solution.clone();
        this.isMultiple = isMultiple;
    }
}
