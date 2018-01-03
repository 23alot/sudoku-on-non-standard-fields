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
    public LinkedList<Solution> result = new LinkedList<>();
    /**
     * Main constructor
     * @param structure of sudoku
     */
    public Algorithm(Structure structure){
        this.structure = structure;
    }
    HeadNode findMinNode(){
        HeadNode min = (HeadNode)structure.root.right;
        HeadNode temp = (HeadNode)min.right;
        for(int i = 1;i < structure.width - solution.size(); ++i)
            if(temp.currentNumber < min.currentNumber)
                min = temp;
        return min;
    }
    /**
     * Recursive function that finds solution
     */
    public void solve(byte depth){
        if(result.size()!=0)
            return;
        byte curDepth = (byte)(depth + 1);
        LinkedList<Node> sol = null;
        ArrayList<Solution> solutions = new ArrayList<>();
        // TODO: check for multiple solutions
        if(solution.size() == structure.N*structure.N) {
            result.add(new Solution(depth, solution, false)); // Check copy send or not
            return;
        }
        if(isBadEnd())
            return;
        HeadNode deleted = findMinNode();
        Node temp = deleted.down;
        for(byte i = 0; i < deleted.currentNumber; ++i){

            delete(temp);
            solution.add(temp);
            solve(curDepth);
            // If solution has already exist then we have
            // multiple solutions and that is impossible for sudoku
//            if(solutions.isEmpty())
//                solutions.add(new Solution(curDepth,sol,false));
//            else{
//                solutions.get(0).isMultiple = true;
//                solutions.add(new Solution(curDepth,sol,true));
//            }

            cover(temp);
            solution.poll();
            temp = temp.down;
        }
//        if(!solutions.isEmpty())
//            result.addAll(solutions);
//        return sol;
    }

    /**
     * Return a solution back to the structure
     * @param nd node of a column to return
     */
    void cover(Node nd){
        HeadNode head = nd.upHead;
        head.left.right = head;
        head.right.left = head;
        Node temp = head.down;
        for(byte i = 0; i < head.currentNumber; ++i){
            temp.left.right = temp;
            temp.right.left = temp;
            temp.leftHead.currentNumber++;
            temp = temp.down;
        }
        nd.upHead.currentNumber--;
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
        if(solution.size() == structure.N)
            return false;
        HeadNode temp = (HeadNode)structure.root.right;
        for(int i = 0; i < structure.width - solution.size();++i){
            if(temp.currentNumber == 0)
                return true;
            temp = (HeadNode)temp.right;
        }
        return false;
    }
    /**
     * Deletes from structure each solution
     * @param nd node of a column to delete
     */
    void delete(Node nd){
        HeadNode head = nd.upHead;
        head.left.right = head.right;
        head.right.left = head.left;
        Node temp = head.down;
        for(byte i = 0; i < head.currentNumber; ++i){
            temp.left.right = temp.right;
            temp.right.left = temp.left;
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
        head.up.down = head.down;
        head.down.up = head.up;
        Node temp = head.right;
        for(byte i = 0; i < head.currentNumber; ++i){
            if(temp.up.upHead == null)
                System.out.println(temp);
            temp.up.upHead.currentNumber--;
            if(temp.up.upHead.currentNumber < structure.minLength){
                structure.minLength = temp.up.upHead.currentNumber;
                structure.minNode = temp.up.upHead;
            }
            temp.up.down = temp.down;
            temp.down.up = temp.up;
            temp = temp.right;
        }
    }
}
