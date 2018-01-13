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
    int w;
    int h;
    Paint p;
    public DrawBoardGeneratorView(Context context) {
        super(context);
        p = new Paint();
        getHolder().addCallback(this);
    }
    public DrawBoardGeneratorView(Context context, AttributeSet attrs){
        super(context,attrs);
        System.out.println(1);

        p = new Paint();
        getHolder().addCallback(this);
    }
    public DrawBoardGeneratorView(Context context, AttributeSet attrs,int defStyleAttr){
        super(context,attrs,defStyleAttr);
        System.out.println(1);

        p = new Paint();
        getHolder().addCallback(this);
    }
    public DrawBoardGeneratorView(Context context, AttributeSet attrs,int defStyleAttr, int defStyleRes){
        super(context,attrs,defStyleAttr,defStyleRes);
        System.out.println(1);

        p = new Paint();
        getHolder().addCallback(this);
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
            Canvas canvas;

            while (running) {
                canvas = null;
                try {
                    canvas = surfaceHolder.lockCanvas(null);

                    if (canvas == null)
                        continue;
                    draw(canvas);
                    break;
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
            int n = 9;
            byte[] prpr = new byte[n*n];
            for(int i = 0; i < n*n;++i)
                prpr[i] = 0;
            float sizeY = 40;
            float length = (w-2*10)/n;
            DrawCell[][] board = new DrawCell[n][n];
            for(int i = 0; i < n; ++i) {
                float sizeX = 10;
                for (int z = 0; z < n; ++z) {
                    board[i][z] = new DrawCell(new Border(z == 0,
                            z == (n - 1),
                            i == 0,
                            i == (n - 1)),
                            sizeX, sizeY, length, p, canvas);
                    sizeX += length;
                }
                sizeY += length;
            }
            for(int i = 0; i < n; ++i)
                for(int z = 0; z < n; ++z)
                    board[i][z].drawBoard(p,canvas);

        }
    }
}
