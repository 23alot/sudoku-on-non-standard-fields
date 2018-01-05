package dancinglinks;

import sudoku.Board;
import sudoku.Cell;

/**
 * Created by boscatov on 02.12.2017.
 */
public class Structure {

    int minLength;
    HeadNode minNode;
    /**
     * Define borders of areas
     */
    private byte[] areas;
    /**
     * Reference to start node of structure
     */
    HeadNode root = new HeadNode();
    /**
     * Dimension of the structure
     */
    byte N;
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
     * @param N number of dimensions
     * @param areas board separation
     */
    public Structure(byte N, byte[] areas){
        this.N = N;
        width = 4*N*N;
        height = N*N*N;
        createStructureTitles(N);
        int row,column,area;

        HeadNode rowNode = (HeadNode)root.down;
        for(int i = 0; i < height; ++i){
            HeadNode colNode = (HeadNode)root.right;
            row = (i / (N*N));
            column = ((i/N)%N);
            area = areas[(row*N+column)];
            // Insert cell node
            colNode = insertNode(colNode,rowNode,0,
                    (N*row + column),(N*N));
            // Insert row node
            colNode = insertNode(colNode,rowNode,(N*N),
                    (N*N + i%N + N*row),(2*N*N));
            // Insert column node
            colNode = insertNode(colNode,rowNode,(2*N*N),
                    (2*N*N + i%N + N*column),(3*N*N));
            // Insert area node
            insertNode(colNode,rowNode,(3*N*N),
                    (3*N*N + i%N + N*area),(3*N*N + i%N + N*area));
            rowNode = (HeadNode)rowNode.down;
        }
        minNode = (HeadNode)root.right;
        minLength = N;
    }
    HeadNode insertNode(HeadNode currentColumn, HeadNode currentRow,int start,int value,int end){
        int z = start;
        for(; z < value; ++z)
            currentColumn = (HeadNode)currentColumn.right;

        insertAfter(currentColumn,currentRow);

        for(; z < end; ++z)
            currentColumn = (HeadNode)currentColumn.right;
        return currentColumn;
    }
    void insertAfter(HeadNode currentColumn, HeadNode currentRow){
        if(currentColumn.up == null)
            currentColumn.up = currentColumn;
        if(currentRow.left == null)
            currentRow.left = currentRow;

        Node newNode = new Node(currentColumn.up,currentColumn,currentRow.left,currentRow,currentColumn,currentRow);

        currentColumn.up.down = newNode;
        currentRow.left.right = newNode;
        currentColumn.up = newNode;
        currentRow.left = newNode;
    }
    void dump(){
        byte[][] arr = new byte[N*N*N][4*N*N];

        for(int i = 0; i < N*N*N; ++i)
            for(int z = 0; z < 4*N*N; ++z)
                arr[i][z] = 0;

        Node temp = root.right;
        while(temp!=root){
            if(!temp.upHead.deleted) {
                Node nd = temp.down;
                while(nd!=temp) {
                    arr[nd.leftHead.position][nd.upHead.position] = 1;
                    nd = nd.down;
                }
            }
            temp= temp.right;
        }
        System.out.println("dump start");
        for(int i = 0; i < N*N*N; ++i){
            for(int z = 0; z < 4*N*N; ++z)
                System.out.print(arr[i][z]);
            System.out.println();
        }
        System.out.println("dump end");
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
                while(currentRow != searchRow.left)
                    currentRow = (HeadNode)currentRow.down;

                insertAfter(currentColumn,currentRow);

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
            temp.right = new HeadNode(null, null, temp, root, curTemp.currentNumber,0);
            curTemp = (HeadNode) curTemp.right;
            temp = (HeadNode)temp.right;
        }
        curTemp = (HeadNode)cur.root.down;
        temp = root;
        for(int i = 0; i < height;++i) {
            temp.down = new HeadNode(temp, root, null, null, curTemp.currentNumber,i);
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
        int i;
        for(i = 0; i < width; ++i) {
            temp.right = new HeadNode(null, null, temp, root, N,i);
            temp = (HeadNode) temp.right;
        }
        temp = root;
        for(i = 0; i < height; ++i){
            temp.down = new HeadNode(temp, root, null, null, (byte)4,i);
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
