package dancinglinks;

/**
 * Created by boscatov on 02.12.2017.
 */
public class Algorithm {
    HeadNode root;
    byte minLength;
    HeadNode minNode;

    /**
     *
     * @param N dimension of Sudoku
     */
    Algorithm(byte N){
        minLength = N;
        minNode = (HeadNode)root.right;
    }

    /**
     * Deletes from structure each solution
     * @param nd
     */
    void delete(HeadNode nd){

    }

    /**
     * Deletes row which contains node
     * @param nd node to delete
     */
    void deleteRow(Node nd){
        HeadNode head = nd.leftHead;
        // Deletion in head nodes
        head.up.down = head.down.up;
        head.down.up = head.up.down;
        Node temp = head.right;
        for(byte i = 0; i < head.currentNumber; ++i){
            temp.up.upHead.currentNumber--;
            if(temp.up.upHead.currentNumber < minLength){
                minLength = temp.up.upHead.currentNumber;
                minNode = temp.up.upHead;
            }
            temp.up.down = temp.down.up;
            temp.down.up = temp.up.down;
            temp = temp.right;
        }
    }
}
