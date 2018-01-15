package sudoku.newgame;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

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

public class DrawView extends SurfaceView implements SurfaceHolder.Callback {
    public DrawThread drawThread;
    public DrawBoard board = null;
    Canvas canvas;
    int w;
    int h;
    Paint p;
    Context context;
    public DrawView(Context context) {
        super(context);
        this.context = context;
        p = new Paint();
        getHolder().addCallback(this);
    }
    public DrawView(Context context, AttributeSet attrs){
        super(context,attrs);
        this.context = context;
        p = new Paint();
        getHolder().addCallback(this);
    }
    public DrawView(Context context, AttributeSet attrs,int defStyleAttr){
        super(context,attrs,defStyleAttr);
        System.out.println(1);
        this.context = context;
        p = new Paint();
        getHolder().addCallback(this);
    }
    public DrawView(Context context, AttributeSet attrs,int defStyleAttr, int defStyleRes){
        super(context,attrs,defStyleAttr,defStyleRes);
        System.out.println(1);
        this.context = context;
        p = new Paint();
        getHolder().addCallback(this);
    }
    String drawBoardtoJSON(DrawBoard drawBoard){
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            return gson.toJson(board);
    }
    public void changeBoard(String s){
        try{
            JSONObject jsonBoard = new JSONObject(s);
            board = (DrawBoard)jsonBoard.get("Board");
        }catch (JSONException e){}
    }
    public void creation(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String boardik = sharedPreferences.getString("Boardik",null);
        if(boardik!=null){
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            board = gson.fromJson(boardik,DrawBoard.class);
            return;
        }
        //                byte[] prpr = {0,0,0,0,0,0,0,0,0,
//                1,1,1,1,1,1,1,1,1,
//                2,2,2,2,2,2,2,2,2,
//                3,3,3,3,3,3,3,3,3,
//                4,4,4,4,4,4,4,4,4,
//                5,5,5,5,5,5,5,5,5,
//                6,6,6,6,6,6,6,6,6,
//                7,7,7,7,7,7,7,7,7,
//                8,8,8,8,8,8,8,8,8};
        byte[] prpr = {0,0,0,1,1,1,2,2,2,
                0,0,0,1,1,1,2,2,2,
                0,0,0,1,1,1,2,2,2,
                3,3,3,4,4,4,5,5,5,
                3,3,3,4,4,4,5,5,5,
                3,3,3,4,4,4,5,5,5,
                6,6,6,7,7,7,8,8,8,
                6,6,6,7,7,7,8,8,8,
                6,6,6,7,7,7,8,8,8};
        int n = 9;
        Algorithm algo = new Algorithm(new Structure((byte) n, prpr));
        Board bd = algo.create(10, 20, prpr);
        board = new DrawBoard(10,40,(w-2*10)/9,bd.areas,bd);
    }
    boolean checkSudoku(){
        int n = 9;
        for(int i = 0; i < n; ++i)
            for(int j = 0; j < n; ++j) {
                if(!board.bd.cells[i][j].isCorrect())
                    return false;
            }
        return true;
    }
    void refreshAll(){
        board.refreshAll();
    }
    void focusOnCell(float x, float y, int color, int highlightColor){
        board.focusOnCell(x,y,w,color,highlightColor);
    }
    void setValue(float x, float y, String value){
        board.setValue(x,y,value,w);
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.w = w;
        this.h = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
//        drawThread = new DrawThread(getHolder());
//        drawThread.setRunning(true);
//        drawThread.start();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawThread = new DrawThread(getHolder());
        drawThread.setRunning(true);
        if(board == null)
            creation();
        drawThread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        boolean retry = true;
        drawThread.setRunning(false);
        while (retry) {
            try {
                drawThread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }

    public class DrawThread extends Thread {

        private boolean running = false;
        private SurfaceHolder surfaceHolder;

        public DrawThread(SurfaceHolder surfaceHolder) {
            this.surfaceHolder = surfaceHolder;
        }

        public void setRunning(boolean running) {
            this.running = running;
        }

        @Override
        public void run() {
            while (running) {
                canvas = null;
                try {
                    canvas = surfaceHolder.lockCanvas(null);

                    if (canvas == null)
                        continue;
                    draw(canvas);
                } finally {
                    if (canvas != null) {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        }
        void draw(Canvas canvas){
            canvas.drawColor(Color.WHITE);
            p.setColor(Color.BLACK);
            board.draw(canvas,p);
        }
    }

}