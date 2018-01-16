package sudoku.newgame;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
        Toast.makeText(this, "Запущен генератор", Toast.LENGTH_LONG).show();
        setContentView(R.layout.create_board);
        db = findViewById(R.id.drawBoardGeneratorView);
        db.setOnTouchListener(this);

    }
    void tutu(){
        db.focusOnCell(x,y, Color.BLUE);
    }
    @Override
    public boolean onTouch(View v, MotionEvent event){
        x = event.getX();
        y = event.getY();
        tutu();
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
