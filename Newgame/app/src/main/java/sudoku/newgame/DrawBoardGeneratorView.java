package sudoku.newgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import sudoku.newgame.dancinglinks.Algorithm;
import sudoku.newgame.dancinglinks.Structure;
import sudoku.newgame.draw.Border;
import sudoku.newgame.draw.DrawBoard;
import sudoku.newgame.draw.DrawCell;
import sudoku.newgame.sudoku.Board;

/**
 * Created by sanya on 13.01.2018.
 */
public class DrawBoardGeneratorView extends View {
    public DrawCell[][] board;
    public Canvas canvas;
    int w;
    int h;
    float startY = 40;
    float startX = 10;
    Paint p;
    Context context;
    public DrawBoardGeneratorView(Context context){
        super(context);
        this.context = context;
        p = new Paint();

    }
    public DrawBoardGeneratorView(Context context, AttributeSet attrs) {
        super(context,attrs);
        this.context = context;
        p = new Paint();
    }

    public DrawBoardGeneratorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        p = new Paint();
    }

    public DrawBoardGeneratorView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        Display display = getDisplay();
        this.context = context;
        p = new Paint();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        p.setColor(Color.BLACK);
        if(board == null)
            creation();
        int n = 9;
        for(int i = 0; i < n; ++i)
            for(int z = 0; z < n; ++z)
                board[i][z].draw(p,canvas);
        for(int i = 0; i < n; ++i)
            for(int z = 0; z < n; ++z)
                board[i][z].drawBoard(p,canvas);
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.w = w;
        this.h = h;
        super.onSizeChanged(w, h, oldw, oldh);
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
            board[posy][posx].setFillColor(color);
        invalidate();
    }
}
