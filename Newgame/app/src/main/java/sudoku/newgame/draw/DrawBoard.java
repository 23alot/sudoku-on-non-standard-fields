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
    public DrawBoard(float startX, float startY, float length, byte[] structure, Board bd){
        p = new Paint();
        this.startX = startX;
        this.startY = startY;
        this.bd = bd;
        this.structure = structure;
        float sizeY = startY;
        board = new DrawCell[9][9];
        for(int i = 0; i < 9; ++i){
            float sizeX = startX;
            for(int z = 0; z < 9;++z){
                board[i][z] = new DrawCell(new Border(z%9==0 || structure[9*i+z-1]!=structure[9*i+z],
                        z%9==8 || structure[9*i+z+1]!=structure[9*i+z],
                        i%9==0 || structure[9*i+z-9]!=structure[9*i+z],
                        i%9==8 || structure[9*i+z+9]!=structure[9*i+z]),
                        sizeX,sizeY,length);
                sizeX+=length;
            }
            sizeY+=length;
        }


    }

    public void draw(Canvas canvas, Paint paint){
        int n = 9;

        for(int i = 0; i < n; ++i)
            for(int z = 0; z < n; ++z)
                board[i][z].draw(paint,canvas);

        for(int i = 0; i < 9; ++i){
            for(int z = 0; z < 9;++z){
                board[i][z].drawBoard(paint,canvas);
                if(bd.cells[i][z].isInput)
                    board[i][z].writeText(paint,canvas,bd.cells[i][z].value,Color.BLACK);
                else if(bd.cells[i][z].value!=-1)
                    board[i][z].writeText(paint,canvas,bd.cells[i][z].value,Color.rgb(42,13,130));
            }
        }
    }

    public void focusOnCell(float x, float y, int w, int color, int highlightColor){
        x -= startX;
        y -= startY;
        int n = 9;
        int posx = (int)x/((w-2*10)/n);
        int posy = (int)y/((w-2*10)/n);
        if(posy < n && posx < n) {
            board[posy][posx].changeFillColor(color);
            highlightCell(posx,posy,highlightColor);
        }
    }
    public void refreshAll(){
        for(int i = 0; i < bd.N; ++i)
            for(int j = 0; j < bd.N; ++j)
                board[i][j].changeFillColor(Color.WHITE);
    }
    void highlightCell(int x, int y, int highlightColor){
        int value = bd.cells[y][x].value;
        if(value == -1)
            return;
        for(int i = 0; i < bd.N; ++i)
            for(int j = 0; j < bd.N; ++j)
                if(bd.cells[i][j].value==value){
                    if((i == y && j != x)||(i != y && j == x) || (i != y && j != x && bd.areas[bd.N*y+x]==bd.areas[bd.N*i+j]))
                        board[i][j].changeFillColor(Color.RED);
                    else
                        board[i][j].changeFillColor(highlightColor);
                }
                else if(i == y || j == x || bd.areas[bd.N*y+x]==bd.areas[bd.N*i+j])
                    board[i][j].changeFillColor(highlightColor);
    }
    public void setValue(float x, float y, String value, int w){
        x -= startX;
        y -= startY;
        int n = 9;
        int posx = (int)x/((w-2*10)/n);
        int posy = (int)y/((w-2*10)/n);
        if(posy < n && posx < n && !bd.cells[posy][posx].isInput) {
            bd.cells[posy][posx].value = Byte.valueOf(value);
            highlightCell(posx,posy,Color.rgb(153,204,255));
        }
    }
}
