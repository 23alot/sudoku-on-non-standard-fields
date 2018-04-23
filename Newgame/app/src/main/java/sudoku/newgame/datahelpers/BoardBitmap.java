package sudoku.newgame.datahelpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import sudoku.newgame.BoardsActivity;
import sudoku.newgame.GameActivity;
import sudoku.newgame.draw.DrawBoard;
import sudoku.newgame.sudoku.Board;

/**
 * Created by sanya on 03.03.2018.
 */

public class BoardBitmap {
    DrawBoard db;
    byte[] structure;
    int len;
    Bitmap myBitmap;
    SharedPreferences sharedPreferences;
    public BoardBitmap(byte[] structure, int n, float length) {
        len = (int) (length - 20) >> 1;
        this.structure = structure;
        float size = len / n;
        db = new DrawBoard(2, 2, size, structure, n);
    }
    public BoardBitmap() {

    }

    public Bitmap[] getBitmap(Context context) {
        File directory = context.getDir("boards", Context.MODE_PRIVATE);
        File[] files = directory.listFiles();
        if(files == null)
            return null;
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
        Bitmap[] result = new Bitmap[names.length];
        for(int i = 0; i < names.length; ++i) {
            try {
                File f = new File(context.getDir("boards", Context.MODE_PRIVATE), "board"+i+".bmp");
                result[i] = BitmapFactory.decodeStream(new FileInputStream(f));
            }
            catch (FileNotFoundException ex) {
                Log.d("RearrangeBitmapFiles", ex.getMessage());
            }
            //result[i] = BitmapFactory.decodeStream(is);
        }
        return result;
    }
    public void toBitmap() {
        myBitmap = Bitmap.createBitmap( len, len, Bitmap.Config.RGB_565 );
        Canvas canvas = new Canvas();
        canvas.setBitmap(myBitmap);
        Paint p = new Paint();
        db.drawBitmap(canvas, p);
    }
    public void save(Context context) {
        File directory = context.getDir("boards", Context.MODE_PRIVATE);
        File[] files = directory.listFiles();
        if(files != null) {
            String[] names = new String[files.length];
            for (int i = 0; i < files.length; ++i) {
                try {
                    names[i] = files[i].getCanonicalPath();
                } catch (IOException ex) {
                    Log.d("GetBitmapFiles", ex.getMessage());
                }
            }
            Arrays.sort(names);
            rearrangeFiles(names, context);
        }
        saveBitmap("board0.bmp", myBitmap, context);
        sharedPreferences = context.getSharedPreferences("Boards",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String data = sharedPreferences.getString("Array", null);
        if(data == null) {
            editor.putString("Array", createDataArray());
            editor.apply();
        }
        else {
            editor.putString("Array", addToDataArray(data));
            editor.apply();
        }
    }
    private String addToDataArray(String data) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        byte[][] arr = gson.fromJson(data, byte[][].class);
        for(int i = 7; i >= 0; --i) {
            arr[i+1] = arr[i];
        }
        arr[0] = structure;
        return gson.toJson(arr);
    }
    private String createDataArray() {
        byte[][] arr = new byte[9][];
        for(int i = 0; i < 9; ++i) {
            arr[i] = null;
        }
        arr[0] = structure;
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.toJson(arr);
    }
    private void rearrangeFiles(String[] names, Context context) {
        int size = names.length == 9?names.length-2:names.length-1;
        for(int i = size; i >= 0; --i) {
            FileOutputStream fos = null;
            try {
                File f = new File(context.getDir("boards", Context.MODE_PRIVATE),"board"+i+".bmp");
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                File fn = new File(context.getDir("boards", Context.MODE_PRIVATE),"board"+(i+1)+".bmp");
                fos = new FileOutputStream(fn);
                b.compress(Bitmap.CompressFormat.JPEG,100, fos);
            }
            catch (FileNotFoundException ex) {
                Log.d("RearrangeBitmapFiles", ex.getMessage());
            }
            finally {
                try {
                    if(fos != null) {
                        fos.close();
                    }
                }
                catch (Exception ex) {
                    Log.d("rearrangeBoards",ex.getMessage());
                }
            }
        }
    }
    private void saveBitmap(String name,Bitmap b, Context context) {
        File mypath = new File(context.getDir("boards", Context.MODE_PRIVATE),name);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            b.compress(Bitmap.CompressFormat.JPEG, 100, fos);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(fos != null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        File directory = context.getDir("boards", Context.MODE_PRIVATE);
        File[] files = directory.listFiles();
        directory = context.getFilesDir();
        if(files!=null)
            Log.d("Save Bitmap",files.length+"");
    }
}
