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

    /**
     *
     * @param N dimension of Sudoku
     */
    Algorithm(byte N){

    }

    /**
     * Recursive function that finds solution
     */
    void solve(){
        HeadNode deleted = structure.minNode;
        Node temp = deleted.down;
        for(byte i = 0; i < deleted.currentNumber; ++i){
            solution.add(temp);
            delete(temp);
            solve();
            cover(temp);
            solution.poll();
            temp = temp.down;
        }
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
