package sudoku.newgame;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

/**
 * Created by sanya on 20.01.2018.
 */

public class DimensionActivity extends Activity {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();
        setContentView(R.layout.dimension_choose);
        setupButtons();

    }
    void setupButtons(){
        Button bt = findViewById(R.id.button4x4);
        bt.setOnClickListener(setListener(4));
        bt = findViewById(R.id.button5x5);
        bt.setOnClickListener(setListener(5));
        bt = findViewById(R.id.button6x6);
        bt.setOnClickListener(setListener(6));
        bt = findViewById(R.id.button7x7);
        bt.setOnClickListener(setListener(7));
        bt = findViewById(R.id.button8x8);
        bt.setOnClickListener(setListener(8));
        bt = findViewById(R.id.button9x9);
        bt.setOnClickListener(setListener(9));
    }
    View.OnClickListener setListener(final int dimension){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putInt("Dimension", dimension);
                editor.apply();
                Intent intent = new Intent(DimensionActivity.this, GeneratorActivity.class);
                startActivity(intent);
            }
        };
    }
    @Override
    protected void onPause() {
        super.onPause();

    }
}
