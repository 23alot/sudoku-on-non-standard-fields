package sudoku.newgame;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import sudoku.newgame.datahelpers.Size;
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
    boolean newGame = false;
    int w;
    float x;
    float y;
    DrawView db;
    PopupWindow pw;
    HistoryFragment fragmentHistory;
    RulesFragment fragmentRules;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    FragmentTransaction fTrans;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isPen = true;
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
        fragmentHistory = new HistoryFragment();
        fragmentRules = new RulesFragment();
        Button button = findViewById(R.id.button20);
        final Activity act = this;
        pw = initiatePopupWindow();
        pw.setAnimationStyle(R.style.Animation);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View v = findViewById(R.id.drawView);

                pw.showAtLocation(v, Gravity.BOTTOM, 0 , 0);
            }
        });
        setupStatistics();
    }
    void createButtons(){
        FrameLayout fl = findViewById(R.id.framelayout);
        int n = sharedPreferences.getInt("Dimension",9);
        Point size = new Point();
        Display display = getWindowManager().getDefaultDisplay();
        display.getSize(size);
        Log.d("Create Buttons", "x: "+size.x + " y: "+size.y);
        int width;
        Resources resources = getApplicationContext().getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        int marginButtons = 20;
        int sizeButtons;
        final Button penButton = new Button(getApplicationContext());
        final Button clearButton = new Button(getApplicationContext());
        final Button hintButton = new Button(getApplicationContext());
        final Button undoButton = new Button(getApplicationContext());
        penButton.setBackgroundResource(R.drawable.pen);
        penButton.setStateListAnimator(null);
        clearButton.setBackgroundResource(R.drawable.eraser);
        clearButton.setStateListAnimator(null);
        hintButton.setBackgroundResource(R.drawable.hint);
        hintButton.setStateListAnimator(null);
        undoButton.setBackgroundResource(R.drawable.undo);
        undoButton.setStateListAnimator(null);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            int margin = 5;
            int r = n>4?(n%2==0?n/2:n/2+1):n;
            sizeButtons = (size.x - size.y) / 6;
            width = (size.x - size.y - (r+1)*margin)/r;
            int i = 1;
            Button bt = null;
            for(;i < r+1; ++i){
                bt = new Button(getApplicationContext());
                bt.setBackgroundColor(Color.WHITE);
                //bt.setBackgroundResource(R.drawable.val_button);
                bt.setText(i + "");
                bt.setTextSize((0.85f * width) / displayMetrics.scaledDensity);
                FrameLayout.LayoutParams viewParams = new FrameLayout.LayoutParams(width,
                        width);
                viewParams.gravity = Gravity.BOTTOM;
                if(r == n)
                    viewParams.bottomMargin = marginButtons;
                else
                    viewParams.bottomMargin = width + 2*marginButtons;
                bt.setLayoutParams(viewParams);
                bt.setX(size.y + margin/2 + i * margin + (i-1)*width);
                bt.setHeight(width);
                bt.setPadding(0,0,0,0);

                bt.setId(i);
                bt.setOnClickListener(createOnClick());
                bt.setStateListAnimator(null);
                fl.addView(bt);
            }
            int k = n%2==0?0:width/2;
            for(;i < n+1; ++i){
                bt = new Button(getApplicationContext());
                bt.setBackgroundColor(Color.WHITE);
                bt.setText(i + "");
                FrameLayout.LayoutParams viewParams = new FrameLayout.LayoutParams(width,
                        width);
                bt.setLayoutParams(viewParams);
                bt.setTextSize((0.85f * width) / displayMetrics.scaledDensity);
                viewParams.gravity = Gravity.BOTTOM;
                viewParams.bottomMargin = marginButtons;
                bt.setX(size.y + k + margin/2 + i%r * margin + ((i-1)%r)*width);
                bt.setHeight(width);
                bt.setPadding(0,0,0,0);
//                bt.setY(size.y - 75 - width);
                Log.d("Create Buttons","Button"+i+" is created " + bt.getX());
                bt.setId(i);
                bt.setOnClickListener(createOnClick());
                bt.setStateListAnimator(null);
                fl.addView(bt);
            }

            LinearLayout layout = findViewById(R.id.linear);
            layout.setX(size.y - 50);

            ImageButton dots = findViewById(R.id.buttonOverflow);
            if (resourceId > 0) {
                int x = resources.getDimensionPixelSize(resourceId);
                dots.setX(size.x - x);
            }
            else {
                dots.setX(size.x);
            }
            FrameLayout.LayoutParams viewParamsB = new FrameLayout.LayoutParams(sizeButtons,
                    sizeButtons);
            viewParamsB.gravity = Gravity.BOTTOM;
            if(r == n) {
                viewParamsB.bottomMargin = width + 2*marginButtons;
            }
            else {
                viewParamsB.bottomMargin = 2*width + 3*marginButtons;
            }
            penButton.setLayoutParams(viewParamsB);
            clearButton.setLayoutParams(viewParamsB);
            hintButton.setLayoutParams(viewParamsB);
            undoButton.setLayoutParams(viewParamsB);

//            penButton.setY(size.y - 2 * width - 2 * margin - 75 - sizeButtons);
//            clearButton.setY(size.y - 2 * width - 2 * margin - 75 - sizeButtons);
//            hintButton.setY(size.y - 2 * width - 2 * margin - 75 - sizeButtons);
//            undoButton.setY(size.y - 2 * width - 2 * margin - 75 - sizeButtons);
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
            int down;

            if (resourceId > 0) {

                Log.d("Down","resources: " + resources.getDimensionPixelSize(resourceId));
                Log.d("Down","resources: " + displayMetrics.density);
                down = (int)(resources.getDimensionPixelSize(resourceId) / displayMetrics.density);
            }
            else {
                down = 0;
            }
            Point s = Size.getNavigationBarSize(getApplicationContext());
            Log.d("Down", "Down: " + down + " s.y: " + s.y + " s.x: " + s.x);
            Button bt = null;
            for (int i = 1; i < n + 1; ++i) {
                bt = new Button(getApplicationContext());
                Log.d("Create Buttons","Button"+i+" is created");
                bt.setText(i + "");
                FrameLayout.LayoutParams viewParams = new FrameLayout.LayoutParams(width,
                        width);
                viewParams.gravity = Gravity.BOTTOM;
                viewParams.bottomMargin = marginButtons;
                bt.setLayoutParams(viewParams);
                Log.d("Create buttons", "Width: " + width);
                bt.setTextSize((0.85f * width) / displayMetrics.scaledDensity);
                bt.setX(i * margin + (i-1)*width);
//                bt.setHeight(width);
                bt.setPadding(0,0,0,0);
                //bt.setY(size.y - width - down);
                bt.setBackgroundColor(Color.WHITE);
                bt.setId(i);
                bt.setOnClickListener(createOnClick());
                bt.setStateListAnimator(null);
                fl.addView(bt);
            }
            sizeButtons = size.x / 6;

            Chronometer clock = findViewById(R.id.chronometer2);
            clock.setTextSize(20);
            clock.start();

            FrameLayout.LayoutParams viewParamsB = new FrameLayout.LayoutParams(sizeButtons,
                    sizeButtons);
            viewParamsB.gravity = Gravity.BOTTOM;
            viewParamsB.bottomMargin = (width + 2*marginButtons);
            penButton.setLayoutParams(viewParamsB);
            clearButton.setLayoutParams(viewParamsB);
            hintButton.setLayoutParams(viewParamsB);
            undoButton.setLayoutParams(viewParamsB);
            Log.d("Button Y", "Y: " + bt.getY());
        }

        penButton.setX(size.x - 6*sizeButtons / 15 - sizeButtons);
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
        Log.d("createButtons", "width: "+penButton.getWidth()+" y: "+penButton.getY()+" x: "+penButton.getX());
        fl.addView(penButton);

        clearButton.setX(size.x - 2*6*sizeButtons/15 - 2*sizeButtons);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.refreshAll();
                db.setValue(x, y, w, "-1");
                db.clearPencil(x,y,w);
            }
        });
        fl.addView(clearButton);

        hintButton.setX(size.x - 3*6*sizeButtons/15 - 3*sizeButtons);
        hintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.refreshAll();
                db.hint(x, y);
            }
        });
        fl.addView(hintButton);

        undoButton.setX(size.x - 4*6*sizeButtons/15 - 4*sizeButtons);
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.refreshAll();
                db.undo();
            }
        });
        fl.addView(undoButton);

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
        if(!db.checkSudoku() && !newGame) {
//            SharedPreferences sharedPreferences = GameActivity.this.getSharedPreferences("Structure", Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPreferences.edit();
            DrawView dw = findViewById(R.id.drawView);
            db.refreshAll();
            editor.putString("Boardik", dw.drawBoardtoJSON(dw.board));
            editor.putLong("Time",SystemClock.elapsedRealtime() - ch.getBase());
            editor.apply();
        }
    }
    private void setupStatistics() {
        SharedPreferences sp = this.getSharedPreferences("Statistics", Context.MODE_PRIVATE);
        String stat = sp.getString("Array", null);
        if(stat != null) {
            return;
        }
        Stat[][] data = new Stat[3][6];
        for(int i = 0; i < 3; ++i) {
            for(int z = 0; z < 6; ++z) {
                data[i][z] = new Stat(0, 0, 0, 0);
            }
        }
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("Array", gson.toJson(data));
        editor.apply();
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
        stat[2-dif][n-4].numGames++;
        editor.putString("Array", gson.toJson(stat));
        editor.apply();
    }
    private void winStat(long time) {
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
        cell.numGames++;
        timing = (timing + time) / cell.winGames;
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
                        intent.putExtra("Difficulty", sharedPreferences.getInt("Difficulty", 0));
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
    private PopupWindow initiatePopupWindow() {
        PopupWindow mDropdown = null;
        try {

            LayoutInflater mInflater = (LayoutInflater) getApplicationContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = mInflater.inflate(R.layout.newgame_menu, null);
            setupNewGameMenu(layout);
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
    private void setupNewGameMenu(View popupView) {
        Button button = popupView.findViewById(R.id.new_game);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("New game menu", "New game");
                newGame = true;
                gameStat();
                editor.putLong("Time",0);
                editor.putString("Boardik", null);
                editor.apply();
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                startActivity(intent);
                pw.dismiss();
                finish();
            }
        });
        button = popupView.findViewById(R.id.new_field);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("New game menu", "New field");
                newGame = true;
                gameStat();
                editor.putLong("Time",0);
                editor.apply();
                Intent intent = new Intent(getApplicationContext(), DimensionActivity.class);
                startActivity(intent);
                pw.dismiss();
                finish();
            }
        });
        button = popupView.findViewById(R.id.new_easy);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("New game menu", "New easy");
                newGame = true;
                gameStat();
                editor.putLong("Time",0);
                editor.putString("Boardik", null);
                editor.putInt("Difficulty", 13);
                editor.apply();
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                startActivity(intent);
                pw.dismiss();
                finish();
            }
        });
        button = popupView.findViewById(R.id.new_medium);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("New game menu", "New medium");
                newGame = true;
                gameStat();
                editor.putLong("Time",0);
                editor.putString("Boardik", null);
                editor.putInt("Difficulty", 8);
                editor.apply();
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                startActivity(intent);
                pw.dismiss();
                finish();
            }
        });
        button = popupView.findViewById(R.id.new_hard);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("New game menu", "New hard");
                newGame = true;
                gameStat();
                editor.putLong("Time",0);
                editor.putString("Boardik", null);
                editor.putInt("Difficulty", 3);
                editor.apply();
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                startActivity(intent);
                pw.dismiss();
                finish();
            }
        });
        button = popupView.findViewById(R.id.repeat_game);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("New game menu", "Repeat game");
                pw.dismiss();
                newGame = true;
                db.board.bd.resetValues();
                gameStat();
                setTimer(0);
                db.refreshAll();
            }
        });
    }
    @Override
    public void onBackPressed() {
        if(fragmentHistory.active) {
            fTrans = getFragmentManager().beginTransaction();
            fragmentHistory.active = false;
            fTrans.remove(fragmentHistory);
            fTrans.commit();
        }
        else if(fragmentRules.active) {
            fTrans = getFragmentManager().beginTransaction();
            fragmentRules.active = false;
            fTrans.remove(fragmentRules);
            fTrans.commit();
        }
        else {
            super.onBackPressed();
        }
    }
    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.inflate(R.menu.menu_stat);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                fTrans = getFragmentManager().beginTransaction();
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.menuSettings:
                        Log.d("Popup menu", "Settings choice");
                        intent = new Intent(getApplicationContext(), SettingsActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.menuRules:
                        Log.d("Popup menu", "Rules choice");
                        fragmentRules.active = true;
                        fTrans.add(R.id.framelayout, fragmentRules);
                        break;
                    case R.id.menuStatistics:
                        Log.d("Popup menu", "Statistics choice");
                        intent = new Intent(getApplicationContext(), StatisticsActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.menuHistory:
                        Log.d("Popup menu", "History choice");
                        fragmentHistory.active = true;
                        fragmentHistory.setHistory(db.board.gameHistory);
                        fTrans.add(R.id.framelayout, fragmentHistory);
                        break;
                    default:
                        return false;
                }
                fTrans.commit();
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