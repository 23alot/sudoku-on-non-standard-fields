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
