package sudoku.newgame;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.FileAlreadyExistsException;

import sudoku.newgame.dancinglinks.Algorithm;
import sudoku.newgame.dancinglinks.Structure;
import sudoku.newgame.datahelpers.DataConstants;
import sudoku.newgame.datahelpers.UserTime;
import sudoku.newgame.draw.DrawBoard;
import sudoku.newgame.sudoku.Board;

/**
 * Created by sanya on 13.01.2018.
 */

public class DrawView extends View{
    boolean isDone = false;
    Point size;
    volatile Board bd;
    public DrawBoard board = null;
    Canvas canvas;
    byte[] area = null;
    SharedPreferences sharedPreferences;
    int w;
    int h;
    int n;
    Paint p;
    Context context;
    int theme = 0;
    public DrawView(Context context){
        super(context);
        this.context = context;
        sharedPreferences = context.getSharedPreferences("Structure", Context.MODE_PRIVATE);
        theme = sharedPreferences.getInt("Theme", 0);
        n = sharedPreferences.getInt("Dimension",9);
        size = new Point();
        p = new Paint();

    }
    public DrawView(Context context, AttributeSet attrs) {
        super(context,attrs);
        this.context = context;
        sharedPreferences = context.getSharedPreferences("Structure", Context.MODE_PRIVATE);
        theme = sharedPreferences.getInt("Theme", 0);
        n = sharedPreferences.getInt("Dimension",9);
        size = new Point();
        p = new Paint();
    }

    public DrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        sharedPreferences = context.getSharedPreferences("Structure", Context.MODE_PRIVATE);
        theme = sharedPreferences.getInt("Theme", 0);
        n = sharedPreferences.getInt("Dimension",9);
        size = new Point();
        p = new Paint();
    }

    public DrawView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        sharedPreferences = context.getSharedPreferences("Structure", Context.MODE_PRIVATE);
        theme = sharedPreferences.getInt("Theme", 0);
        n = sharedPreferences.getInt("Dimension",9);
        size = new Point();
        p = new Paint();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        Log.d("onDraw","draw begin "+w);
        Log.d("Theme",theme+"");
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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w = getMeasuredWidth();
        int h = getMeasuredHeight();
        if(h < w) {
            this.w = h;
            this.h = w;
        }
        Log.d("onMeasure","w="+w+" h="+h);
        setMeasuredDimension(w, h);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.w = w;
        this.h = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }
    String drawBoardtoJSON(DrawBoard drawBoard){
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            return gson.toJson(board);
    }
    public void creation(){
        String boardik = sharedPreferences.getString("Boardik",null);
        if(boardik != null){
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            board = gson.fromJson(boardik,DrawBoard.class);
            return;
        }
        boardik = sharedPreferences.getString("area", null);
        if(boardik == null) {
            area = new byte[] {0,0,0,1,1,1,2,2,2,
                0,0,0,1,1,1,2,2,2,
                0,0,0,1,1,1,2,2,2,
                3,3,3,4,4,4,5,5,5,
                3,3,3,4,4,4,5,5,5,
                3,3,3,4,4,4,5,5,5,
                6,6,6,7,7,7,8,8,8,
                6,6,6,7,7,7,8,8,8,
                6,6,6,7,7,7,8,8,8};
        }
        else {
            Log.d("creation","area from preferences");
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            area = gson.fromJson(boardik,byte[].class);
        }
        long start1, start2, end2, end1;
        start1 = System.nanoTime();
        int difficulty = (20 + sharedPreferences.getInt("Difficulty",8))*n*n/81;
        difficulty = n*n - difficulty;
        bd = null;
        isDone = false;
        Log.d("Creation", "Difficulty " + difficulty);
        Creation[] threads = new Creation[6];
        for(int i = 0; i < 6; ++i){
            threads[i] = new Creation(difficulty,area,i%4);
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
            }
        }
        end1 = System.nanoTime();
        Log.d("Creation",bd+"");
        board = new DrawBoard(10,10,(w - 2 * 10)/n, bd.areas, bd, n);
        int dir = sharedPreferences.getInt("Difficulty",8);
        Log.d("TestBD", dir+"");
        dir = (dir - 3) / 5;
        Log.d("TestBD2", dir+"");
        String difficultys;
        switch(2-dir) {
            case 0:
                difficultys = "easy";
                break;
            case 1:
                difficultys = "medium";
                break;
            case 2:
                difficultys = "hard";
                break;
            default: difficultys = "error";
        }
        UserTime.addCreationTime(n+"", difficultys, end1-start1);
        Toast.makeText(context,end1-start1+"",Toast.LENGTH_SHORT).show();
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
    boolean checkSudoku(){
        for(int i = 0; i < n; ++i)
            for(int j = 0; j < n; ++j) {
                if(!board.bd.cells[i][j].isCorrect())
                    return false;
            }
        return true;
    }
    void refreshAll(){
        board.refreshAll();
        invalidate();
    }
    void focusOnCell(float x, float y,int w, int color, int highlightColor){
        board.focusOnCell(x,y);
        invalidate();
    }
    void hint(float x, float y) {
        board.hint(x, y);
        invalidate();
    }
    void setValue(float x, float y,int w, String value){
        board.setValue(x,y,value,w);
        invalidate();
    }
    void undo() {
        board.undo();
        invalidate();
    }
    void clearPencil(float x, float y, int w){
        board.clearPencil(x,y);
    }
    void setPencilValue(float x, float y, String value){
        board.setPencilValue(x,y,value);
        invalidate();
    }
}