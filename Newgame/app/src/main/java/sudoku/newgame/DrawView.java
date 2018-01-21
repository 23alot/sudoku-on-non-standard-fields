package sudoku.newgame;

import android.content.Context;
import android.content.SharedPreferences;
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
import sudoku.newgame.draw.DrawBoard;
import sudoku.newgame.sudoku.Board;

/**
 * Created by sanya on 13.01.2018.
 */

public class DrawView extends View{
    Point size;
    Board bd;
    public DrawBoard board = null;
    Canvas canvas;
    byte[] area = null;
    SharedPreferences sharedPreferences;
    int w;
    int h;
    int n;
    Paint p;
    Context context;
    public DrawView(Context context){
        super(context);
        this.context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        n = sharedPreferences.getInt("Dimension",9);
        size = new Point();
        p = new Paint();

    }
    public DrawView(Context context, AttributeSet attrs) {
        super(context,attrs);
        this.context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        n = sharedPreferences.getInt("Dimension",9);
        size = new Point();
        p = new Paint();
    }

    public DrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        n = sharedPreferences.getInt("Dimension",9);
        size = new Point();
        p = new Paint();
    }

    public DrawView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        n = sharedPreferences.getInt("Dimension",9);
        size = new Point();
        p = new Paint();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        p.setColor(Color.BLACK);
        if(board==null)
            creation();
        getDisplay().getSize(size);
        if(size.x < size.y) {
            if(board.board[0][0].length!=(w-2*10) / n) {
                Log.d("onDraw", "Вызвался " + w);
                w = size.x;
                board.changeLength((w - 2 * 10) / n);
            }
        }
        else if(w != size.y){
            Log.d("onDraw","Вызвался");
            w = size.y;
            board.changeLength((w - 100) / n);
        }
        board.draw(canvas, p);
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
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String boardik = sharedPreferences.getString("Boardik",null);
        if(boardik != null){
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            board = gson.fromJson(boardik,DrawBoard.class);
            return;
        }
        boardik = sharedPreferences.getString("area",null);
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
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            area = gson.fromJson(boardik,byte[].class);
        }
        Point size = new Point();
        getDisplay().getSize(size);
        w = size.x;
        int difficulty = (20 + sharedPreferences.getInt("Difficulty",8))*n*n/81;
        difficulty = n*n - difficulty;
        Algorithm algo = new Algorithm(new Structure((byte) n, area));
        Log.d("Creation", difficulty+"");
        bd = algo.create(difficulty-3, difficulty+2, area);
        board = new DrawBoard(10,10,(w - 2 * 10)/n,bd.areas,bd,n);
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
        board.focusOnCell(x,y,w,color,highlightColor);
        invalidate();
    }
    void setValue(float x, float y,int w, String value){
        board.setValue(x,y,value,w);
        invalidate();
    }
    void setPencilValue(float x, float y, String value){
        board.setPencilValue(x,y,value);
        invalidate();
    }
}