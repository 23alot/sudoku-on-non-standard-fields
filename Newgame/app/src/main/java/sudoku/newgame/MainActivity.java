package sudoku.newgame;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.json.JSONObject;

import sudoku.newgame.draw.DrawBoard;
import sudoku.newgame.draw.DrawCell;
import sudoku.newgame.sudoku.Cell;

public class MainActivity extends Activity implements View.OnTouchListener {
    private Button mbutton;
    private Cell focusedCell = null;
    private DrawCell focusedDrawCell = null;
    float x;
    float y;
    DrawView db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        db = findViewById(R.id.drawView);
        db.setOnTouchListener(this);
        FrameLayout fl = (FrameLayout)findViewById(R.id.framelayout);
        //db.creation();
        //db.changeBoard(board);
//        FrameLayout.LayoutParams viewParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
//                FrameLayout.LayoutParams.WRAP_CONTENT);
        for(int i = 1; i < 10; ++i){
            Button bt = new Button(getApplicationContext());
            bt.setText(i+"");
            FrameLayout.LayoutParams viewParams = new FrameLayout.LayoutParams(100,
                    FrameLayout.LayoutParams.WRAP_CONTENT);
            bt.setLayoutParams(viewParams);
            bt.setX(i*100);
            bt.setHeight(30);
            bt.setY(1200);
            bt.setId(i);
            bt.setOnClickListener(createOnClick());
            fl.addView(bt);
        }
        Button button = findViewById(R.id.button20);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Boardik",null);
                editor.apply();
                db.creation();
            }
        });
        button = findViewById(R.id.button21);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GeneratorActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onPause() {
        super.onPause();
        if(!db.checkSudoku()) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            DrawView dw = findViewById(R.id.drawView);
            db.refreshAll();
            editor.putString("Boardik", dw.drawBoardtoJSON(dw.board));
            editor.apply();
        }
    }
    View.OnClickListener createOnClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.refreshAll();
                db.setValue(x,y,(String)((Button)view).getText());
                if(db.checkSudoku())
                    Toast.makeText(MainActivity.this, "Судоку решено верно", Toast.LENGTH_LONG).show();
            }
        };
    }
    void refresh(float x, float y){
        db.focusOnCell(x,y,Color.WHITE,Color.WHITE);
    }
    void tutu(){
        db.focusOnCell(x,y,Color.rgb(150,150,150),Color.rgb(153,204,255));
    }
    @Override
    public boolean onTouch(View v, MotionEvent event){
        float a = x;
        float b = y;
        x = event.getX();
        y = event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN: // нажатие
                db.refreshAll();
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