package sudoku.newgame.board;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import sudoku.newgame.GameActivity;
import sudoku.newgame.R;
import sudoku.newgame.board.possiblevalues.PossibleValuesView;
import sudoku.newgame.draw.Border;

public class CellView extends FrameLayout {
    int dimension;
    public CellView(Context context) {
        super(context);
    }

    public CellView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        SharedPreferences sharedPreferences = context.getSharedPreferences("Structure", Context.MODE_PRIVATE);
        dimension = sharedPreferences.getInt("Dimension", 9);
    }

    public CellView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CellView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    public CellView(Context context, String value, int dimension, Border border) {
        super(context);
        setupCell(value, null);
    }

    private void setupCell(String value, Border border) {

    }
}
