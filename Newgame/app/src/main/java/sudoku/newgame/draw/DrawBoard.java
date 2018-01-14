package sudoku.newgame.draw;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import sudoku.newgame.sudoku.Board;

/**
 * Created by sanya on 13.01.2018.
 */

public class DrawBoard {
    Board bd;
    DrawCell[][] board;
    byte[] structure;
    public DrawBoard(float startX, float startY, Paint p, Canvas canvas, float length, byte[] structure, Board bd){
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
        for(int i = 0; i < 9; ++i){
            for(int z = 0; z < 9;++z){
                board[i][z].drawBoard(p,canvas);
                if(bd.cells[i][z].isInput)
                    board[i][z].writeText(p,canvas,bd.cells[i][z].value);
            }
        }

    }
}
