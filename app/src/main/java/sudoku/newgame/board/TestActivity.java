package sudoku.newgame.board;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import sudoku.newgame.R;


public class TestActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);
        LinearLayout linearLayout = findViewById(R.id.test_layout);
        BoardView board = new BoardView(this, 9, null);
        linearLayout.addView(board);
    }
}
