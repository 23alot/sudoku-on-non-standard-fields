package sudoku.newgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import sudoku.newgame.R;
import sudoku.newgame.datahelpers.TimeHelper;

/**
 * Created by sanya on 06.03.2018.
 */

public class CongratulationActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.congratulations);
        Intent intent = getIntent();
        long time = intent.getLongExtra("Time", 0);
        TextView text = findViewById(R.id.gameTime);
        TimeHelper timeHelper = new TimeHelper();
        text.setText(timeHelper.millisecondsToTime(time));
    }
}
