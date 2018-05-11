package sudoku.newgame;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import sudoku.newgame.dancinglinks.Algorithm;
import sudoku.newgame.dancinglinks.Structure;
import sudoku.newgame.datahelpers.DataConstants;
import sudoku.newgame.draw.DrawBoard;
import sudoku.newgame.sudoku.Board;

public class DemoDrawView extends View {
    boolean isDone = false;
    int[] game;
    Point size;
    volatile Board bd;
    public DrawBoard board = null;
    Canvas canvas;
    byte[] area = null;
    SharedPreferences sharedPreferences;
    int w;
    int h;
    int n;
    int theme = 0;
    int errorValue;
    Paint p;
    Context context;
    public DemoDrawView(Context context){
        super(context);
        this.context = context;
        n = 9;
        sharedPreferences = context.getSharedPreferences("Structure", Context.MODE_PRIVATE);
        size = new Point();
        p = new Paint();

    }
    public DemoDrawView(Context context, AttributeSet attrs) {
        super(context,attrs);
        this.context = context;
        n = 9;
        sharedPreferences = context.getSharedPreferences("Structure", Context.MODE_PRIVATE);
        size = new Point();
        p = new Paint();
    }

    public DemoDrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        n = 9;
        sharedPreferences = context.getSharedPreferences("Structure", Context.MODE_PRIVATE);
        size = new Point();
        p = new Paint();
    }

    public DemoDrawView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        n = 9;
        sharedPreferences = context.getSharedPreferences("Structure", Context.MODE_PRIVATE);
        size = new Point();
        p = new Paint();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        theme = sharedPreferences.getInt("Theme", 0);
        canvas.drawColor(DataConstants.getBackgroundColor(theme));
        p.setColor(DataConstants.getMainTextColor(theme));
        //boolean newGame = sharedPreferences.getBoolean("New game",false);

        if(board == null) {
            creation();
        }
        if(board.theme != theme) {
            board.theme = theme;
            board.refreshFillColor(theme);
        }
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if(board.board[0][0].length != (h - 2*10) / n) {
                board.changeLength((h - 2*10) / n);
            }

        }
        else if(board.board[0][0].length != (w-2*10) / n) {
            board.changeLength((w - 2 * 10) / n);
        }

        board.draw(canvas, p, theme);
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.w = w;
        this.h = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }
    void setValueInPosition(int x, int y, String value) {
        if(!bd.cells[x][y].isInput && !bd.cells[x][y].isCorrect()) {
            board.setValueInPosition(x,y, value);
        }
    }
    private void creation() {
        area = new byte[] {0,0,0,0,0,1,2,2,2,
                0,0,0,1,1,1,2,2,2,
                0,1,1,1,1,1,2,2,2,
                3,3,3,3,3,4,4,4,4,
                3,3,3,3,4,4,4,4,4,
                5,5,5,5,5,5,5,5,8,
                6,7,7,7,7,7,5,8,8,
                6,6,6,6,7,7,8,8,8,
                6,6,6,6,7,7,8,8,8};
        Algorithm algorithm = new Algorithm(new Structure((byte)n, area));
        int dif = 45;
        bd = null;
        isDone = false;
        Creation[] threads = new Creation[6];
        for(int i = 0; i < 6; ++i){
            threads[i] = new Creation(dif,area,i%4);
            threads[i].start();
        }
        outerloop:
        while(true){
            int t = 0;
            for(int i = 0; i < 6;++i){
                if(isDone){
                    Log.d("Threads",i + " thread");
                    //bd = threads[i].bd;
                    Log.d("Threads",bd + "");
//                    for(int z = 0; z < 4; ++z){
//                        threads[z].interrupt();
//                    }
                    break outerloop;
                }
                if(threads[i].algorithm.isFailed){
                    t++;
                }
                //Log.d("Thread loop","isDone="+threads[i].algorithm.isDone + " failed: " + t);
            }
            //Log.d("Thread loop","Failed: " + t);
            if(t == 6){
                Log.d("Loop threads","All threads failed");
                creation();
                return;
            }
        }
        bd = algorithm.create(dif-6, dif+9, area, n*n);
        if(bd == null) {
            Log.d("DrawView", "null board");
            bd = algorithm.create(dif-6, dif+9, area, n*n);
        }
        board = new DrawBoard(10,10,(w - 2 * 10)/n,bd.areas,bd,n);
        algorithm = new Algorithm(bd);
        game = algorithm.demoSolve();
    }
    class Creation extends Thread {
        Algorithm algorithm;
        Board board;
        int q;
        int difficulty;
        public Creation(int difficulty, byte[] area, int q){
            algorithm = new Algorithm(new Structure((byte) n, area));
            this.q = q;
            this.difficulty = difficulty;
        }
        @Override
        public void run(){
            board = algorithm.create(difficulty-3,difficulty+7,area,q*n*n);
            Log.d("Thread","End of thread " + algorithm.isFailed + " " +isDone);
            if(bd != null) {
                Log.d("Thread "," "+bd);
                return;
            }
            if(board != null) {
                bd = board;
                isDone = true;
            }
        }
    }
    //Set all pencil values
    public void basicPencil() {
        board.setBasicPencilValues();
        board.refreshAll();
        invalidate();
    }


    // Place first pen value
    public void firstPen() {
        int a = game[0];
        int row = (a / (n*n));
        int column = (a / n) % n;
        setValueInPosition(row, column, a%n+1+"");
        invalidate();
    }
    private void calculateErrorValue() {
        for(int a: game) {
            if(a == game[0]) {
                continue;
            }
            int row = (a / (n*n));
            int column = (a / n) % n;
            Log.d("Calc", a+"");
            for(int i = 0; i < n; ++i) {
                if(i != a%n && !bd.cells[row][column].possibleValues[i]) {
                    errorValue = a;
                    return;
                }
            }
        }
    }
    // Make an error
    public void errorPen() {
        calculateErrorValue();
        board.refreshAll();
        int a = errorValue;
        int row = (a / (n*n));
        int column = (a / n) % n;
        for(int i = 0; i < n; ++i) {
            if(i != a%n && !bd.cells[row][column].possibleValues[i]) {
                setValueInPosition(row, column, i+1+"");
                break;
            }
        }

        invalidate();
    }
    public void fixError() {
        board.refreshAll();
        int a = errorValue;
        int row = (a / (n*n));
        int column = (a / n) % n;
        setValueInPosition(row, column, a%n+1+"");
        invalidate();
    }
    public void gameStep(int i) {
        board.refreshAll();
        int a = game[i];
        int row = (a / (n*n));
        int column = (a / n) % n;
        setValueInPosition(row, column, a%n+1+"");
        invalidate();
    }
}
