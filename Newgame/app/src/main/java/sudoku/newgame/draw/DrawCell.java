package sudoku.newgame.draw;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by sanya on 13.01.2018.
 */

public class DrawCell {
    Border border;
    float startX;
    float startY;
    float length;
    public DrawCell(Border border, float startX, float startY, float length, Paint p, Canvas canvas){
        this.border = border;
        this.length = length;
        this.startX = startX;
        this.startY = startY;
        draw(p,canvas);
    }
    void draw(Paint p, Canvas canvas){
        p.setStrokeWidth(2);
        p.setColor(Color.GRAY);
        if(!border.up){
            canvas.drawLine(startX,startY,startX+length,startY,p);
        }

        if(!border.down){
            canvas.drawLine(startX,startY+length,startX+length,startY+length,p);
        }


        if(!border.left){
            canvas.drawLine(startX,startY,startX,startY+length,p);
        }

        if(!border.right){
            canvas.drawLine(startX+length,startY,startX+length,startY+length,p);
        }


    }
    public void drawBoard(Paint p, Canvas canvas){
        p.setStrokeWidth(4);
        p.setColor(Color.BLACK);
        if(border.up){
            canvas.drawLine(startX,startY,startX+length,startY,p);
        }

        if(border.down){
            canvas.drawLine(startX,startY+length,startX+length,startY+length,p);
        }


        if(border.left){
            canvas.drawLine(startX,startY,startX,startY+length,p);
        }

        if(border.right){
            canvas.drawLine(startX+length,startY,startX+length,startY+length,p);
        }
    }
    void writeText(Paint paint, Canvas canvas, Byte n){
        paint.setColor(Color.BLACK);
        paint.setTextSize(length-20);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(n.toString(),startX+length/2,startY+length-20,paint);
    }
}
