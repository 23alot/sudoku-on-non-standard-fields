package sudoku.newgame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import sudoku.newgame.datahelpers.BoardBitmap;

/**
 * Created by sanya on 14.01.2018.
 */

public class GeneratorActivity extends Activity implements View.OnTouchListener {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private Button acceptButton;
    private Button declineButton;
    float x;
    float y;
    DrawBoardGeneratorView db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_board);
        sharedPreferences = this.getSharedPreferences("Structure", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        db = findViewById(R.id.drawBoardGeneratorView);
        db.setOnTouchListener(this);
        acceptButton = findViewById(R.id.button50);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptButton.setBackgroundColor(Color.WHITE);
                acceptButton.setVisibility(View.INVISIBLE);
                if(db.saveArea()) {
                    showPopupMenu(view);
                    acceptButton.setBackgroundColor(Color.WHITE);
                    acceptButton.setVisibility(View.VISIBLE);
                }
            }
        });
        declineButton = findViewById(R.id.button51);
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.declineArea();
            }
        });
    }
    void startNewGame(){
        Point size = new Point();
        Display display = getWindowManager().getDefaultDisplay();
        display.getSize(size);
        float w;
        if(size.x < size.y)
            w = size.x;
        else
            w = size.y;
        BoardBitmap bitmap = new BoardBitmap(db.prpr,db.n,w);
        bitmap.toBitmap();
        bitmap.save(this);
        Intent intent = new Intent(this, GameActivity.class);
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        editor.putString("area",gson.toJson(db.prpr));
        editor.apply();
        startActivity(intent);
        finish();
    }

    void tutu(){
        db.focusOnCell(x,y, Color.BLUE);
    }
    void tutuMove(){
        db.focusOnCellMove(x,y, Color.BLUE);
    }
    @Override
    public boolean onTouch(View v, MotionEvent event){
        x = event.getX();
        y = event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN: // нажатие
                tutu();
                break;
            case MotionEvent.ACTION_MOVE: // движение
                tutuMove();
                break;
            case MotionEvent.ACTION_UP: // отпускание
            case MotionEvent.ACTION_CANCEL:
                //
                break;
        }
        return true;
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
                        startNewGame();
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
}
