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
import android.widget.Button;
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
    byte[] prpr;
    public Canvas canvas;
    int w;
    int h;
    int currentSize;
    CellPosition[] currentHighlighted;
    int counter;
    byte currentArea = 0;
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
        currentHighlighted = new CellPosition[n];
        currentSize = 0;
        prpr = new byte[n*n];
        for(int i = 0; i < n*n;++i)
            prpr[i] = -1;
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
    void refreshBorders(){
        for(int i = 0; i < board.length; ++i) {
            float sizeX = startX;
            for (int z = 0; z < board.length; ++z) {
                board[i][z].border.left = z == 0 || prpr[board.length*i+z-1] != prpr[board.length*i+z];
                board[i][z].border.right = z == (board.length - 1) || prpr[board.length*i+z+1]!=prpr[board.length*i+z];
                board[i][z].border.up = i == 0 || prpr[board.length*i+z-board.length]!=prpr[board.length*i+z];
                board[i][z].border.down = i == (board.length - 1) || prpr[board.length*i+z+board.length]!=prpr[board.length*i+z];
            }
        }
    }
    public void focusOnCell(float x, float y, int color){
        x -= startX;
        y -= startY;
        int n = 9;
        int posx = (int)x/((w-2*10)/n);
        int posy = (int)y/((w-2*10)/n);
        if(currentSize == n) {
            getRootView().findViewById(R.id.button50).setBackgroundColor(Color.RED);
            invalidate();
            return;
        }
        if(posy < n && posx < n)
        {
            if(board[posy][posx].getFillColor()==color) {
                deleteFromSequence(posx, posy);
                invalidate();
                return;
            }
            currentHighlighted[currentSize++] = new CellPosition(posx,posy,board[posy][posx]);
            board[posy][posx].setFillColor(color);
        }
        if(currentSize == n) {
            if(!checkCell())
                Toast.makeText(context, "Такая себе поляна", Toast.LENGTH_LONG).show();
            else
                getRootView().findViewById(R.id.button50).setVisibility(VISIBLE);

        }
        invalidate();
    }
    void saveArea(){
        for(int i = 0; i < board.length;++i){
            currentHighlighted[i].drawCell.setFillColor(Color.YELLOW);
            prpr[currentHighlighted[i].y*board.length + currentHighlighted[i].x] = currentArea;
        }
        currentArea++;
        currentSize = 0;
        refreshBorders();
        invalidate();
    }
    void deleteFromSequence(int x, int y){
        if(board[y][x].getFillColor()==Color.YELLOW)
            return;
        board[y][x].setFillColor(Color.WHITE);
        for(int i = 0; i <= currentSize; ++i)
            if(currentHighlighted[i].drawCell.getFillColor()==Color.WHITE) {
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
                if(!isVisited[i][z]&&board[z][i].getFillColor()==Color.WHITE) {
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
        if(x-1 >= 0 && board[y][x-1].getFillColor()==Color.WHITE && !isVisited[x-1][y]){
            counter++;
            isFullArea(x-1, y);
        }
        if(x+1 < board.length && board[y][x+1].getFillColor()==Color.WHITE && !isVisited[x+1][y]){
            counter++;
            isFullArea(x+1, y);
        }
        if(y-1 >= 0 && board[y-1][x].getFillColor()==Color.WHITE && !isVisited[x][y-1]){
            counter++;
            isFullArea(x, y-1);
        }
        if(y+1 < board.length && board[y+1][x].getFillColor()==Color.WHITE && !isVisited[x][y+1]){
            counter++;
            isFullArea(x, y+1);
        }
    }
}
