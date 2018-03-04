package sudoku.newgame;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import org.w3c.dom.Attr;

import sudoku.newgame.draw.DrawCell;
import sudoku.newgame.sudoku.Cell;

public class GameActivity extends Activity implements View.OnTouchListener {
    boolean isPen;
    private Button mbutton;
    private Cell focusedCell = null;
    private DrawCell focusedDrawCell = null;
    int w;
    float x;
    float y;
    DrawView db;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isPen = false;
        Point size = new Point();
        Display display = getWindowManager().getDefaultDisplay();
        display.getSize(size);
        if(size.x < size.y)
            w = size.x;
        else
            w = size.y;
        setContentView(R.layout.game);
        db = findViewById(R.id.drawView);
        db.setOnTouchListener(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(GameActivity.this);
        editor = sharedPreferences.edit();
        createButtons();
        Button button = findViewById(R.id.button20);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("Boardik",null);
                editor.apply();
                db.creation();
                db.invalidate();
            }
        });
    }
    void createButtons(){
        FrameLayout fl = (FrameLayout)findViewById(R.id.framelayout);
        int n = sharedPreferences.getInt("Dimension",9);
        Point size = new Point();
        Display display = getWindowManager().getDefaultDisplay();
        display.getSize(size);
        Log.d("Create Buttons", "x: "+size.x + " y: "+size.y);
        int width;
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            int margin = 5;
            int r = n>4?(n%2==0?n/2:n/2+1):n;
            width = (size.x - size.y - (r+1)*margin)/r;
            int i = 1;
            for(;i < r+1; ++i){
                Button bt = new Button(getApplicationContext());
                bt.setBackgroundColor(Color.WHITE);
                //bt.setBackgroundResource(R.drawable.val_button);
                bt.setText(i + "");
                FrameLayout.LayoutParams viewParams = new FrameLayout.LayoutParams(width,
                        FrameLayout.LayoutParams.WRAP_CONTENT);
                bt.setLayoutParams(viewParams);
                bt.setX(size.y + margin/2 + i * margin + (i-1)*width);
                bt.setHeight(width);
                if(r == n)
                    bt.setY(size.y - 100 - width);
                else
                    bt.setY(size.y - 100 - 2*width - 2*margin);
                bt.setId(i);
                bt.setOnClickListener(createOnClick());
                fl.addView(bt);
            }
            int k = n%2==0?0:width/2;
            for(;i < n+1; ++i){
                Button bt = new Button(getApplicationContext());
                bt.setBackgroundColor(Color.WHITE);
                bt.setText(i + "");
                FrameLayout.LayoutParams viewParams = new FrameLayout.LayoutParams(width,
                        FrameLayout.LayoutParams.WRAP_CONTENT);
                bt.setLayoutParams(viewParams);
                bt.setX(size.y + k + margin/2 + i%r * margin + ((i-1)%r)*width);
                bt.setHeight(width);
                bt.setY(size.y - 75 - width - margin);
                Log.d("Create Buttons","Button"+i+" is created " + bt.getX());
                bt.setId(i);
                bt.setOnClickListener(createOnClick());
                fl.addView(bt);
            }
        }
        else {
            int margin = 5;
            width = (size.x-5*(n+1))/n;

            for (int i = 1; i < n + 1; ++i) {
                Button bt = new Button(getApplicationContext());
                Log.d("Create Buttons","Button"+i+" is created");
                bt.setText(i + "");
                FrameLayout.LayoutParams viewParams = new FrameLayout.LayoutParams(width,
                        FrameLayout.LayoutParams.WRAP_CONTENT);
                bt.setLayoutParams(viewParams);
                bt.setX(i * margin + (i-1)*width);
                bt.setHeight(width);
                bt.setY(size.y - 100 - width);
                bt.setElevation(10);
                bt.setTranslationZ(10);
                bt.setBackgroundColor(Color.WHITE);
                bt.setId(i);
                bt.setOnClickListener(createOnClick());
                fl.addView(bt);
            }
        }
        final ImageButton penButton = new ImageButton(getApplicationContext());
        FrameLayout.LayoutParams viewParams = new FrameLayout.LayoutParams(width,
                width);
        penButton.setLayoutParams(viewParams);
        penButton.setY(size.y - 100 - 3*width);
        penButton.setX(size.x - 200);
        penButton.setBackgroundResource(R.drawable.pencil);
        penButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPen)
                    penButton.setBackgroundResource(R.drawable.pencil);
                    //penButton.setBackgroundColor(Color.RED);
                else
                    penButton.setBackgroundResource(R.drawable.pen);
                    //penButton.setBackgroundColor(Color.BLUE);
                isPen = !isPen;
            }
        });
        fl.addView(penButton);
        final Button clearButton = new Button(getApplicationContext());
        viewParams = new FrameLayout.LayoutParams(width,
                width);
        clearButton.setLayoutParams(viewParams);
        clearButton.setBackgroundResource(R.drawable.eraser);
        clearButton.setY(size.y - 100 - 3*width);
        clearButton.setX(size.x - 400);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.refreshAll();
                db.setValue(x, y, w, "-1");
                db.clearPencil(x,y,w);
            }
        });
        fl.addView(clearButton);
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
                if(isPen) {
                    db.refreshAll();
                    db.setValue(x, y, w, (String) ((Button) view).getText());
                    if (db.checkSudoku())
                        Toast.makeText(GameActivity.this, "Судоку решено верно",
                                Toast.LENGTH_LONG).show();
                }
                else {
                    Log.d("Button click","Pencil click");
                    db.setPencilValue(x, y, (String) ((Button) view).getText());
                }
            }
        };
    }
    void refresh(float x, float y){
        db.focusOnCell(x,y,w,Color.WHITE,Color.WHITE);
    }
    void tutu(){
        db.refreshAll();
        db.focusOnCell(x,y,w,Color.rgb(150,150,150),Color.rgb(153,204,255));
    }
    @Override
    public boolean onTouch(View v, MotionEvent event){
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