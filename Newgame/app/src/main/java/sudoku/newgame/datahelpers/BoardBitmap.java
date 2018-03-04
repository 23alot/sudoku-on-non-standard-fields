package sudoku.newgame.datahelpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import sudoku.newgame.draw.DrawBoard;

/**
 * Created by sanya on 03.03.2018.
 */

public class BoardBitmap {
    DrawBoard db;
    int len;
    Bitmap myBitmap;
    public BoardBitmap(byte[] structure, int n, float length) {
        len = (int) (length - 3 * 20) >> 1;
        float size = len / n;
        db = new DrawBoard(size, structure, n);
    }
    public void toBitmap() {
        myBitmap = Bitmap.createBitmap( len, len, Bitmap.Config.RGB_565 );
        Canvas canvas = new Canvas();
        canvas.setBitmap(myBitmap);
        Paint p = new Paint();
        db.drawBitmap(canvas, p);
    }
    public void save(Context context) {
        File directory = context.getFilesDir();
        File[] files = directory.listFiles();
        String[] names = new String[files.length];
        for(int i = 0; i < files.length; ++i) {
            try {
                names[i] = files[i].getCanonicalPath();
            }
            catch (IOException ex){
                Log.d("GetBitmapFiles", ex.getMessage());
            }

        }
        Arrays.sort(names);
        rearrangeFiles(names, context);
        saveBitmap(names[0], myBitmap, context);
    }
    private void rearrangeFiles(String[] names, Context context) {
        for(int i = names.length-2; i >= 0; --i) {
            String path = context.getFilesDir() + "/" + names[i];
            InputStream is = null;
            try {
                is = context.openFileInput(path);
            }
            catch (FileNotFoundException ex) {
                Log.d("RearrangeBitmapFiles", ex.getMessage());
            }
            Bitmap b = BitmapFactory.decodeStream(is);
            path = names[i+1];
            File mypath=new File(context.getFilesDir(), path);

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(mypath);
                b.compress(Bitmap.CompressFormat.PNG, 100, fos);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void saveBitmap(String name,Bitmap b, Context context) {
        File mypath=new File(context.getFilesDir(), name);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            b.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
