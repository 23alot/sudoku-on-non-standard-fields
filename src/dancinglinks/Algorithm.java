package dancinglinks;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by boscatov on 02.12.2017.
 */
public class Algorithm {
    private int moves;
    private Structure structure;
    private int[] solution;
    private int solutionCounter;
    //LinkedList<Integer> solution = new LinkedList<>();
    public LinkedList<Solution> result = new LinkedList<>();
    /**
     * Main constructor
     * @param structure of sudoku
     */
    public Algorithm(Structure structure){
        this.structure = structure;
        this.solution = new int[structure.N*structure.N];
        this.solutionCounter = 0;
        moves = 0;
    }
    int length(HeadNode nd){
        int t = 0;
        Node temp = nd.down;
        while(temp!=nd){
            t++;
            temp = temp.down;
        }
        return t;
    }
    HeadNode findMinNode(){
        HeadNode min = structure.root;
        int d = structure.N + 1;
        HeadNode temp = (HeadNode)min.right;
        while(temp!=structure.root) {
            if (!temp.deleted && length(temp) < d) {
                min = temp;
                d = length(temp);
            }
            temp = (HeadNode)temp.right;
        }
        return min;
    }
    /**
     * Recursive function that finds solution
     */
    boolean isEnd(){
        HeadNode temp = (HeadNode)structure.root.right;
        int t = 0;
        while(temp!=structure.root) {

            if (!temp.deleted) {
                t++;
            }
            temp = (HeadNode) temp.right;
        }
        return t==0;
    }
    public void solve(){

        if(isBadEnd())
            return;
        if(isEnd()) {
            if(result.size() != 0) {
                result.get(0).isMultiple = true;
                result.add(new Solution(moves, solution, true));
            }
            return;
        }

        HeadNode deleted = findMinNode();
        Node temp = deleted.down;
        while(temp!=deleted){
            delete(temp);
            moves++;
            solution[solutionCounter++] = temp.leftHead.position;
            solve();
            cover(temp);
            solutionCounter--;
            temp = temp.down;
        }

    }

    boolean isBadEnd(){
        if(solutionCounter == structure.N*structure.N)
            return false;
        HeadNode temp = (HeadNode)structure.root.right;
        while(temp!=structure.root){
            if(!temp.deleted && temp.down == temp) {
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
        while(temp!=head){
            temp.upHead.deleted = true;
            deleteColumn(temp);
            temp = temp.right;
        }
        temp = head.right;
        while(temp!=head){
            deleteRows(temp);
            temp = temp.right;
        }

    }
    void cover(Node nd){
        HeadNode head = nd.leftHead;
        Node temp = head.left;
        while(temp!=head){
            coverRows(temp);
            temp = temp.left;
        }
        temp = head.left;
        while(temp!=head){
            temp.upHead.deleted = false;
            coverColumn(temp);
            temp = temp.left;
        }
    }
    void deleteRows(Node nd){
        HeadNode head = nd.upHead;
        Node temp = head.down;
        while(temp!=head){
            if(temp!=nd)
                deleteRow(temp);
            temp = temp.down;
        }
    }
    void coverRows(Node nd){
        HeadNode head = nd.upHead;
        Node temp = head.up;
        while(temp!=head){
            if(temp!=nd)
                coverRow(temp);
            temp = temp.up;
        }
    }
    void deleteColumn(Node nd){
        HeadNode head = nd.upHead;
        Node temp = head.down;
        while(temp!=head){
            if(temp.left.right == temp && nd.leftHead != temp.leftHead) {
                temp.left.right = temp.right;
                temp.right.left = temp.left;
            }
            temp = temp.down;
        }

    }
    void coverColumn(Node nd){
        HeadNode head = nd.upHead;
        Node temp = head.up;
        while(temp!=head){
            if(nd != temp) {
                temp.left.right = temp;
                temp.right.left = temp;
            }
            temp = temp.up;
        }
    }
    void deleteRow(Node nd){
        HeadNode head = nd.leftHead;
        Node temp = head.right;

        while(temp!=head){
            if(temp.up.down == temp) {
                temp.up.down = temp.down;
                temp.down.up = temp.up;
            }
            temp = temp.right;
        }
    }
    void coverRow(Node nd){
        HeadNode head = nd.leftHead;
        Node temp = head.left;
        while(temp!=head){
            if(temp.up.down != temp) {
                temp.up.down = temp;
                temp.down.up = temp;
            }
            temp = temp.left;
        }
    }
}
