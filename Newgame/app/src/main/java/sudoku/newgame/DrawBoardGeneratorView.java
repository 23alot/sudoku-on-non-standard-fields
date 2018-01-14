package sudoku.newgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import sudoku.newgame.dancinglinks.Algorithm;
import sudoku.newgame.dancinglinks.Structure;
import sudoku.newgame.draw.Border;
import sudoku.newgame.draw.DrawBoard;
import sudoku.newgame.draw.DrawCell;
import sudoku.newgame.sudoku.Board;

/**
 * Created by sanya on 13.01.2018.
 */

public class DrawBoardGeneratorView extends SurfaceView implements SurfaceHolder.Callback {
    public DrawBoardGeneratorView.DrawThread drawThread;
    public DrawCell[][] board;
    public Canvas canvas;
    int w;
    int h;
    float startY = 40;
    float startX = 10;
    Paint p;
    public DrawBoardGeneratorView(Context context) {
        super(context);
        p = new Paint();

        getHolder().addCallback(this);
    }
    public DrawBoardGeneratorView(Context context, AttributeSet attrs){
        super(context,attrs);


        p = new Paint();
        getHolder().addCallback(this);
    }
    public DrawBoardGeneratorView(Context context, AttributeSet attrs,int defStyleAttr){
        super(context,attrs,defStyleAttr);


        p = new Paint();
        getHolder().addCallback(this);
    }
    public DrawBoardGeneratorView(Context context, AttributeSet attrs,int defStyleAttr, int defStyleRes){
        super(context,attrs,defStyleAttr,defStyleRes);


        p = new Paint();
        getHolder().addCallback(this);
    }
    public void creation(){
        int n = 9;
        byte[] prpr = new byte[n*n];
        for(int i = 0; i < n*n;++i)
            prpr[i] = 0;
        float sizeY = startY;
        float length = (w-2*10)/n;
        board = new DrawCell[n][n];
        for(int i = 0; i < n; ++i) {
            float sizeX = startX;
            for (int z = 0; z < n; ++z) {
                board[i][z] = new DrawCell(new Border(z == 0,
                        z == (n - 1),
                        i == 0,
                        i == (n - 1)),
                        sizeX, sizeY, length);
                sizeX += length;
            }
            sizeY += length;
        }
    }
    public void focusOnCell(float x, float y, int color){
        x -= startX;
        y -= startY;
        int n = 9;
        int posx = (int)x/((w-2*10)/n);
        int posy = (int)y/((w-2*10)/n);
        if(posy < n && posx < n)
            board[posy][posx].changeFillColor(color);
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
        drawThread = new DrawBoardGeneratorView.DrawThread(getHolder());
        drawThread.setRunning(true);
        drawThread.start();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawThread = new DrawBoardGeneratorView.DrawThread(getHolder());
        drawThread.setRunning(true);
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
        public SurfaceHolder surfaceHolder;

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
                    draw();
                } finally {
                    if (canvas != null) {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        }

        void draw(){
            canvas.drawColor(Color.WHITE);
            p.setColor(Color.BLACK);
            int n = 9;
            for(int i = 0; i < n; ++i)
                for(int z = 0; z < n; ++z)
                    board[i][z].draw(p,canvas);
            for(int i = 0; i < n; ++i)
                for(int z = 0; z < n; ++z)
                    board[i][z].drawBoard(p,canvas);
            board[1][1].fillCell(p,canvas);
            board[1][2].fillCell(p,canvas);
        }
    }
}
