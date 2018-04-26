package sudoku.newgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import sudoku.newgame.firebaseauth.ChooserActivity;

public class SettingsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Button bt = findViewById(R.id.login_button);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, ChooserActivity.class);
                startActivity(intent);
                finish();
            }
        });
        bt = findViewById(R.id.leaderboards);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, LeaderboardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
