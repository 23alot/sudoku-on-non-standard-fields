package dancinglinks;

/**
 * Created by boscatov on 02.12.2017.
 */
public class Structure {
    /**
     * Reference to start node of structure
     */
    private HeadNode root;
    /**
     * Current width of structure
     */
    int width;
    /**
     * Current height of structure
     */
    int height;
    /**
     * Constructor with number of dimensions
     * creates base structure
     * @param N dimension of Sudoku
     */
    Structure(byte N){
        width = 4*N*N;
        height = N*N*N;
        // TODO: create base structure with height N^3 and width 4*N^2
    }

    /**
     * Copy constructor
     * @param cur structure to copy
     */
    private Structure(Structure cur){
        createStructureTitles(cur);
        // Current column head node
        HeadNode title = (HeadNode)cur.root.right;
        // Current node
        Node temp = cur.root.right.down;
        Node searchRow;
        // Current row head node of new structure
        HeadNode currentRow = (HeadNode)root.down;
        // Current column head node of new structure
        HeadNode currentColumn = (HeadNode)root.right;

        while(title != cur.root){
            while(temp != title){
                // Find row position of node
                searchRow = new Node(temp);
                while(!(searchRow.left instanceof HeadNode))
                    searchRow.left = searchRow.left.left;
                // searchRow.left.position is row position
                while(currentRow.position != ((HeadNode) searchRow.left).position)
                    currentRow = (HeadNode)currentRow.down;
                Node newNode = new Node(currentColumn.top,currentColumn,currentRow.top,currentRow);
                currentRow.top = newNode;
                currentColumn.top = newNode;
            }
        }
    }

    /**
     * Copy skeleton of structure
     * @param cur structure to copy
     */
    private void createStructureTitles(Structure cur){
        HeadNode temp = root;
        HeadNode curTemp = (HeadNode)cur.root.right;
        while(curTemp != cur.root) {
            temp.right = new HeadNode(null, null, temp, root, curTemp.position, curTemp.currentNumber);
            curTemp = (HeadNode) curTemp.right;
        }
        curTemp = (HeadNode)cur.root.down;
        temp = root;
        while(curTemp != cur.root) {
            temp.down = new HeadNode(temp, root, null, null, curTemp.position, curTemp.currentNumber);
            curTemp = (HeadNode) curTemp.down;
        }
        temp = null;
        curTemp = null;
    }

    /**
     * Makes a copy of current structure with new nodes
     * using copy constructor
     * @return copy of current structure
     */
    Structure copy(){
        return new Structure(this);
    }
}
