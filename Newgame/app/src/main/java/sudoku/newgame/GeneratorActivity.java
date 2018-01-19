package sudoku.newgame;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by sanya on 14.01.2018.
 */

public class GeneratorActivity extends Activity implements View.OnTouchListener {
    private Button mbutton;
    float x;
    float y;
    DrawBoardGeneratorView db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_board);
        db = findViewById(R.id.drawBoardGeneratorView);
        db.setOnTouchListener(this);
        mbutton = findViewById(R.id.button50);
//        mbutton.setVisibility(View.INVISIBLE);
        mbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(db.saveArea())
                    startNewGame();

                mbutton.setVisibility(View.INVISIBLE);
                mbutton.setBackgroundColor(Color.WHITE);
            }
        });
    }
    void startNewGame(){
        Intent intent = new Intent(this, GameActivity.class);
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("area",gson.toJson(db.prpr));
        editor.apply();
        startActivity(intent);
        finish();
    }
    void tutu(){
        db.focusOnCell(x,y, Color.BLUE);
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
                //
                break;
            case MotionEvent.ACTION_UP: // отпускание
            case MotionEvent.ACTION_CANCEL:
                //
                break;
        }
        return true;
    }

}
