package sudoku.newgame.dancinglinks;

/**
 * Created by boscatov on 02.12.2017.
 */
public class Node {
    Node up,down,left,right;
    public HeadNode upHead,leftHead;
    /**
     * Initialization constructor
     * @param up Reference to upper node
     * @param down Reference to down node
     * @param left Reference to left node
     * @param right Reference to right node
     */
    Node(Node up, Node down, Node left, Node right, HeadNode upHead,HeadNode leftHead){
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
        this.upHead = upHead;
        this.leftHead = leftHead;
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

    boolean deleted = false;
    int position;
    HeadNode(){}
    HeadNode(HeadNode up, HeadNode down, HeadNode left, HeadNode right, byte currentNumber,int position){
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
        this.currentNumber = currentNumber;
        this.upHead = this;
        this.leftHead = this;
        this.position = position;
    }
}
