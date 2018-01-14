package sudoku.newgame;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import sudoku.newgame.draw.DrawCell;

public class MainActivity extends Activity implements View.OnTouchListener {
    private Button mbutton;
    float x;
    float y;
    DrawBoardGeneratorView db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(new DrawView(this));
        //setContentView(R.layout.main);
        setContentView(R.layout.create_board);
        db = findViewById(R.id.drawBoardGeneratorView);
        db.setOnTouchListener(this);

//        System.out.println(1);
//        final DrawView drawView = findViewById(R.id.drawView);
//        final EditText time = findViewById(R.id.editText);
//        String ss = ((Integer)((-1)%9)).toString();
//        time.setText(ss);
//        mbutton = (Button)findViewById(R.id.button4);
//        View.OnClickListener oclmbutton = new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Long start = System.currentTimeMillis();
//                drawView.drawThread.run();
//                Long finish = System.currentTimeMillis();
//                String s = ((Long)(finish-start)).toString();
//                time.setText(s);
//            }
//        };
//        mbutton.setOnClickListener(oclmbutton);
    }
    void tutu(){
        db.focusOnCell(x,y,Color.BLUE);
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