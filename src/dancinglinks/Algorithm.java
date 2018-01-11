package dancinglinks;

import sudoku.Board;
import sudoku.Cell;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by boscatov on 02.12.2017.
 */
public class Algorithm {
    private Random rnd = new Random();
    private int moves;
    private Structure structure;
    private int[] solution;
    private int solutionCounter;
    private Solution result = null;
    /**
     * Main constructor
     * @param structure of sudoku
     */
    public Algorithm(Structure structure){
        this.structure = structure;
        this.solution = new int[structure.N*structure.N];
        this.solutionCounter = 0;
        moves = 0;
    }
    public Algorithm(Board bd){
        this.structure = new Structure(bd.N,bd.areas);
        this.solution = new int[bd.emptyCells];
        this.solutionCounter = 0;
        moves = 0;
        for(int i = 0; i < bd.N; ++i)
            for(int z = 0; z < bd.N; ++z){
            // Could be null from getNode
                if(bd.cells[i][z].isInput)
                    delete(structure.getNode(bd.N*bd.N*i + bd.N*z + bd.cells[i][z].value - 1));
            }
    }
    public void changeStructure(Structure st){
        this.structure = st;
        //this.solution = new int[emptyCells];
        this.solutionCounter = 0;
        moves = 0;
    }
    private int length(HeadNode nd){
        int t = 0;
        Node temp = nd.down;
        while(temp!=nd){
            t++;
            temp = temp.down;
        }
        return t;
    }
    private HeadNode findMinNode(){
        int d = findMinValue();
        List<HeadNode> result = new ArrayList<>();
        HeadNode temp = (HeadNode)structure.root.right;
        while(temp!=structure.root) {
            if (!temp.deleted && length(temp) == d) {
                result.add(temp);
            }
            temp = (HeadNode)temp.right;
        }

        return result.get(rnd.nextInt(result.size()));
    }
    private int findMinValue(){
        int d = structure.N + 1;
        HeadNode temp = (HeadNode)structure.root.right;
        while(temp!=structure.root) {
            if (!temp.deleted && length(temp) < d) {
                d = length(temp);
            }
            temp = (HeadNode)temp.right;
        }
        return d;
    }
    private boolean isEnd(){
        HeadNode temp = (HeadNode)structure.root.right;
        int t = 0;
        while(temp!=structure.root) {

            if (!temp.deleted) {
                t++;
            }
            temp = (HeadNode) temp.right;
        }
        return t==0;
    }
    private void refresh(){
        solutionCounter = 0;
        moves = 0;
    }
    public void start(){
        refresh();
        solve();
    }
    private void solve(){
        if(result!=null && !result.isMultiple)
            return;
        if(isBadEnd())
            return;
        if(isEnd()) {
            if(result == null)
                result = new Solution(moves,solution,false);
            else
                result.isMultiple = true;
            return;
        }

        HeadNode deleted = findMinNode();
        Node temp = deleted.down;
        while(temp!=deleted){
            delete(temp);
            moves++;
            solution[solutionCounter++] = temp.leftHead.position;
            solve();
            cover(temp);
            solutionCounter--;
            temp = temp.down;
        }

    }

    private boolean isBadEnd(){
        if(solutionCounter == structure.N*structure.N)
            return false;
        HeadNode temp = (HeadNode)structure.root.right;
        while(temp!=structure.root){
            if(!temp.deleted && temp.down == temp) {
                return true;
            }
            temp = (HeadNode)temp.right;
        }
        return false;
    }
    /**
     * Deletes from structure each solution
     * @param nd node of a column to delete
     */
    public void delete(Node nd){
        HeadNode head = nd.leftHead;
        Node temp = head.right;
        while(temp!=head){
            temp.upHead.deleted = true;
            deleteColumn(temp);
            temp = temp.right;
        }
        temp = head.right;
        while(temp!=head){
            deleteRows(temp);
            temp = temp.right;
        }

    }
    public void cover(Node nd){
        HeadNode head = nd.leftHead;
        Node temp = head.left;
        while(temp!=head){
            coverRows(temp);
            temp = temp.left;
        }
        temp = head.left;
        while(temp!=head){
            temp.upHead.deleted = false;
            coverColumn(temp);
            temp = temp.left;
        }
    }
    private void deleteRows(Node nd){
        HeadNode head = nd.upHead;
        Node temp = head.down;
        while(temp!=head){
            if(temp!=nd)
                deleteRow(temp);
            temp = temp.down;
        }
    }
    private void coverRows(Node nd){
        HeadNode head = nd.upHead;
        Node temp = head.up;
        while(temp!=head){
            if(temp!=nd)
                coverRow(temp);
            temp = temp.up;
        }
    }
    private void deleteColumn(Node nd){
        HeadNode head = nd.upHead;
        Node temp = head.down;
        while(temp!=head){
            if(temp.left.right == temp && nd.leftHead != temp.leftHead) {
                temp.left.right = temp.right;
                temp.right.left = temp.left;
            }
            temp = temp.down;
        }

    }
    private void coverColumn(Node nd){
        HeadNode head = nd.upHead;
        Node temp = head.up;
        while(temp!=head){
            if(nd != temp) {
                temp.left.right = temp;
                temp.right.left = temp;
            }
            temp = temp.up;
        }
    }
    private void deleteRow(Node nd){
        HeadNode head = nd.leftHead;
        Node temp = head.right;

        while(temp!=head){
            if(temp.up.down == temp) {
                temp.up.down = temp.down;
                temp.down.up = temp.up;
            }
            temp = temp.right;
        }
    }
    private void coverRow(Node nd){
        HeadNode head = nd.leftHead;
        Node temp = head.left;
        while(temp!=head){
            if(temp.up.down != temp) {
                temp.up.down = temp;
                temp.down.up = temp;
            }
            temp = temp.left;
        }
    }
    public Board create(int difficultyl, int difficultyr, byte[] areas){
        start();
        int[][] answer = toArray();
        int[] finalSolution = result.solution.clone();
        boolean[] isVisited= new boolean[structure.N*structure.N];
        for(int i = 0; i < structure.N*structure.N; ++i)
            isVisited[i] = false;
        int pos = rnd.nextInt(structure.N*structure.N);
        while (!(moves <= difficultyr && moves > difficultyl)){
            result = null;
            while(isVisited[pos])
                pos = rnd.nextInt(structure.N*structure.N);

            isVisited[pos] = true;
            for(int i = 0; i < isVisited.length;++i)
                if(finalSolution[i]!=-1) {
                    delete(structure.getNode(finalSolution[i]));
                }
            start();

            for(int i = isVisited.length-1; i >= 0 ;--i)
                if(finalSolution[i]!=-1)
                    cover(structure.getNode(finalSolution[i]));

            if(!result.isMultiple) {
                finalSolution[pos] = -1;
            }

        }
        return new Board(structure.N,areas,finalSolution,answer);
    }
    private int[][] toArray(){
        int[][] answer = new int[structure.N][structure.N];
        for(int a: result.solution){
            int row = (a / (structure.N * structure.N));
            int column = ((a / structure.N) % structure.N);
            answer[row][column] = (byte) (a % structure.N + 1);
        }
        return answer;
    }
    public Solution getSolution(){
        return result;
    }
}
