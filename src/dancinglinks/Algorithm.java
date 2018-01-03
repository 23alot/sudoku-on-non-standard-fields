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
        while(temp!=structure.root) {
            if (temp.currentNumber < min.currentNumber)
                min = temp;
            temp = (HeadNode)temp.right;
        }
        return min;
    }
    /**
     * Recursive function that finds solution
     */
    public void solve(byte depth){
        if(result.size()!=0)
            return;
        byte curDepth = (byte)(depth + 1);
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

    boolean isBadEnd(){
        if(solution.size() == structure.N*structure.N)
            return false;
        HeadNode temp = (HeadNode)structure.root.right;
        while(temp!=structure.root){
            if(temp.currentNumber == 0) {
                return true;
            }
            temp = (HeadNode)temp.right;
        }
        return false;
    }
    /**
     * Deletes from structure each solution
     * @param nd node of a column to delete
     */
    void delete(Node nd){
        HeadNode head = nd.leftHead;
        Node temp = head.right;
        head.up.down = head.down;
        head.down.up = head.up;
        for(byte i = 0; i < head.currentNumber; ++i) {
            deleteColumn(temp);
            temp = temp.right;
        }
        temp = head.right;
        for(byte i = 0; i < head.currentNumber; ++i) {
            deleteRows(temp);
            temp = temp.right;
        }

    }
    void cover(Node nd){
        HeadNode head = nd.leftHead;
        Node temp = head.right;
        for(byte i = 0; i < head.currentNumber; ++i) {
            coverRows(temp);
            temp = temp.right;
        }
        temp = head.right;
        for(byte i = 0; i < head.currentNumber; ++i) {
            coverColumn(temp);
            temp = temp.right;
        }
    }
    void deleteRows(Node nd){
        HeadNode head = nd.upHead;
        Node temp = head.down;
        for(byte i = 0; i < head.currentNumber; ++i){
            if(temp.leftHead != nd.leftHead && !(temp instanceof HeadNode))
                deleteRow(temp);
            temp = temp.down;
        }
    }
    void coverRows(Node nd){
        HeadNode head = nd.upHead;
        Node temp = head.down;

        for(byte i = 0; i < head.currentNumber; ++i){
            if(nd.leftHead!=temp.leftHead && !(temp instanceof HeadNode))
                coverRow(temp);
            temp = temp.down;
        }
    }
    void deleteColumn(Node nd){
        HeadNode head = nd.upHead;
        head.left.right = head.right;
        head.right.left = head.left;
        Node temp = head.down;

        for(byte i = 0; i < head.currentNumber; ++i){
            if(temp.left.right == temp && nd.leftHead != temp.leftHead) {
                temp.left.right = temp.right;
                temp.right.left = temp.left;
                temp.leftHead.currentNumber--;
            }
            temp = temp.down;
        }

    }
    void coverColumn(Node nd){

        HeadNode head = nd.upHead;
        head.left.right = head;
        head.right.left = head;
        Node temp = head.down;
        for(byte i = 0; i < head.currentNumber; ++i){
            if(temp.left.right == temp && nd.leftHead != temp.leftHead) {
                temp.left.right = temp;
                temp.right.left = temp;
                temp.leftHead.currentNumber++;
            }
            temp = temp.down;
        }
    }
    void deleteRow(Node nd){
        HeadNode head = nd.leftHead;
        head.up.down = head.down;
        head.down.up = head.up;
        Node temp = head.right;
        for(byte i = 0; i < head.currentNumber; ++i){
            if(temp.up.down == temp) {
                temp.up.down = temp.down;
                temp.down.up = temp.up;
                temp.upHead.currentNumber--;
            }
            temp = temp.right;
        }
    }
    void coverRow(Node nd){
        HeadNode head = nd.leftHead;
        head.up.down = head;
        head.down.up = head;
        Node temp = head.right;
        for(byte i = 0; i < head.currentNumber; ++i){
            if(temp.up.down != temp) {
                temp.up.down = temp;
                temp.down.up = temp;
                temp.upHead.currentNumber++;
            }
            temp = temp.right;
        }
    }
}
