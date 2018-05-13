package sudoku.newgame.draw;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import sudoku.newgame.datahelpers.DataConstants;

/**
 * Created by sanya on 13.01.2018.
 */

public class DrawCell {
    public Border border;
    int fillColor;
    int textColor;
    float startX;
    float startY;
    public float length;
    public DrawCell(Border border, float startX, float startY, float length, int theme){
        this.border = border;
        this.length = length;
        this.startX = startX;
        this.startY = startY;
        fillColor = DataConstants.getFillColor(theme);
        textColor = DataConstants.getMainTextColor(theme);
    }
    public void changeLength(float startX, float startY, float length){
        this.startX = startX;
        this.startY = startY;
        this.length = length;
    }
    public void draw(Paint p, Canvas canvas, int theme){
        p.setStrokeWidth(3);
        p.setColor(DataConstants.getBackgroundColor(theme));
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
        fillCell(p, canvas, theme);

    }
    public void setFillColor(int fillColor){
        this.fillColor = fillColor;
    }
    public int getFillColor(){ return fillColor; }
    public void setTextColor(int textColor){
        this.textColor = textColor;
    }
    public void fillCell(Paint p, Canvas canvas, int theme){
        p.setColor(fillColor);
        canvas.drawRect(startX+1,startY+1,startX+length-1,startY+length-1,p);
    }
    public void drawBoard(Paint p, Canvas canvas, int theme){
        p.setStrokeWidth(4);
        p.setColor(DataConstants.getMainTextColor(theme));
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
    void writePossibleValues(Paint paint, Canvas canvas, boolean[] possible, int theme){
        paint.setColor(DataConstants.getHelpTextColor(theme));
        paint.setTextSize(length/3-2);
        int k = possible.length < 6? 2:3;
        int r = possible.length - 2*k;
        paint.setTextAlign(Paint.Align.LEFT);
        int i = 0;
        if(k == 2){
            if(possible[i++])
                canvas.drawText(i + "", startX + 10,
                        startY + length / 3 - 10, paint);
            if(possible[i++])
                canvas.drawText(i + "", startX + 2*length/3 + 10,
                        startY + length / 3 - 10, paint);
            if(r!=0 && possible[i++])
                canvas.drawText(i + "", startX + length/3 + 10,
                        startY + 2 *length / 3 - 10, paint);
            if(possible[i++])
                canvas.drawText(i + "", startX + 10,
                        startY + length - 10, paint);
            if(possible[i++])
                canvas.drawText(i + "", startX + 2*length/3 + 10,
                        startY + length - 10, paint);
            return;
        }
        if(possible[i++])
            canvas.drawText(i + "", startX + 10,
                    startY + length / 3 - 10, paint);
        if(possible[i++])
            canvas.drawText(i + "", startX + length/3 + 10,
                    startY + length / 3 - 10, paint);
        if(possible[i++])
            canvas.drawText(i + "", startX + 2*length/3 + 10,
                    startY + length / 3 - 10, paint);
        if(r == 1) {
            if (possible[i++])
                canvas.drawText(i + "", startX + length / 3 + 10,
                        startY + 2 * length / 3 - 10, paint);
        }
        else if(r == 2){
            if(possible[i++])
                canvas.drawText(i + "", startX + 10,
                        startY + 2*length / 3 - 10, paint);
            if(possible[i++])
                canvas.drawText(i + "", startX + 2*length/3 + 10,
                        startY + 2*length / 3 - 10, paint);
        }
        else if(r == 3){
            if(possible[i++])
                canvas.drawText(i + "", startX + 10,
                        startY + 2*length / 3 - 10, paint);
            if(possible[i++])
                canvas.drawText(i + "", startX + length/3 + 10,
                        startY + 2*length / 3 - 10, paint);
            if(possible[i++])
                canvas.drawText(i + "", startX + 2*length/3 + 10,
                        startY + 2*length / 3 - 10, paint);
        }
        if(possible[i++])
            canvas.drawText(i + "", startX + 10,
                    startY + length - 10, paint);
        if(possible[i++])
            canvas.drawText(i + "", startX + length/3 + 10,
                    startY + length - 10, paint);
        if(possible[i++])
            canvas.drawText(i + "", startX + 2*length/3 + 10,
                    startY + length - 10, paint);
    }
    void writeText(Paint paint, Canvas canvas, Byte n){
        paint.setColor(textColor);
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
