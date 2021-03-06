package sudoku.newgame.dancinglinks;

import android.util.Log;

import sudoku.newgame.History;
import sudoku.newgame.sudoku.Board;
import sudoku.newgame.sudoku.Cell;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Algorithm {
    public boolean isFailed = false;
    private long startTime;
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
        int d = structure.N+1;
        int t = 1;
        HeadNode temp = (HeadNode)structure.root.right;
        while(temp!=structure.root) {
            if(!temp.deleted){
                int k = length(temp);
                if(k == d){
                    t++;
                }
                if (k < d) {
                    t = 1;
                    d = k;
                }
            }
            temp = (HeadNode)temp.right;
        }

        return getRandomMinNode(d,t,0);
    }
    private HeadNode getRandomMinNode(int d, int t, int q) {
        HeadNode temp = (HeadNode)structure.root.right;
        int r = -1;
        t = q + rnd.nextInt(t);
        while(temp!=structure.root) {
            if(!temp.deleted){
                int k = length(temp);
                if(k == d){
                    r++;
                    if(r == t)
                        return temp;
                }
            }
            temp = (HeadNode)temp.right;
        }
        return null;
    }
    private HeadNode findMinValue(){
        int d = structure.N + 1;
        HeadNode temp = (HeadNode)structure.root.right;
        HeadNode result = null;
        while(temp!=structure.root) {
            if (!temp.deleted && length(temp) < d) {
                d = length(temp);
                result = temp;
            }
            temp = (HeadNode)temp.right;
        }
        return result;
    }
    private boolean isEnd(){
        HeadNode temp = (HeadNode)structure.root.right;
        while(temp!=structure.root) {

            if (!temp.deleted) {
                return false;
            }
            temp = (HeadNode) temp.right;
        }
        return true;
    }
    private void refresh(){
        solutionCounter = 0;
        moves = 0;
    }
    private void startFirst(int q){
        refresh();
        Node temp = getRandomMinNode(structure.N,structure.N*structure.N,q).down;
        delete(temp);
        moves++;
        solution[solutionCounter++] = temp.leftHead.position;
        findFirstSolution();
    }
    public void start(){
        refresh();
        solve();
    }
    private void findFirstSolution(){
        if(System.currentTimeMillis()-startTime > 1000) {
            return;
        }
        if(result!=null)
            return;
        if(isBadEnd()) {
            return;
        }
        if(isEnd()) {
            result = new Solution(moves,solution,false);
            return;
        }

        HeadNode deleted = findMinNode();
        Node temp = deleted.down;
        while(temp!=deleted){
            delete(temp);
            moves++;
            solution[solutionCounter++] = temp.leftHead.position;
            findFirstSolution();
            cover(temp);
            solutionCounter--;
            if(result!=null)
                return;
            if(System.currentTimeMillis()-startTime > 1000) {
                return;
            }
            temp = temp.down;
        }
    }
    private void solve(){
        if(result != null && result.isMultiple)
            return;
        if(isBadEnd())
            return;
        if(isEnd()) {
            //Log.d("solve","End");
            if(result == null)
                result = new Solution(moves, solution,false);
            else
                result.isMultiple = true;
            return;
        }

        HeadNode deleted = findMinValue();
        Node temp = deleted.down;
        while(temp!=deleted){
            delete(temp);
            moves++;
            solution[solutionCounter++] = temp.leftHead.position;
            solve();
            cover(temp);
            solutionCounter--;
            if(result!=null && result.isMultiple)
                return;
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
    private void delete(Node nd){
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
    private void cover(Node nd){
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
    public Board create(int difficultyl, int difficultyr, byte[] areas,int q){
        //Log.d("Algorithm","Algo started ");
        startTime = System.currentTimeMillis();
        startFirst(q);
        if(System.currentTimeMillis()-startTime > 1000) {
            isFailed = true;
            return null;
        }
        int[][] answer = toArray();
        int[] finalSolution = result.solution.clone();
        boolean[] isVisited= new boolean[structure.N*structure.N];
        for(int i = 0; i < structure.N*structure.N; ++i)
            isVisited[i] = false;
        int pos = rnd.nextInt(structure.N*structure.N);
        int t = 0;
        refresh();
        while (!(moves <= difficultyr && moves > difficultyl && !result.isMultiple)){
            //Log.d("Algorithm", moves+" moves");
            t++;
            result = null;
            while(isVisited[pos])
                pos = rnd.nextInt(structure.N*structure.N);

            isVisited[pos] = true;
            for(int i = 0; i < isVisited.length;++i)
                if(finalSolution[i]!=-1 && i != pos) {
                    delete(structure.getNode(finalSolution[i]));
                }
            start();

            for(int i = isVisited.length-1; i >= 0 ;--i)
                if(finalSolution[i]!=-1 && i != pos)
                    cover(structure.getNode(finalSolution[i]));

            if(result != null && !result.isMultiple && moves <= difficultyr) {
                finalSolution[pos] = -1;
            }
            if(countVisited(isVisited) == structure.N*structure.N) {
                Log.d("create", "All visited");
                break;
            }
        }
        Log.d("Algorithm","Final moves " + moves);
        Log.d("Sudokus solved", t+"");
        return new Board(structure.N,areas,finalSolution,answer);
    }

    private int countVisited(boolean[] isVisited){
        int t = 0;
        for(int i = 0; i < isVisited.length; ++i)
            if(isVisited[i])
                t++;
        return t;
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
    public int[] demoSolve() {
        start();

        return solution;
    }
}
