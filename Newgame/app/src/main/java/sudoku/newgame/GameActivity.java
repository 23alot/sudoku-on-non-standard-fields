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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import sudoku.newgame.datahelpers.DataConstants;
import sudoku.newgame.datahelpers.Size;
import sudoku.newgame.datahelpers.UserTime;
import sudoku.newgame.draw.DrawCell;
import sudoku.newgame.sudoku.Cell;

public class GameActivity extends Activity implements View.OnTouchListener {
    boolean isPen;
    boolean isAd;
    View layout;
    boolean isPause = true;
    boolean isFragment = false;
    private Button mbutton;
    private Cell focusedCell = null;
    private DrawCell focusedDrawCell = null;
    private int difficulty;
    private int board;
    private boolean recreate = false;
    long stopTime;
    long winTime;
    int winDif;
    boolean newGame = false;
    int w;
    public int theme = 0;
    float x;
    float y;
    DrawView db;
    PopupWindow pw;
    HistoryFragment fragmentHistory;
    RulesFragment fragmentRules;
    PauseFragment fragmentPause;
    SettingsFragment fragmentSettings;
    LoginFragment fragmentLogin;
    CongratulationFragment fragmentCongratulation;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    FragmentTransaction fTrans;
    private RewardedVideoAd mRewardedVideoAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        long start = System.currentTimeMillis();
        sharedPreferences = GameActivity.this.getSharedPreferences("Structure", Context.MODE_PRIVATE);
        theme = sharedPreferences.getInt("Theme", 0);
        setup();
        setContentView(R.layout.game);
        db = findViewById(R.id.drawView);
        // db.invalidate();
        db.setOnTouchListener(this);
        long time = sharedPreferences.getLong("Time", 0);
        editor = sharedPreferences.edit();
        setTimer(time);
        createButtons();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null && sharedPreferences.getBoolean("Auth", true)) {
            fTrans = getFragmentManager().beginTransaction();
            fragmentLogin.isActive = true;
            fTrans.add(R.id.framelayout, fragmentLogin);
            isFragment = true;
            Log.d("Login","stopTime "+System.currentTimeMillis());
            stopTime();
            fTrans.commit();
        }
        Log.d("OnCreate", "Create time: " + (System.currentTimeMillis() - start));
    }
    private void setup() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                isPen = true;
                recreate = false;
                Point size = new Point();
                Display display = getWindowManager().getDefaultDisplay();
                display.getSize(size);
                if(size.x < size.y)
                    w = size.x;
                else
                    w = size.y;
                fragmentHistory = new HistoryFragment();
                fragmentRules = new RulesFragment();
                fragmentPause = new PauseFragment();
                fragmentSettings = new SettingsFragment();
                fragmentLogin = new LoginFragment();
                fragmentCongratulation = new CongratulationFragment();
                setupStatistics();
                pw = initiatePopupWindow();
                pw.setAnimationStyle(R.style.Animation);
            }
        });
        thread.start();
    }
    void createButtons() {
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
        Button button = findViewById(R.id.button20);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFragment) {
                    return;
                }
                View v = findViewById(R.id.drawView);

                pw.showAtLocation(v, Gravity.BOTTOM, 0 , 0);
            }
        });
        penButton.setId(R.id.pen_button);
        clearButton.setId(R.id.clear_button);
        hintButton.setId(R.id.hint_button);
        undoButton.setId(R.id.undo_button);
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
                bt.setBackgroundColor(DataConstants.getBackgroundColor(theme));
                bt.setTextColor(DataConstants.getMainTextColor(theme));
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
                bt.setTextColor(DataConstants.getMainTextColor(theme));
                bt.setBackgroundColor(DataConstants.getBackgroundColor(theme));
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
                Log.d("Create Buttons","Button"+i+" is created " + bt.getX());
                bt.setId(i);
                bt.setOnClickListener(createOnClick());
                bt.setStateListAnimator(null);
                fl.addView(bt);
            }

            RelativeLayout layout = findViewById(R.id.relativeCondition);
            layout.setX(size.y - 50);
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

            Chronometer clock = findViewById(R.id.chronometer2);
            clock.setTextSize(20);
            clock.start();
        }
        else {

            sizeButtons = size.x / 6;
            width = (size.x-5*(n+1))/n;
            if(sizeButtons + width + 4*marginButtons + size.x + 100> size.y) {
                width = size.y - size.x - sizeButtons - 3*marginButtons - 100;
            }
            int down;
            int margin = (size.x - n*width)/(n+1);
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
                bt.setTextColor(DataConstants.getMainTextColor(theme));
                bt.setText(i + "");
                FrameLayout.LayoutParams viewParams = new FrameLayout.LayoutParams(width,
                        width);
                viewParams.gravity = Gravity.BOTTOM;
                viewParams.bottomMargin = sizeButtons + 3*marginButtons;
                bt.setLayoutParams(viewParams);
                Log.d("Create buttons", "Width: " + width);
                bt.setTextSize((0.85f * width) / displayMetrics.scaledDensity);
                bt.setX(i * margin + (i-1)*width);
//                bt.setHeight(width);
                bt.setPadding(0,0,0,0);
                //bt.setY(size.y - width - down);
                bt.setBackgroundColor(DataConstants.getBackgroundColor(theme));
                bt.setId(i);
                bt.setOnClickListener(createOnClick());
                bt.setStateListAnimator(null);
                fl.addView(bt);
            }

            Chronometer clock = findViewById(R.id.chronometer2);
            clock.setTextSize(20);
            clock.start();

            FrameLayout.LayoutParams viewParamsB = new FrameLayout.LayoutParams(sizeButtons,
                    sizeButtons);
            viewParamsB.gravity = Gravity.BOTTOM;
            viewParamsB.bottomMargin = (marginButtons);
            penButton.setLayoutParams(viewParamsB);
            clearButton.setLayoutParams(viewParamsB);
            hintButton.setLayoutParams(viewParamsB);
            undoButton.setLayoutParams(viewParamsB);

        }
        Button pause = findViewById(R.id.button_pause);
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFragment) {
                    return;
                }
                if(isPause) {
                    setPause();
                }
                else {
                    setPlay();
                }
            }
        });

        penButton.setX(size.x - 6*sizeButtons / 15 - sizeButtons);
        penButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isPause || isFragment) {
                    return;
                }
                if(isPen) {
                    penButton.setBackgroundResource(R.drawable.pencil);
                }
                else {
                    penButton.setBackgroundResource(R.drawable.pen);
                }
                updateButtons();
                isPen = !isPen;
            }
        });
        Log.d("createButtons", "width: "+penButton.getWidth()+" y: "+penButton.getY()+" x: "+penButton.getX());
        fl.addView(penButton);

        clearButton.setX(size.x - 2*6*sizeButtons/15 - 2*sizeButtons);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isPause || isFragment) {
                    return;
                }
                db.refreshAll();
                db.setValue(x, y, w, "-1");
                db.clearPencil(x,y,w);
            }
        });
        fl.addView(clearButton);

        hintButton.setX(size.x - 3*6*sizeButtons/15 - 3*sizeButtons);
        hintButton.setId(R.id.hint_button);
        hintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isPause || isFragment) {
                    return;
                }
                hint();
            }
        });
        fl.addView(hintButton);

        undoButton.setX(size.x - 4*6*sizeButtons/15 - 4*sizeButtons);
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isPause || isFragment) {
                    return;
                }
                db.refreshAll();
                db.undo();
            }
        });
        fl.addView(undoButton);

        ImageButton menu = findViewById(R.id.buttonOverflow);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFragment) {
                    return;
                }
                showPopupMenu(view);
            }
        });
        updateButtons();
    }
    private void hint() {
        db.refreshAll();
        hintReward();
        long time = sharedPreferences.getLong("Time", 0);
        editor.putLong("Time", time + 30000);
        editor.apply();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Chronometer ch = findViewById(R.id.chronometer2);
        ch.stop();
        try {
            if (db != null && !db.checkSudoku() && !newGame) {
                DrawView dw = findViewById(R.id.drawView);
                db.refreshAll();
                editor.putString("Boardik", dw.drawBoardtoJSON(dw.board));
                if(isPause && !isFragment) {
                    editor.putLong("Time", SystemClock.elapsedRealtime() - ch.getBase());
                }
                editor.apply();
            }
        }
        catch(Exception e) {
            Log.d("Pause", "Error in pause");
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
        Chronometer ch = findViewById(R.id.chronometer2);
        if(SystemClock.elapsedRealtime() - ch.getBase() < 15000)
            return;

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
        UserTime winner = new UserTime(time, "Matt");
        String difficulty;
        switch(2-dif) {
            case 0:
                difficulty = "easy";
                break;
            case 1:
                difficulty = "medium";
                break;
            case 2:
                difficulty = "hard";
                break;
            default: difficulty = "error";
        }
        winner.addToDataBase(n+"", difficulty);
        editor.apply();
    }
    View.OnClickListener createOnClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isPause || isFragment) {
                    return;
                }
                if(isPen) {
                    db.refreshAll();
                    db.setValue(x, y, w, ((Button) view).getText().toString());
                    if (db.checkSudoku()) {
                        Toast.makeText(GameActivity.this, "Судоку решено верно",
                                Toast.LENGTH_LONG).show();
                        Chronometer ch = findViewById(R.id.chronometer2);

                        long time = SystemClock.elapsedRealtime() - ch.getBase();
                        editor.putLong("Time",0);
                        editor.putString("Boardik", null);
                        editor.apply();
                        winStat(time);
                        winTime = time;
                        winDif = sharedPreferences.getInt("Difficulty", 0);
                        fTrans = getFragmentManager().beginTransaction();
                        fragmentCongratulation.isActive = true;
                        fTrans.add(R.id.framelayout, fragmentCongratulation);
                        isFragment = true;
                        stopTime();
                        fTrans.commit();
                    }
                }
                else {
                    Log.d("Button click","Pencil click");
                    db.setPencilValue(x, y, ((Button) view).getText().toString());
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
        if(recreate) {
            return;
        }
        theme = sharedPreferences.getInt("Theme", 0);
        updateButtons();
        Log.d("onResume",""+isPause);
        if(isPause && !isFragment) {
            Log.d("onResume","stopTime "+System.currentTimeMillis());
            long time = sharedPreferences.getLong("Time", 0);
            Chronometer ch = findViewById(R.id.chronometer2);
            setTimer(time);
            ch.start();
        }
    }
    private PopupWindow initiatePopupWindow() {
        PopupWindow mDropdown = null;
        try {

            LayoutInflater mInflater = (LayoutInflater) getApplicationContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = mInflater.inflate(R.layout.newgame_menu, null);
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
                setTimer(0);
                db.board = null;
                resumeTime();
                db.invalidate();
                pw.dismiss();
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
                Intent intent = new Intent(getApplicationContext(), BoardsActivity.class);
                startActivityForResult(intent, 1);
                pw.dismiss();
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
                setTimer(0);
                db.board = null;
                resumeTime();
                db.invalidate();
                pw.dismiss();
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
                setTimer(0);
                db.board = null;
                resumeTime();
                db.invalidate();
                pw.dismiss();
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
                setTimer(0);
                db.board = null;
                resumeTime();
                db.invalidate();
                pw.dismiss();
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
        if(fragmentHistory.isActive) {
            fTrans = getFragmentManager().beginTransaction();
            fragmentHistory.isActive = false;
            fTrans.remove(fragmentHistory);
            fTrans.commit();
            isFragment = false;
            resumeTime();
        }
        else if(fragmentRules.isActive) {
            fTrans = getFragmentManager().beginTransaction();
            fragmentRules.isActive = false;
            fTrans.remove(fragmentRules);
            fTrans.commit();
            isFragment = false;
            resumeTime();
        }
        else if(fragmentSettings.isActive) {
            fTrans = getFragmentManager().beginTransaction();
            fragmentSettings.isActive = false;
            theme = sharedPreferences.getInt("Theme", 0);
            updateButtons();
            db.invalidate();
            fTrans.remove(fragmentSettings);
            fTrans.commit();
            isFragment = false;
            resumeTime();
        }
        else if(fragmentLogin.isActive) {
            fTrans = getFragmentManager().beginTransaction();
            fragmentLogin.isActive = false;
            theme = sharedPreferences.getInt("Theme", 0);
            updateButtons();
            db.invalidate();
            fTrans.remove(fragmentLogin);
            fTrans.commit();
            isFragment = false;
            resumeTime();
        }
        else if(fragmentCongratulation.isActive) {
            fTrans = getFragmentManager().beginTransaction();
            fragmentCongratulation.isActive = false;
            fTrans.remove(fragmentCongratulation);
            fTrans.commit();
            setTimer(0);
            db.board = null;
            isFragment = false;
            resumeTime();
            db.invalidate();
        }
        else {
            super.onBackPressed();
        }
    }
    private void stopTime() {
        Log.d("stopTime","stopTime "+System.currentTimeMillis());
        if(!isPause) {
            return;
        }
        Chronometer ch = findViewById(R.id.chronometer2);
        ch.stop();
        long time = SystemClock.elapsedRealtime() - ch.getBase();
        editor.putLong("Time", time);
        editor.apply();
    }
    public void resumeTime() {
        if(!isPause) {
            return;
        }
        Chronometer ch = findViewById(R.id.chronometer2);
        long time = sharedPreferences.getLong("Time", 0);
        ch.setBase(SystemClock.elapsedRealtime() - time);
        ch.start();
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
                        fragmentSettings.isActive = true;
                        fTrans.add(R.id.framelayout, fragmentSettings);
                        isFragment = true;
                        stopTime();
                        break;
                    case R.id.menuRules:
                        Log.d("Popup menu", "Rules choice");
                        fragmentRules.isActive = true;
                        fTrans.add(R.id.framelayout, fragmentRules);
                        isFragment = true;
                        stopTime();
                        break;
                    case R.id.menuStatistics:
                        Log.d("Popup menu", "Statistics choice");
                        intent = new Intent(getApplicationContext(), TuturuActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        break;
                    case R.id.menuHistory:
                        Log.d("Popup menu", "History choice");
                        fragmentHistory.isActive = true;
                        fragmentHistory.setHistory(db.board.gameHistory);
                        fTrans.add(R.id.framelayout, fragmentHistory);
                        isFragment = true;
                        stopTime();
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
        editor.putLong("Time", time);
        editor.apply();
        ch.setBase(SystemClock.elapsedRealtime() - time);
    }

    private void setPause() {
        LinearLayout ll = findViewById(R.id.pause_layout);
        ll.setVisibility(View.VISIBLE);
        fragmentPause.isActive = true;
        Button pause = findViewById(R.id.button_pause);
        pause.setBackgroundResource(R.drawable.play);
        stopTime();
        fTrans = getFragmentManager().beginTransaction();
        fTrans.add(R.id.pause_layout, fragmentPause);
        fTrans.commit();
        isPause = false;
    }
    private void setPlay() {
        LinearLayout ll = findViewById(R.id.pause_layout);
        ll.setVisibility(View.INVISIBLE);
        fragmentPause.isActive = false;
        Button pause = findViewById(R.id.button_pause);
        pause.setBackgroundResource(R.drawable.pause);
        fTrans = getFragmentManager().beginTransaction();
        fTrans.remove(fragmentPause);
        fTrans.commit();
        // isAd = false;
        Button hint = findViewById(R.id.hint_button);
        hint.setEnabled(true);
        isPause = true;
        resumeTime();
    }

    private void hintReward() {
        db.hint(x, y);
        if (db.checkSudoku()) {
            Toast.makeText(GameActivity.this, "Судоку решено верно",
                    Toast.LENGTH_LONG).show();
            Chronometer ch = findViewById(R.id.chronometer2);

            long time = SystemClock.elapsedRealtime() - ch.getBase();
            editor.putLong("Time",0);
            editor.putString("Boardik", null);
            editor.apply();
            winStat(time);
            winTime = time;
            winDif = sharedPreferences.getInt("Difficulty", 0);
            fTrans = getFragmentManager().beginTransaction();
            fragmentCongratulation.isActive = true;
            fTrans.add(R.id.framelayout, fragmentCongratulation);
            isFragment = true;
            stopTime();
            fTrans.commit();
        }
    }
    private void updateButtons() {
        LinearLayout ll = findViewById(R.id.pause_layout);
        ll.setBackgroundColor(DataConstants.getBackgroundColor(theme));
        if(fragmentPause.isActive) {
            RelativeLayout rl = fragmentPause.fragment.findViewById(R.id.fragment_pause);
            rl.setBackgroundColor(DataConstants.getBackgroundColor(theme));
        }
        RelativeLayout rl = findViewById(R.id.relativeCondition);
        rl.setBackgroundColor(DataConstants.getHeaderColor(theme));
        Chronometer ch = findViewById(R.id.chronometer2);
        ch.setTextColor(DataConstants.getMainTextColor(theme));
        Button newGame = findViewById(R.id.button20);
        newGame.setTextColor(DataConstants.getMainTextColor(theme));
        // New game menu
        while(layout == null);
        LinearLayout linearLayout = layout.findViewById(R.id.new_game_layout);
        linearLayout.setBackgroundColor(DataConstants.getBackgroundColor(theme));
        Button bt = layout.findViewById(R.id.new_game);
        bt.setTextColor(DataConstants.getMainTextColor(theme));
        bt = layout.findViewById(R.id.repeat_game);
        bt.setTextColor(DataConstants.getMainTextColor(theme));
        bt = layout.findViewById(R.id.new_field);
        bt.setTextColor(DataConstants.getMainTextColor(theme));
        bt = layout.findViewById(R.id.new_easy);
        bt.setTextColor(DataConstants.getMainTextColor(theme));
        bt = layout.findViewById(R.id.new_medium);
        bt.setTextColor(DataConstants.getMainTextColor(theme));
        bt = layout.findViewById(R.id.new_hard);
        bt.setTextColor(DataConstants.getMainTextColor(theme));
        int n = sharedPreferences.getInt("Dimension",9);
        Log.d("updateButtons", n+"");
        Button check = findViewById(n);
        if(check.getCurrentTextColor() == DataConstants.getMainTextColor(theme)) {
            return;
        }
        for(int i = 1; i < n+1; ++i) {
            Button btr = findViewById(i);
            btr.setBackgroundColor(DataConstants.getBackgroundColor(theme));
            btr.setTextColor(DataConstants.getMainTextColor(theme));
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("onActivityResult", "recreate");
        recreate = true;
        this.recreate();
    }
}