package dancinglinks;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by boscatov on 02.12.2017.
 */
public class Algorithm {
    Structure structure;
    LinkedList<Node> solution = new LinkedList<>();
    LinkedList<Solution> result = new LinkedList<>();
    /**
     *
     * @param N dimension of Sudoku
     */
    Algorithm(byte N){

    }

    /**
     * Recursive function that finds solution
     */
    LinkedList<Node> solve(byte depth){
        byte curDepth = (byte)(depth + 1);
        LinkedList<Node> sol = null;
        ArrayList<Solution> solutions = new ArrayList<>();
        // TODO: check for multiple solutions
        if(solution.size() == structure.N)
            return solution; // Check copy send or not
        if(isBadEnd())
            return null;
        HeadNode deleted = structure.minNode;
        Node temp = deleted.down;
        for(byte i = 0; i < deleted.currentNumber; ++i){
            solution.add(temp);
            delete(temp);
            sol = solve(curDepth);
            // If solution has already exist then we have
            // multiple solutions and that is impossible for sudoku
            if(solutions.isEmpty())
                solutions.add(new Solution(curDepth,sol,false));
            else{
                solutions.get(0).isMultiple = true;
                solutions.add(new Solution(curDepth,sol,false));
            }

            cover(temp);
            solution.poll();
            temp = temp.down;
        }
        result.addAll(solutions);
        return sol;
    }

    /**
     * Return a solution back to the structure
     * @param nd node of a column to return
     */
    void cover(Node nd){
        Node temp = nd;
        HeadNode head = nd.upHead;
        for(byte i = 0; i < head.currentNumber; ++i){
            temp.left.right = temp;
            temp.right.left = temp;
            temp.leftHead.currentNumber++;
            temp = temp.down;
        }
        coverRow(nd);
    }

    /**
     * Return the node row back to the structure
     * @param nd back node
     */
    void coverRow(Node nd){
        HeadNode head = nd.leftHead;
        // Cover in head nodes
        head.up.down = head;
        head.down.up = head;
        Node temp = head.right;
        for(byte i = 0; i < head.currentNumber; ++i){
            temp.up.upHead.currentNumber++;
            if(temp.up.upHead.currentNumber < structure.minLength){
                structure.minLength = temp.up.upHead.currentNumber;
                structure.minNode = temp.up.upHead;
            }
            temp.up.down = temp;
            temp.down.up = temp;
            temp = temp.right;
        }
    }
    boolean isBadEnd(){
        if(solution.size() < structure.N)
            return false;
        HeadNode temp = (HeadNode)structure.root.right;
        for(int i = 0; i < structure.width - solution.size();++i){
            if(temp.currentNumber != 0)
                return false;
            temp = (HeadNode)temp.right;
        }
        return true;
    }
    /**
     * Deletes from structure each solution
     * @param nd node of a column to delete
     */
    void delete(Node nd){
        Node temp = nd;
        HeadNode head = nd.upHead;
        for(byte i = 0; i < head.currentNumber; ++i){
            temp.left.right = temp.right.left;
            temp.right.left = temp.left.right;
            temp.leftHead.currentNumber--;
            temp = temp.down;
        }
        deleteRow(nd);
    }

    /**
     * Deletes row which contains node
     * @param nd node to delete
     */
    private void deleteRow(Node nd){
        HeadNode head = nd.leftHead;
        // Deletion in head nodes
        head.up.down = head.down.up;
        head.down.up = head.up.down;
        Node temp = head.right;
        for(byte i = 0; i < head.currentNumber; ++i){
            temp.up.upHead.currentNumber--;
            if(temp.up.upHead.currentNumber < structure.minLength){
                structure.minLength = temp.up.upHead.currentNumber;
                structure.minNode = temp.up.upHead;
            }
            temp.up.down = temp.down.up;
            temp.down.up = temp.up.down;
            temp = temp.right;
        }
    }
}
