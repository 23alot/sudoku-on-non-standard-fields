package dancinglinks;

/**
 * Created by boscatov on 02.12.2017.
 */
public class Node {
    Node up,down,left,right;
    HeadNode upHead;
    /**
     * Initialization constructor
     * @param up Reference to upper node
     * @param down Reference to down node
     * @param left Reference to left node
     * @param right Reference to right node
     */
    Node(Node up, Node down, Node left, Node right, HeadNode upHead){
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
        this.upHead = upHead;
    }

    /**
     * Default constructor
     */
    Node(){}

    /**
     * Copy constructor
     * @param other node to copy
     */
    Node(Node other){
        this.up = other.up;
        this.down = other.down;
        this.left = other.left;
        this.right = other.right;
        this.upHead = other.upHead;
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
    /**
     * Last inserted node
     */
    Node top;
    HeadNode(){
        super(null,null,null,null,null);
    }
    HeadNode(HeadNode up, HeadNode down, HeadNode left, HeadNode right, byte position, byte currentNumber){
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
        this.position = position;
        this.currentNumber = currentNumber;
        this.top = this;
    }
}
