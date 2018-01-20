package sudoku.newgame.draw;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import org.json.JSONException;
import org.json.JSONObject;

import sudoku.newgame.sudoku.Board;

/**
 * Created by sanya on 13.01.2018.
 */

public class DrawBoard {
    public Board bd;
    public DrawCell[][] board;
    public float startX;
    public float startY;
    public byte[] structure;
    Paint p;
    int n;
    public DrawBoard(float startX, float startY, float length, byte[] structure, Board bd, int n){
        p = new Paint();
        this.n = n;
        this.startX = startX;
        this.startY = startY;
        this.bd = bd;
        this.structure = structure;
        float sizeY = startY;
        board = new DrawCell[n][n];
        for(int i = 0; i < n; ++i){
            float sizeX = startX;
            for(int z = 0; z < n;++z){
                board[i][z] = new DrawCell(new Border(z%n==0 ||
                        structure[n*i+z-1]!=structure[n*i+z],
                        z%n==(n-1) || structure[n*i+z+1]!=structure[n*i+z],
                        i%n==0 || structure[n*i+z-n]!=structure[n*i+z],
                        i%n==(n-1) || structure[n*i+z+n]!=structure[n*i+z]),
                        sizeX,sizeY,length);
                sizeX+=length;
            }
            sizeY+=length;
        }
    }
    public void changeLength(float length){
        float sizeY = startY;
        for(int i = 0; i < n; ++i){
            float sizeX = startX;
            for(int z = 0; z < n;++z){
                board[i][z].changeLength(sizeX,sizeY,length);
                sizeX+=length;
            }
            sizeY+=length;
        }
    }

    public void draw(Canvas canvas, Paint paint){

        for(int i = 0; i < n; ++i)
            for(int z = 0; z < n; ++z)
                board[i][z].draw(paint,canvas);

        for(int i = 0; i < n; ++i){
            for(int z = 0; z < n;++z){
                board[i][z].drawBoard(paint,canvas);
                if(bd.cells[i][z].isInput) {
                    board[i][z].setTextColor(Color.BLACK);
                    board[i][z].writeText(paint, canvas, bd.cells[i][z].value);
                }
                else if(bd.cells[i][z].value!=-1) {
                    if(isCorrect(i,z))
                        board[i][z].setTextColor(Color.BLUE);
                    else
                        board[i][z].setTextColor(Color.RED);
                    board[i][z].writeText(paint, canvas, bd.cells[i][z].value);
                }
            }
        }
    }

    public void focusOnCell(float x, float y, int w, int color, int highlightColor){
        x -= startX;
        y -= startY;
        int posx = (int)x/((w-2*10)/n);
        int posy = (int)y/((w-2*10)/n);
        if(posy < n && posx < n) {
            board[posy][posx].setFillColor(color);
            highlightCell(posx,posy,highlightColor);
        }
    }
    public void refreshAll(){
        for(int i = 0; i < n; ++i)
            for(int j = 0; j < n; ++j)
                    board[i][j].setFillColor(Color.WHITE);
    }
    void highlightCell(int x, int y, int highlightColor){
        int value = bd.cells[y][x].value;
        if(value == -1)
            return;
        for(int i = 0; i < n; ++i)
            for(int j = 0; j < n; ++j) {
                if (bd.cells[i][j].value == value) {
                    if(!(i==y && j==x))
                        board[i][j].setFillColor(highlightColor);
                    if ((i == y && j != x) || (i != y && j == x) ||
                            (i != y && j != x && bd.areas[n * y + x] == bd.areas[n * i + j]))
                        board[y][x].setFillColor(Color.rgb(255, 204, 204));
                }
                else if(i==y || j==x || bd.areas[n * y + x] == bd.areas[n * i + j])
                    board[i][j].setFillColor(highlightColor);
            }
    }
    private boolean isCorrect(int x, int y){
        for(int i = 0; i < n; ++i)
            for(int j = 0; j < n; ++j)
                if(bd.cells[i][j].value==bd.cells[x][y].value){
                    if(!(x==i&&y==j)) {
                        if (bd.areas[n * x + y] == bd.areas[n * i + j])
                            return false;
                        if (x == i || y == j)
                            return false;
                    }
                }
        return true;
    }
    public void setValue(float x, float y, String value, int w){
        x -= startX;
        y -= startY;
        int posx = (int)x/((w-2*10)/n);
        int posy = (int)y/((w-2*10)/n);
        if(posy < n && posx < n && !bd.cells[posy][posx].isInput) {
            bd.cells[posy][posx].value = Byte.valueOf(value);
            highlightCell(posx,posy,Color.rgb(153,204,255));
        }
    }
}
