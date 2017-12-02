package dancinglinks;

/**
 * Created by boscatov on 02.12.2017.
 */
public class Structure {
    /**
     * Reference to start node of structure
     */
    private HeadNode root = new HeadNode();
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
     * creates base structure of classic Sudoku
     * @param N dimension of Sudoku
     */
    Structure(byte N){
        width = 4*N*N;
        height = N*N*N;
        // TODO: create base structure with height N^3 and width 4*N^2
    }

    /**
     * Constructor with number of dimensions
     * and areas
     * @param N
     * @param areas
     */
    Structure(byte N, byte[] areas){
        width = 4*N*N;
        height = N*N*N;

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
        Node temp;
        Node searchRow;
        // Current row head node of new structure
        HeadNode currentRow = (HeadNode)root.down;
        // Current column head node of new structure
        HeadNode currentColumn = (HeadNode)root.right;

        while(title != cur.root){
            temp = title.down;
            while(temp != title){
                // Find row position of node
                searchRow = new Node(temp);
                while(!(searchRow.left instanceof HeadNode))
                    searchRow.left = searchRow.left.left;
                // searchRow.left.position is row position
                while(currentRow.position != ((HeadNode) searchRow.left).position)
                    currentRow = (HeadNode)currentRow.down;
                if(currentColumn.top == null)
                    currentColumn.top = currentColumn;
                if(currentRow.top == null)
                    currentRow.top = currentRow;
                Node newNode = new Node(currentColumn.top,currentColumn,currentRow.top,currentRow);
                currentRow.top = newNode;
                currentColumn.top = newNode;
                temp = temp.down;
            }
            title = (HeadNode)title.right;
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
            temp = (HeadNode)temp.right;
        }
        curTemp = (HeadNode)cur.root.down;
        temp = root;
        while(curTemp != cur.root) {
            temp.down = new HeadNode(temp, root, null, null, curTemp.position, curTemp.currentNumber);
            curTemp = (HeadNode) curTemp.down;
            temp = (HeadNode)temp.right;
        }
        temp = null;
        curTemp = null;
    }

    /**
     * Create skeleton of structure
     * @param N dimension of Sudoku
     */
    void createStructureTitles(byte N){
        HeadNode temp = root;
        byte i;
        for(i = 0; i < width; ++i) {
            temp.right = new HeadNode(null, null, temp, root, i, N);
            temp = (HeadNode) temp.right;
        }
        for(i = 0; i < height; ++i){
            temp.down = new HeadNode(temp, root, null, null, i, N);
            temp = (HeadNode) temp.down;
        }
        temp = null;
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
