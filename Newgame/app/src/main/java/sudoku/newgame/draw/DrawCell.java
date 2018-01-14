package sudoku.newgame.draw;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by sanya on 13.01.2018.
 */

public class DrawCell {
    Border border;
    int fillColor;
    float startX;
    float startY;
    float length;
    public DrawCell(Border border, float startX, float startY, float length){
        this.border = border;
        this.length = length;
        this.startX = startX;
        this.startY = startY;
        fillColor = Color.WHITE;
    }
    public void draw(Paint p, Canvas canvas){
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
        fillCell(p,canvas);

    }
    public void changeFillColor(int fillColor){
        this.fillColor = fillColor;
    }
    public void fillCell(Paint p, Canvas canvas){
        p.setColor(fillColor);
        canvas.drawRect(startX+1,startY+1,startX+length-1,startY+length-1,p);
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
    void writeText(Paint paint, Canvas canvas, Byte n, int color){
        paint.setColor(color);
        paint.setTextSize(length-20);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(n.toString(),startX+length/2,startY+length-20,paint);
    }
    void writeText(Paint paint, Canvas canvas, String value, int color){
        paint.setColor(color);
        paint.setTextSize(length-20);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(value,startX+length/2,startY+length-20,paint);
    }
}
