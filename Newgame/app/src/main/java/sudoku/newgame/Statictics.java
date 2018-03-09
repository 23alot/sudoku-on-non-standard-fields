package sudoku.newgame;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by sanya on 08.03.2018.
 */

public class Statictics extends Activity {
    private RadioGroup radioGroupDif;
    private RadioGroup radioGroupBoard;
    private SharedPreferences sharedPreferences;
    private int curDifficulty;
    private int curBoard;
    private Stat[][] stat;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics);

        radioGroupBoard = findViewById(R.id.radioGroupBoard);
        radioGroupDif = findViewById(R.id.radioGroupDifficulty);
        sharedPreferences = this.getSharedPreferences("Statistics", Context.MODE_PRIVATE);
        setupStatistics();
        radioGroupBoard.setOnCheckedChangeListener(listenerBoard());
        radioGroupDif.setOnCheckedChangeListener(listenerDifficulty());
        refreshText();
    }
    private RadioGroup.OnCheckedChangeListener listenerBoard() {
        return new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch (checkedId)
                {
                    case R.id.radio4:
                        curBoard = 0;
                        break;
                    case R.id.radio5:
                        curBoard = 1;
                        break;
                    case R.id.radio6:
                        curBoard = 2;
                        break;
                    case R.id.radio7:
                        curBoard = 3;
                        break;
                    case R.id.radio8:
                        curBoard = 4;
                        break;
                    case R.id.radio9:
                        curBoard = 5;
                        break;
                    default:
                        break;
                }
                refreshText();
            }
        };
    }
    private RadioGroup.OnCheckedChangeListener listenerDifficulty() {
        return new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch (checkedId)
                {
                    case R.id.radioEasy:
                        curDifficulty = 0;
                        break;
                    case R.id.radioMedium:
                        curDifficulty = 1;
                        break;
                    case R.id.radioHard:
                        curDifficulty = 2;
                        break;
                    default:
                        break;
                }
                refreshText();
            }
        };
    }
    private void setupStatistics() {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String data = sharedPreferences.getString("Array", null);
        stat = gson.fromJson(data, Stat[][].class);
    }
    private void refreshText() {
        TextView num = findViewById(R.id.textViewNum);
        TextView best = findViewById(R.id.textViewBest);
        TextView avg = findViewById(R.id.textViewAvg);
        num.setText("Сыграно игр: " + stat[curDifficulty][curBoard].numGames);
        best.setText("Лучшее время: " + stat[curDifficulty][curBoard].bestTime);
        avg.setText("Среднее время: " + stat[curDifficulty][curBoard].avgTime);
    }
}
