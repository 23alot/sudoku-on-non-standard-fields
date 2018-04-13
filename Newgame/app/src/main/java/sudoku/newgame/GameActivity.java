package sudoku.newgame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import sudoku.newgame.draw.DrawCell;
import sudoku.newgame.sudoku.Cell;

public class GameActivity extends Activity implements View.OnTouchListener {
    boolean isPen;
    private Button mbutton;
    private Cell focusedCell = null;
    private DrawCell focusedDrawCell = null;
    private int difficulty;
    private int board;
    long stopTime;
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
        sharedPreferences = GameActivity.this.getSharedPreferences("Structure", Context.MODE_PRIVATE);
        long time = sharedPreferences.getLong("Time", 0);
        setTimer(time);
        editor = sharedPreferences.edit();
        createButtons();
        Button button = findViewById(R.id.button20);
        final Activity act = this;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupWindow pw = initiatePopupWindow(view);
                View v = findViewById(R.id.drawView);
                pw.showAtLocation(v, Gravity.BOTTOM, 0 , 0);
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
            Chronometer clock = findViewById(R.id.chronometer2);
//            clock.setY(10+clock.getHeight());
            clock.setTextSize(20);
            clock.start();
//            clock.setHeight(100);
//            Button ng = findViewById(R.id.button20);
//            ng.setY(clock.getY() + 110);
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
            Chronometer clock = findViewById(R.id.chronometer2);
            clock.setTextSize(20);
            clock.start();

//            clock.setY(size.x);
//            Button ng = findViewById(R.id.button20);
//            Log.d("Clock height", clock.getHeight()+"");
//            clock.setHeight(100);
//            ng.setY(clock.getY() + 110);
        }
        final ImageButton penButton = new ImageButton(getApplicationContext());
        width = 100;
        FrameLayout.LayoutParams viewParams = new FrameLayout.LayoutParams(width,
                width);
        penButton.setLayoutParams(viewParams);
        penButton.setY(size.x + 250);
        penButton.setX(size.x - 150);
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
        clearButton.setY(size.x + 250);
        clearButton.setX(size.x - 200 - 100);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.refreshAll();
                db.setValue(x, y, w, "-1");
                db.clearPencil(x,y,w);
            }
        });
        fl.addView(clearButton);
        ImageButton menu = findViewById(R.id.buttonOverflow);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);
            }
        });
    }
    @Override
    protected void onPause() {
        super.onPause();
        Chronometer ch = findViewById(R.id.chronometer2);
        Log.d("onPause", SystemClock.elapsedRealtime()-ch.getBase()+"");
        ch.stop();
        if(!db.checkSudoku()) {
//            SharedPreferences sharedPreferences = GameActivity.this.getSharedPreferences("Structure", Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPreferences.edit();
            DrawView dw = findViewById(R.id.drawView);
            db.refreshAll();
            editor.putString("Boardik", dw.drawBoardtoJSON(dw.board));
            editor.putLong("Time",SystemClock.elapsedRealtime() - ch.getBase());
            editor.apply();
        }
    }
    private void gameStat() {
        SharedPreferences sp = GameActivity.this.getSharedPreferences("Statistics", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String data = sp.getString("Array", null);
        Stat[][] stat = gson.fromJson(data, Stat[][].class);
        int n = sharedPreferences.getInt("Dimension", 9);
        int dif = sharedPreferences.getInt("Difficulty", 8);
        dif = (dif - 3) / 5;
        stat[dif][n-4].numGames++;
        editor.putString("Array", gson.toJson(stat));
        editor.apply();
    }
    private void winStat(long time) {
        time /= 1000;
        SharedPreferences sp = GameActivity.this.getSharedPreferences("Statistics", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String data = sp.getString("Array", null);
        Stat[][] stat = gson.fromJson(data, Stat[][].class);
        int n = sharedPreferences.getInt("Dimension", 9);
        int dif = sharedPreferences.getInt("Difficulty", 8);
        Log.d("WinStat","Difficulty: " + dif + " Dimension: " + n);
        dif = (dif - 3) / 5;
        Log.d("WinStat","Difficulty: " + dif + " Dimension: " + n);
        Stat cell = stat[2-dif][n-4];
        long timing = cell.avgTime * cell.winGames;
        if(timing == 0) {
            timing = time;
        }
        cell.winGames++;
        timing = (timing + time) / 2;
        cell.avgTime = timing;
        if(cell.bestTime > time || cell.bestTime == 0) {
            cell.bestTime = time;
        }
        editor.putString("Array", gson.toJson(stat));
        editor.apply();
    }
    View.OnClickListener createOnClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPen) {
                    db.refreshAll();
                    db.setValue(x, y, w, (String) ((Button) view).getText());
                    if (db.checkSudoku()) {
                        Toast.makeText(GameActivity.this, "Судоку решено верно",
                                Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(GameActivity.this, CongratulationActivity.class);
                        Chronometer ch = findViewById(R.id.chronometer2);
                        editor.putLong("Time",0);
                        editor.putString("Boardik", null);
                        editor.apply();
                        long time = SystemClock.elapsedRealtime() - ch.getBase();
                        winStat(time);
                        intent.putExtra("Time", time);
                        startActivity(intent);
                        finish();
                    }
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
    @Override
    protected void onResume() {
        super.onResume();
        long time = sharedPreferences.getLong("Time", 0);
        Chronometer ch = findViewById(R.id.chronometer2);
        setTimer(time);
        ch.start();
    }
    private PopupWindow initiatePopupWindow(View pop) {
        PopupWindow mDropdown = null;
        try {

            LayoutInflater mInflater = (LayoutInflater) getApplicationContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = mInflater.inflate(R.layout.newgame_menu, null);

            layout.measure(View.MeasureSpec.UNSPECIFIED,
                    View.MeasureSpec.UNSPECIFIED);
            mDropdown = new PopupWindow(layout,FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT,true);
            Drawable background = getResources().getDrawable(android.R.drawable.screen_background_light);
            mDropdown.setBackgroundDrawable(background);
            mDropdown.update();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mDropdown;

    }
    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.inflate(R.menu.menu_stat);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menuSettings:
                        Log.d("Popup menu", "Settings choice");
                        break;
                    case R.id.menuRules:
                        Log.d("Popup menu", "Rules choice");
                        break;
                    case R.id.menuStatistics:
                        Log.d("Popup menu", "Statistics choice");
                        break;
                    default:
                        return false;
                }
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

    void setTimer(long time) {
        Chronometer ch = findViewById(R.id.chronometer2);
        Log.d("setTimer", SystemClock.elapsedRealtime()-ch.getBase()+"");
        ch.setBase(SystemClock.elapsedRealtime() - time);
    }
}