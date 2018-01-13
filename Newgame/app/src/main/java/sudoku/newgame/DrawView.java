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
import sudoku.newgame.draw.DrawBoard;
import sudoku.newgame.sudoku.Board;

/**
 * Created by sanya on 13.01.2018.
 */

public class DrawView extends SurfaceView implements SurfaceHolder.Callback {
    public DrawThread drawThread;
    int w;
    int h;
    Paint p;
    public DrawView(Context context) {
        super(context);
        p = new Paint();
        getHolder().addCallback(this);
    }
    public DrawView(Context context, AttributeSet attrs){
        super(context,attrs);
        System.out.println(1);

        p = new Paint();
        getHolder().addCallback(this);
    }
    public DrawView(Context context, AttributeSet attrs,int defStyleAttr){
        super(context,attrs,defStyleAttr);
        System.out.println(1);

        p = new Paint();
        getHolder().addCallback(this);
    }
    public DrawView(Context context, AttributeSet attrs,int defStyleAttr, int defStyleRes){
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
        drawThread = new DrawThread(getHolder());
        drawThread.setRunning(true);
        drawThread.start();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawThread = new DrawThread(getHolder());
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
            Board bd = algo.create(55, 81, prpr);
            DrawBoard board = new DrawBoard(10,40,p,canvas,(w-2*10)/9,prpr,bd);
        }
    }

}