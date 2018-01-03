package dancinglinks;

import java.util.LinkedList;

/**
 * Created by boscatov on 02.01.2018.
 */
public class Solution {
    byte depth;
    boolean isMultiple = false;
    public LinkedList<Node> solution;
    Solution(byte depth,LinkedList<Node> solution, boolean isMultiple){
        this.depth = depth;
        this.solution = new LinkedList<>(solution);
        this.isMultiple = isMultiple;
    }
}
