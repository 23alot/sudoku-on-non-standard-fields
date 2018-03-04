package sudoku.newgame;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;

import sudoku.newgame.datahelpers.BoardBitmap;

/**
 * Created by sanya on 04.03.2018.
 */

public class BoardsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_layout);
        setupBitmap();
    }
    private void setupBitmap() {
        BoardBitmap board = new BoardBitmap();
        Bitmap[] boards = board.getBitmap(this);
        if(boards == null)
            return;
        for(int i = 0; i < boards.length; ++i) {
            String name = "imageView"+(i+1);
            int resID = this.getResources().getIdentifier(name, "id", this.getPackageName());
            ImageView img = findViewById(resID);
            img.setImageBitmap(boards[i]);
        }
    }
}
