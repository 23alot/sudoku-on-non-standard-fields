package sudoku.newgame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import sudoku.newgame.datahelpers.BoardBitmap;

/**
 * Created by sanya on 04.03.2018.
 */

public class BoardsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.board_layout);
        setupBitmap();
    }
    private void setupBitmap() {
        BoardBitmap board = new BoardBitmap();
        Bitmap[] boards = board.getBitmap(this);
        if(boards == null)
            return;
        for(int i = 0; i < boards.length; ++i) {
            String name = "imageView"+(i+1);
            int resID = this.getResources().getIdentifier(name, "id", this.getPackageName());
            ImageView img = findViewById(resID);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int id = view.getId();
                    String name = getResources().getResourceEntryName(id);
                    int i = Character.getNumericValue(name.charAt(name.length()-1));
                    SharedPreferences sharedPreferences = BoardsActivity.this.getSharedPreferences("Boards", Context.MODE_PRIVATE);
                    String data = sharedPreferences.getString("Array", null);
                    if(data != null) {
                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();
                        byte[][] arr = gson.fromJson(data, byte[][].class);
                        byte[] structure = arr[i-1];
                        sharedPreferences = BoardsActivity.this.getSharedPreferences("Structure", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("area", gson.toJson(structure));
                        editor.apply();
                        Intent intent = new Intent(BoardsActivity.this, GameActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    Log.d("Board click", name);
                }
            });
            img.setImageBitmap(boards[i]);
        }
    }
}
