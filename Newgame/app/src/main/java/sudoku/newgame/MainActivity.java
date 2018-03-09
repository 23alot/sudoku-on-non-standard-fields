package sudoku.newgame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import sudoku.newgame.draw.DrawCell;
import sudoku.newgame.sudoku.Cell;

/**
 * Created by sanya on 18.01.2018.
 */

public class MainActivity extends Activity{
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button bt = findViewById(R.id.gameButton);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });
        bt = findViewById(R.id.generatorButton);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GeneratorActivity.class);
                startActivity(intent);
            }
        });
        bt = findViewById(R.id.dimensionButton);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DimensionActivity.class);
                startActivity(intent);
            }
        });
        bt = findViewById(R.id.boardButton);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BoardsActivity.class);
                startActivity(intent);
            }
        });
        bt = findViewById(R.id.statistics);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Statictics.class);
                startActivity(intent);
            }
        });
        sharedPreferences = this.getSharedPreferences("Statistics", Context.MODE_PRIVATE);
        String stat = sharedPreferences.getString("Array", null);
        if(stat == null) {
            setupStatistics();
        }
    }
    private void setupStatistics() {
        Stat[][] data = new Stat[3][6];
        for(int i = 0; i < 3; ++i) {
            for(int z = 0; z < 6; ++z) {
                data[i][z] = new Stat(0, 0, 0, 0);
            }
        }
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Array", gson.toJson(data));
        editor.apply();
    }
    @Override
    protected void onPause() {
        super.onPause();

    }

}
