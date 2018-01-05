package dancinglinks;

import java.util.LinkedList;

/**
 * Created by boscatov on 02.01.2018.
 */
public class Solution {
    byte depth;
    boolean isMultiple = false;
    int[] solution;

    Solution(byte depth,int[] solution, boolean isMultiple){
        this.depth = depth;
        this.solution = solution.clone();
        this.isMultiple = isMultiple;
    }
}
