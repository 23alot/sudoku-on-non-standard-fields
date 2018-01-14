package sudoku.newgame;

import android.app.Activity;
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

    }
    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        DrawView dw = findViewById(R.id.drawView);
        editor.putString("Boardik",dw.drawBoardtoJSON(dw.board));
        editor.commit();
    }
    View.OnClickListener createOnClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.setValue(x,y,(String)((Button)view).getText());
            }
        };
    }
    void refresh(float x, float y){
        db.focusOnCell(x,y,Color.WHITE);
    }
    void tutu(){
        db.focusOnCell(x,y,Color.rgb(179,179,179));
    }
    @Override
    public boolean onTouch(View v, MotionEvent event){
        float a = x;
        float b = y;
        x = event.getX();
        y = event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN: // нажатие
                refresh(a, b);
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