package sudoku.newgame;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import sudoku.newgame.dancinglinks.Algorithm;
import sudoku.newgame.dancinglinks.Structure;
import sudoku.newgame.datahelpers.DataConstants;
import sudoku.newgame.draw.Border;
import sudoku.newgame.draw.DrawBoard;
import sudoku.newgame.draw.DrawCell;
import sudoku.newgame.sudoku.Board;

/**
 * Created by sanya on 13.01.2018.
 */
public class DrawBoardGeneratorView extends View {
    public DrawCell[][] board;
    SharedPreferences sharedPreferences;
    public byte[] prpr;
    public Canvas canvas;
    int w;
    int h;
    int currentSize;
    boolean[] possibleCells;
    CellPosition[] currentHighlighted;
    int counter;
    int n;
    byte currentArea = 0;
    float startY = 40;
    float startX = 10;
    Paint p;
    int theme = 0;
    Context context;
    boolean[][] isVisited;
    public DrawBoardGeneratorView(Context context){
        super(context);
        this.context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        n = sharedPreferences.getInt("DimensionBoard",9);
        sharedPreferences = context.getSharedPreferences("Structure", Context.MODE_PRIVATE);
        theme = sharedPreferences.getInt("Theme", 0);
        p = new Paint();

    }
    public DrawBoardGeneratorView(Context context, AttributeSet attrs) {
        super(context,attrs);
        this.context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        n = sharedPreferences.getInt("DimensionBoard",9);
        sharedPreferences = context.getSharedPreferences("Structure", Context.MODE_PRIVATE);
        theme = sharedPreferences.getInt("Theme", 0);
        p = new Paint();
    }

    public DrawBoardGeneratorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        n = sharedPreferences.getInt("DimensionBoard",9);
        sharedPreferences = context.getSharedPreferences("Structure", Context.MODE_PRIVATE);
        theme = sharedPreferences.getInt("Theme", 0);
        p = new Paint();
    }

    public DrawBoardGeneratorView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        n = sharedPreferences.getInt("DimensionBoard",9);
        sharedPreferences = context.getSharedPreferences("Structure", Context.MODE_PRIVATE);
        theme = sharedPreferences.getInt("Theme", 0);
        p = new Paint();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(DataConstants.getBackgroundColor(theme));
        p.setColor(DataConstants.getMainTextColor(theme));
        if(board == null)
            creation();
        for(int i = 0; i < n; ++i)
            for(int z = 0; z < n; ++z)
                board[i][z].draw(p,canvas, theme);
        for(int i = 0; i < n; ++i)
            for(int z = 0; z < n; ++z)
                board[i][z].drawBoard(p, canvas, theme);
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.w = w;
        this.h = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }
    public void creation(){
        isVisited = new boolean[n][n];
        currentHighlighted = new CellPosition[n];
        currentSize = 0;
        possibleCells = new boolean[n*n];
        prpr = new byte[n*n];
        for(int i = 0; i < n*n;++i) {
            prpr[i] = -1;
            possibleCells[i] = false;
        }
        prpr = new byte[n*n];
        for(int i = 0; i < n*n;++i)
            prpr[i] = -1;
        float sizeY = startY;
        Point size = new Point();
        getDisplay().getSize(size);
        w = size.x;
        float length = (w-2*10)/n;
        board = new DrawCell[n][n];
        for(int i = 0; i < n; ++i) {
            float sizeX = startX;
            for (int z = 0; z < n; ++z) {
                board[i][z] = new DrawCell(new Border(z == 0,
                        z == (n - 1),
                        i == 0,
                        i == (n - 1)),
                        sizeX, sizeY, length, theme);
                sizeX += length;
            }
            sizeY += length;
        }
    }
    void declineArea(){
        currentArea--;
        for(int i = 0; i < board.length * board.length; ++i)
            if(prpr[i] == currentArea) {
                prpr[i] = -1;
                board[i/board.length][i%board.length].setFillColor(DataConstants.getFillColor(theme));
            }
        clearCurrentCells();
        refreshBorders();
        getRootView().findViewById(R.id.button50).setVisibility(INVISIBLE);
        if(currentArea == 0) {
            Log.d("Decline area", "Button invisible");
            getRootView().findViewById(R.id.button51).setVisibility(INVISIBLE);
        }
        invalidate();
    }
    private void clearCurrentCells() {
        for(int i = 0; i < currentSize; ++i) {
            currentHighlighted[i].drawCell.setFillColor(DataConstants.getFillColor(theme));
        }
        currentSize = 0;
    }
    void refreshBorders(){
        for(int i = 0; i < board.length; ++i) {
            for (int z = 0; z < board.length; ++z) {
                board[i][z].border.left = z == 0 || prpr[board.length*i+z-1] != prpr[board.length*i+z];
                board[i][z].border.right = z == (board.length - 1) || prpr[board.length*i+z+1]!=prpr[board.length*i+z];
                board[i][z].border.up = i == 0 || prpr[board.length*i+z-board.length]!=prpr[board.length*i+z];
                board[i][z].border.down = i == (board.length - 1) || prpr[board.length*i+z+board.length]!=prpr[board.length*i+z];
            }
        }
    }
    public void focusOnCellMove(float x, float y){
        x -= startX;
        y -= startY;
        int posx = (int)x/((w-2*10)/n);
        int posy = (int)y/((w-2*10)/n);
        if(currentSize == n) {
            getRootView().findViewById(R.id.button50).setBackgroundColor(DataConstants.getSameColor(theme));
            return;
        }
        if(posy < n && posx < n)
        {
            if(board[posy][posx].getFillColor() != DataConstants.getFillColor(theme))
                return;

            if(!(possibleCells[board.length*posy+posx] || (currentSize == 0 && board[posy][posx].getFillColor() == DataConstants.getFillColor(theme)))){
                TextView text = getRootView().findViewById(R.id.error_text);
                text.setText("Не в ту степь");
                text.setTextColor(DataConstants.getMainTextColor(theme));
                return;
            }
            currentHighlighted[currentSize++] = new CellPosition(posx,posy,board[posy][posx]);
            board[posy][posx].setFillColor(DataConstants.getTouchColor(theme));
            refreshPossibleCells();
        }
        if(currentSize == n) {
            if(!checkCell()) {
                currentSize--;
                board[posy][posx].setFillColor(DataConstants.getFillColor(theme));
                TextView text = getRootView().findViewById(R.id.error_text);
                text.setText("Такая себе поляна");
                text.setTextColor(DataConstants.getMainTextColor(theme));
            }
            else
                getRootView().findViewById(R.id.button50).setVisibility(VISIBLE);

        }
        invalidate();
    }

    public void focusOnCell(float x, float y){
        x -= startX;
        y -= startY;
        int posx = (int)x/((w-2*10)/n);
        int posy = (int)y/((w-2*10)/n);
        if(currentSize == n) {
            getRootView().findViewById(R.id.button50).setBackgroundColor(DataConstants.getSameColor(theme));
            return;
        }
        if(posy < n && posx < n)
        {
            if(board[posy][posx].getFillColor() == DataConstants.getAreaColor(theme))
                return;

            if(board[posy][posx].getFillColor() == DataConstants.getTouchColor(theme)) {
                deleteFromSequence(posx, posy);
                refreshPossibleCells();
                invalidate();
                return;
            }
            if(!(possibleCells[board.length*posy+posx] || (currentSize==0 && board[posy][posx].getFillColor() == DataConstants.getFillColor(theme)))){
                TextView text = getRootView().findViewById(R.id.error_text);
                text.setText("Не в ту степь");
                text.setTextColor(DataConstants.getMainTextColor(theme));
                return;
            }
            currentHighlighted[currentSize++] = new CellPosition(posx,posy,board[posy][posx]);
            board[posy][posx].setFillColor(DataConstants.getTouchColor(theme));
            refreshPossibleCells();
        }
        if(currentSize == n) {
            if(!checkCell()) {
                currentSize--;
                board[posy][posx].setFillColor(DataConstants.getFillColor(theme));
                TextView text = getRootView().findViewById(R.id.error_text);
                text.setText("Такая себе поляна");
                text.setTextColor(DataConstants.getMainTextColor(theme));
            }
            else
                getRootView().findViewById(R.id.button50).setVisibility(VISIBLE);

        }
        invalidate();
    }

    void refreshPossibleCells(){
        for(int i = 0; i < board.length*board.length; ++i)
            possibleCells[i] = false;
        for(int i = 0; i < currentSize; ++i){
            if(currentHighlighted[i].y-1>-1)
                possibleCells[board.length*(currentHighlighted[i].y-1)+currentHighlighted[i].x] = true;
            if(currentHighlighted[i].x-1>-1)
                possibleCells[board.length*(currentHighlighted[i].y)+currentHighlighted[i].x-1] = true;
            if(currentHighlighted[i].y+1<board.length)
                possibleCells[board.length*(currentHighlighted[i].y+1)+currentHighlighted[i].x] = true;
            if(currentHighlighted[i].x+1<board.length)
                possibleCells[board.length*(currentHighlighted[i].y)+currentHighlighted[i].x+1] = true;
        }
    }
    boolean saveArea(){
        for(int i = 0; i < board.length;++i){
            currentHighlighted[i].drawCell.setFillColor(DataConstants.getAreaColor(theme));
            prpr[currentHighlighted[i].y*board.length + currentHighlighted[i].x] = currentArea;
        }
        currentArea++;
        currentSize = 0;
        refreshPossibleCells();
        refreshBorders();
        if(currentArea > 0) {
            Log.d("Save area","Button visible");
            getRootView().findViewById(R.id.button51).setVisibility(VISIBLE);
        }
        invalidate();
        return currentArea == board.length;
    }

    void deleteFromSequence(int x, int y){
        board[y][x].setFillColor(DataConstants.getFillColor(theme));
        if(!checkDeleteFromSequence()){
            Log.d("Delete highlighted", "Chain gap");
            board[y][x].setFillColor(DataConstants.getTouchColor(theme));
            return;
        }
        for(int i = 0; i < currentSize; ++i)
            if(currentHighlighted[i].drawCell.getFillColor() == DataConstants.getFillColor(theme)) {
                currentHighlighted[i] = currentHighlighted[--currentSize];
                return;
            }
    }
    boolean checkDeleteFromSequence(){
        int x = currentHighlighted[0].x;
        int y = currentHighlighted[0].y;
        for(int i = 0; i < board.length; ++i)
            for(int z = 0; z < board.length; ++z)
                isVisited[i][z] = board[z][i].getFillColor() != DataConstants.getTouchColor(theme);
        for(int i = 1; i < currentSize; ++i)
            if(currentHighlighted[i].drawCell.getFillColor() == DataConstants.getTouchColor(theme)) {
                x = currentHighlighted[i].x;
                y = currentHighlighted[i].y;
                break;
            }

        counter = 1;
        isFullHighlightedArea(x, y);
        Log.d("Deleting from sequence",counter+"");
        return counter == currentSize-1;
    }
    void isFullHighlightedArea(int x, int y){
        isVisited[x][y] = true;
        if(x-1 >= 0 && !isVisited[x-1][y]){
            counter++;
            isFullHighlightedArea(x-1, y);
        }
        if(x+1 < board.length && !isVisited[x+1][y]){
            counter++;
            isFullHighlightedArea(x+1, y);
        }
        if(y-1 >= 0 && !isVisited[x][y-1]){
            counter++;
            isFullHighlightedArea(x, y-1);
        }
        if(y+1 < board.length && !isVisited[x][y+1]){
            counter++;
            isFullHighlightedArea(x, y+1);
        }
    }
    boolean checkCell(){
        for(int i = 0; i < board.length; ++i)
            for(int z = 0; z < board.length; ++z)
                isVisited[i][z] = board[z][i].getFillColor() == DataConstants.getTouchColor(theme);
        for(int i = 0; i < board.length; ++i)
            for(int z = 0; z < board.length; ++z){
                if(!isVisited[i][z]&&board[z][i].getFillColor() == DataConstants.getFillColor(theme)) {
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
        if(x-1 >= 0 && board[y][x-1].getFillColor() == DataConstants.getFillColor(theme) && !isVisited[x-1][y]){
            counter++;
            isFullArea(x-1, y);
        }
        if(x+1 < board.length && board[y][x+1].getFillColor() == DataConstants.getFillColor(theme) && !isVisited[x+1][y]){
            counter++;
            isFullArea(x+1, y);
        }
        if(y-1 >= 0 && board[y-1][x].getFillColor() == DataConstants.getFillColor(theme) && !isVisited[x][y-1]){
            counter++;
            isFullArea(x, y-1);
        }
        if(y+1 < board.length && board[y+1][x].getFillColor() == DataConstants.getFillColor(theme) && !isVisited[x][y+1]){
            counter++;
            isFullArea(x, y+1);
        }
    }
}
