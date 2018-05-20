package sudoku.newgame;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import sudoku.newgame.datahelpers.DataConstants;
import sudoku.newgame.sudoku.Board;

public class DemoActivity extends Activity {
    int[] game;
    int n = 9;
    DemoDrawView dw;
    TextView tip;
    SharedPreferences sharedPreferences;
    int theme = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        sharedPreferences = DemoActivity.this.getSharedPreferences("Structure", Context.MODE_PRIVATE);
        theme = sharedPreferences.getInt("Theme", 0);
        dw = findViewById(R.id.drawView);
        tip = findViewById(R.id.text_tip);
        createButtons();
        Button bt = findViewById(R.id.button_pause);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread th = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.d("Demo", "ui");
                            highlightUI();
                            Thread.sleep(5000);
                            Log.d("Demo", "start");
                            basicPencil();
                            Thread.sleep(2500);
                            Log.d("Demo", "pencil");
                            firstPen();
                            Thread.sleep(2500);
                            Log.d("Demo", "first pen");
                            errorPen();
                            Thread.sleep(2500);
                            Log.d("Demo", "error pen");
                            fixError();
                            Thread.sleep(2500);
                            restGame();
                            Thread.sleep(1000);
                            finish();
                        }
                        catch (Exception e) {
                            Log.d("Thread exception", e.getMessage());
                        }
                    }
                });
                th.start();
            }
        });
    }
    private void createButtons(){
        FrameLayout fl = findViewById(R.id.framelayout);
        Point size = new Point();
        Display display = getWindowManager().getDefaultDisplay();
        display.getSize(size);
        int width;
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

        int margin = 5;
        width = (size.x-5*(n+1))/n;

        Button bt = null;
        sizeButtons = size.x / 6;
        for (int i = 1; i < n + 1; ++i) {
            bt = new Button(getApplicationContext());
            Log.d("Create Buttons","Button"+i+" is created");
            bt.setText(i + "");
            FrameLayout.LayoutParams viewParams = new FrameLayout.LayoutParams(width,
                    width);
            viewParams.gravity = Gravity.BOTTOM;
            viewParams.bottomMargin = sizeButtons + 4*marginButtons;
            bt.setLayoutParams(viewParams);
            Log.d("Create buttons", "Width: " + width);
            bt.setTextSize((0.85f * width) / displayMetrics.scaledDensity);
            bt.setX(i * margin + (i-1)*width);
//                bt.setHeight(width);
            bt.setPadding(0,0,0,0);
            //bt.setY(size.y - width - down);
            bt.setBackgroundColor(DataConstants.getBackgroundColor(theme));
            bt.setTextColor(DataConstants.getMainTextColor(theme));
            bt.setId(i);
            bt.setStateListAnimator(null);
            fl.addView(bt,1);
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

        penButton.setX(size.x - 6*sizeButtons / 15 - sizeButtons);
        penButton.setId(R.id.pen_button);
        fl.addView(penButton,1);

        clearButton.setX(size.x - 2*6*sizeButtons/15 - 2*sizeButtons);
        clearButton.setId(R.id.clear_button);
        fl.addView(clearButton,1);

        hintButton.setX(size.x - 3*6*sizeButtons/15 - 3*sizeButtons);
        hintButton.setId(R.id.hint_button);
        fl.addView(hintButton,1);

        undoButton.setX(size.x - 4*6*sizeButtons/15 - 4*sizeButtons);
        undoButton.setId(R.id.undo_button);
        fl.addView(undoButton,1);
        updateButtons();
    }
    private void sleep(long t) {
        try {
            Thread.sleep(t);
        }
        catch (Exception e) {

        }
    }
    private void highlightUI() {
        // New game info
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Button play = findViewById(R.id.button_pause);
                play.setBackgroundResource(R.drawable.play);
                play.setClickable(false);
                tip.setText(R.string.new_game_but);
                Button bt = findViewById(R.id.button20);
                bt.setBackgroundResource(R.drawable.highlight);
            }
        });
        sleep(2500);
        // Clock info
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Button btr = findViewById(R.id.button20);
                btr.setBackgroundResource(0);
                tip.setText(R.string.timer);
                Chronometer bt = findViewById(R.id.chronometer2);
                bt.setBackgroundResource(R.drawable.highlight);
            }
        });
        sleep(2500);
        // Play info
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Chronometer btr = findViewById(R.id.chronometer2);
                btr.setBackgroundResource(0);
                tip.setVisibility(View.VISIBLE);
                tip.setText(R.string.play_button);
                Button bt = findViewById(R.id.button_pause);
                bt.setBackgroundResource(R.drawable.play2);
            }
        });
        sleep(2500);
        // Overflow info
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Button btr = findViewById(R.id.button_pause);
                btr.setBackgroundResource(R.drawable.play);
                tip.setVisibility(View.VISIBLE);
                tip.setText(R.string.settings_button);
                ImageButton bt = findViewById(R.id.buttonOverflow);
                bt.setBackgroundResource(R.drawable.highlight);
            }
        });
        sleep(2500);
        // Buttons info
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tip.setText(R.string.button_info);
                ImageButton btr = findViewById(R.id.buttonOverflow);
                btr.setBackgroundResource(0);
                ((FrameLayout.LayoutParams)tip.getLayoutParams()).gravity = Gravity.TOP;
                for(int i = 1; i < 10; ++i) {
                    Button bt = findViewById(i);
                    bt.setBackgroundResource(R.drawable.highlight);
                }
            }
        });
        sleep(2500);
        // Pen info
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for(int i = 1; i < 10; ++i) {
                    Button bt = findViewById(i);
                    bt.setBackgroundResource(0);
                }
                tip.setText(R.string.pen_info);
                Button bt = findViewById(R.id.pen_button);
                bt.setBackgroundResource(R.drawable.pen2);
            }
        });
        sleep(2500);
        // Pencil info
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Button btr = findViewById(R.id.pen_button);
                btr.setBackgroundResource(R.drawable.pencil2);
                tip.setVisibility(View.VISIBLE);
                tip.setText(R.string.pencil_info);
            }
        });
        sleep(2500);
        // Eraser info
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Button btr = findViewById(R.id.pen_button);
                btr.setBackgroundResource(R.drawable.pen);
                tip.setVisibility(View.VISIBLE);
                tip.setText(R.string.eraser_info);
                Button bt = findViewById(R.id.clear_button);
                bt.setBackgroundResource(R.drawable.eraser2);
            }
        });
        sleep(2500);
        // Hint info
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Button btr = findViewById(R.id.clear_button);
                btr.setBackgroundResource(R.drawable.eraser);
                tip.setVisibility(View.VISIBLE);
                tip.setText(R.string.hint_info);
                Button bt = findViewById(R.id.hint_button);
                bt.setBackgroundResource(R.drawable.hint2);
            }
        });
        sleep(2500);
        // Undo info
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Button btr = findViewById(R.id.hint_button);
                btr.setBackgroundResource(R.drawable.hint);
                tip.setVisibility(View.VISIBLE);
                tip.setText(R.string.undo_info);
                Button bt = findViewById(R.id.undo_button);
                bt.setBackgroundResource(R.drawable.undo2);
            }
        });
        sleep(2500);
        // Reset
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Button bt = findViewById(R.id.undo_button);
                bt.setBackgroundResource(R.drawable.undo);
            }
        });
    }
    // First step
    private void basicPencil() {
        dw.basicPencil();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tip.setText(R.string.basic_pencil);
            }
        });

    }
    // Second step
    private void firstPen() {
        dw.firstPen();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tip.setText(R.string.set_pen);
            }
        });

    }
    // Third step
    private void errorPen() {
        dw.errorPen();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tip.setText(R.string.set_error);
            }
        });

    }
    // Fourth step
    private void fixError() {
        dw.fixError();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tip.setText(R.string.fix_error);
            }
        });

    }
    private void restGame() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tip.setText(R.string.rest_game);
            }
        });

        for(int i = 1; i < dw.game.length; ++i) {
            sleep(1000);
            dw.gameStep(i);
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tip.setText(R.string.end_game);
            }
        });
    }
    private void updateButtons() {
        RelativeLayout rl = findViewById(R.id.relativeCondition);
        rl.setBackgroundColor(DataConstants.getHeaderColor(theme));
        Chronometer ch = findViewById(R.id.chronometer2);
        ch.setTextColor(DataConstants.getMainTextColor(theme));
        Button newGame = findViewById(R.id.button20);
        newGame.setTextColor(DataConstants.getMainTextColor(theme));
        int n = sharedPreferences.getInt("Dimension",9);
        Button check = findViewById(n);
        if(check.getCurrentTextColor() == DataConstants.getMainTextColor(theme)) {
            return;
        }
        for(int i = 1; i < n+1; ++i) {
            Button bt = findViewById(i);
            bt.setBackgroundColor(DataConstants.getBackgroundColor(theme));
            bt.setTextColor(DataConstants.getMainTextColor(theme));
        }
    }
}
