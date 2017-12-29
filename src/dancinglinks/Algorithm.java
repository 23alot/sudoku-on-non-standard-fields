package dancinglinks;

/**
 * Created by boscatov on 02.12.2017.
 */
public class Algorithm {
    Structure structure;

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
            delete(temp);
            solve();
            cover(temp);
        }
    }
    void cover(Node nd){

    }
    /**
     * Deletes from structure each solution
     * @param nd head node of column to delete
     */
    void delete(Node nd){
        Node temp = nd.down;
        HeadNode head = nd.upHead;
        for(byte i = 0; i < head.currentNumber; ++i){
            deleteRow(temp);
            temp = temp.down;
        }
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
