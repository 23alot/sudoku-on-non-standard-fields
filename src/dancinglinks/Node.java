package dancinglinks;

/**
 * Created by boscatov on 02.12.2017.
 */
public class Node {
    Node up,down,left,right;
    Node(Node up, Node down, Node left, Node right){
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
    }
}

class HeadNode extends Node{
    /**
     * Value that contains number of nodes in line or in column
     */
    byte currentNumber;
    /**
     * Position of HeadNode
     */
    byte position;
    HeadNode(){
        super(null,null,null,null);
    }
}
