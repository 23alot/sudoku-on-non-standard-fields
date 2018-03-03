package sudoku.newgame.datahelpers;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import sudoku.newgame.draw.DrawBoard;

/**
 * Created by sanya on 03.03.2018.
 */

public class BoardBitmap {
    DrawBoard db;
    int len;
    public BoardBitmap(byte[] structure, int n, float length) {
        len = (int) (length - n * 10) >> 1;
        float size = len / n;
        db = new DrawBoard(size, structure, n);
    }
    public void toBitmap() {
        Bitmap myBitmap = Bitmap.createBitmap( len, len, Bitmap.Config.RGB_565 );
        Canvas canvas = new Canvas();
        canvas.setBitmap(myBitmap);
        Paint p = new Paint();
        db.drawBitmap(canvas, p);
    }
}
