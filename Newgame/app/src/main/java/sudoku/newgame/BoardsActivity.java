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
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import sudoku.newgame.datahelpers.BoardBitmap;

/**
 * Created by sanya on 04.03.2018.
 */

public class BoardsActivity extends Activity {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = BoardsActivity.this.getSharedPreferences("Boards", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        setContentView(R.layout.board_layout);
        setupBitmap();
        setupPlus();
    }
    private void setupPlus() {
        ImageView plus = findViewById(R.id.imageView0);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BoardsActivity.this, DimensionActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void setupBitmap() {
        BoardBitmap board = new BoardBitmap();
        Bitmap[] boards = board.getBitmap(this, this);
        if(boards == null)
            return;
        int i;
        for(i = 0; i < boards.length; ++i) {
            String name = "imageView"+(i+1);
            int resID = this.getResources().getIdentifier(name, "id", this.getPackageName());
            ImageView img = findViewById(resID);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int id = view.getId();
                    String name = getResources().getResourceEntryName(id);
                    int i = Character.getNumericValue(name.charAt(name.length()-1));
                    SharedPreferences sp = BoardsActivity.this.getSharedPreferences("Boards", Context.MODE_PRIVATE);
                    String data = sp.getString("Array", null);
                    if(data != null) {
                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();
                        byte[][] arr = gson.fromJson(data, byte[][].class);
                        byte[] structure = arr[i-1];

                        sharedPreferences = BoardsActivity.this.getSharedPreferences("Structure", Context.MODE_PRIVATE);
                        editor = sharedPreferences.edit();
                        editor.putString("area", gson.toJson(structure));
                        editor.putLong("Time", 0);
                        editor.putInt("Dimension", (int)Math.sqrt(structure.length));
                        //editor.putBoolean("New game", true);
                        editor.apply();
                    }
                    Log.d("Board click", name);
                    showPopupMenu(view);
                }
            });
            img.setImageBitmap(boards[i]);
        }
    }
    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.inflate(R.menu.popupmenugenerator);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int t;
                switch (item.getItemId()) {
                    case R.id.menuEasy:
                        t = 13;
                        break;
                    case R.id.menuMedium:
                        t = 8;
                        break;
                    case R.id.menuHard:
                        t = 3;
                        break;
                    default:
                        return false;
                }
                editor.putInt("Difficulty", t);
                editor.putString("Boardik", null);
                editor.apply();
                startGame();
                return true;
            }
        });

        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {

            }
        });
        popupMenu.show();
    }
    private void startGame(){
        Intent intent = new Intent(BoardsActivity.this, GameActivity.class);
        startActivity(intent);
        finish();
    }
}
