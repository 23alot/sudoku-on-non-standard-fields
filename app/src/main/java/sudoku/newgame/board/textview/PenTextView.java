package sudoku.newgame.board.textview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

public class PenTextView extends android.support.v7.widget.AppCompatTextView {
    public PenTextView(Context context) {
        super(context);
    }

    public PenTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PenTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
