package sudoku.newgame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import sudoku.newgame.R;
import sudoku.newgame.datahelpers.TimeHelper;

/**
 * Created by sanya on 06.03.2018.
 */

public class CongratulationActivity extends Activity {
    PopupWindow pw;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.congratulations);
        Intent intent = getIntent();
        long time = intent.getLongExtra("Time", 0);
        int difficulty = intent.getIntExtra("Difficulty", 0);
        TextView text = findViewById(R.id.gameTime);
        TimeHelper timeHelper = new TimeHelper();
        text.setText(timeHelper.millisecondsToTime(time));
        text = findViewById(R.id.difficulty);
        text.setText(difficulty+"");
        Button button = findViewById(R.id.newGame);
        final Activity act = this;
        pw = initiatePopupWindow();
        pw.setAnimationStyle(R.style.Animation);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View v = findViewById(R.id.popupView);

                pw.showAtLocation(v, Gravity.BOTTOM, 0 , 0);
            }
        });
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
            mDropdown = new PopupWindow(layout, FrameLayout.LayoutParams.MATCH_PARENT,
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
        SharedPreferences sharedPreferences = CongratulationActivity.this.getSharedPreferences("Structure", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("New game menu", "New game");
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
        ((ViewManager)button.getParent()).removeView(button);
    }
}
