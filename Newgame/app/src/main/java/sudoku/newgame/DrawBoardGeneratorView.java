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
    int currentSize;
    DrawCell[] currentHighlighted;
    int counter;
    float startY = 40;
    float startX = 10;
    Paint p;
    Context context;
    boolean[][] isVisited;
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
        isVisited = new boolean[n][n];
        currentHighlighted = new DrawCell[n];
        currentSize = 0;
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
        {
            if(board[posy][posx].getFillColor()==color) {
                deleteFromSequence(posx, posy);
                invalidate();
                return;
            }
            currentHighlighted[currentSize++] = board[posy][posx];
            board[posy][posx].setFillColor(color);
        }
        if(currentSize == n) {
            currentSize = 0;
            if(!checkCell())
                Toast.makeText(context, "Такая себе поляна", Toast.LENGTH_LONG).show();
        }
        invalidate();
    }
    void deleteFromSequence(int x, int y){
        board[y][x].setFillColor(Color.WHITE);
        for(int i = 0; i <= currentSize; ++i)
            if(currentHighlighted[i].getFillColor()==Color.WHITE) {
                currentHighlighted[i] = currentHighlighted[currentSize--];
                return;
            }
    }
    boolean checkCell(){
        for(int i = 0; i < board.length; ++i)
            for(int z = 0; z < board.length; ++z)
                isVisited[i][z] = board[z][i].getFillColor()==Color.BLUE;
        for(int i = 0; i < board.length; ++i)
            for(int z = 0; z < board.length; ++z){
                if(!isVisited[i][z]) {
                    counter = 1;
                    isFullArea(i, z);
                    if(counter % board.length != 0)
                        return false;
                }
            }
        return true;
    }
    void isFullArea(int x, int y){
        isVisited[x][y] = true;
        if(x-1 >= 0 && board[y][x-1].getFillColor()!=Color.BLUE && !isVisited[x-1][y]){
            counter++;
            isFullArea(x-1, y);
        }
        if(x+1 < board.length && board[y][x+1].getFillColor()!=Color.BLUE && !isVisited[x+1][y]){
            counter++;
            isFullArea(x+1, y);
        }
        if(y-1 >= 0 && board[y-1][x].getFillColor()!=Color.BLUE && !isVisited[x][y-1]){
            counter++;
            isFullArea(x, y-1);
        }
        if(y+1 < board.length && board[y+1][x].getFillColor()!=Color.BLUE && !isVisited[x][y+1]){
            counter++;
            isFullArea(x, y+1);
        }
    }
}
