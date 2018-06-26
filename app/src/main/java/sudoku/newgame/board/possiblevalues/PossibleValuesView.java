package sudoku.newgame.board.possiblevalues;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import sudoku.newgame.R;

public class PossibleValuesView extends GridView {
    int dimension;
    PossibleAdapter adapter;
    public PossibleValuesView(Context context) {
        super(context);
    }

    public PossibleValuesView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PossibleValuesView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PossibleValuesView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    public PossibleValuesView(Context context, boolean[] values) {
        super(context);
        this.dimension = values.length;

    }
    public void setupValues(Context context, int dimension) {
        this.setNumColumns(3);
        this.setHorizontalSpacing(0);
        this.setVerticalSpacing(0);
        this.dimension = dimension;
        String[] arr = loadValues();
        adapter = new PossibleAdapter(context, arr);
        this.setAdapter(adapter);
    }
    private String[] loadValues() {
        switch (dimension) {
            case 4:
                return new String[]{"1", null, "2",
                                    null, null, null,
                                    "3", null, "4"};
            case 5:
                return new String[]{"1", null, "2",
                                    null, "3", null,
                                    "4", null, "5"};
            case 6:
                return new String[]{"1", "2", "3",
                        null, null, null,
                        "4", "5", "6"};
            case 7:
                return new String[]{"1", "2", "3",
                        null, "4", null,
                        "5", "6", "7"};
            case 8:
                return new String[]{"1", "2", "3",
                        "4", null, "5",
                        "6", "7", "8"};
            default:
                return new String[]{"1", "2", "3",
                        "4", "5", "6",
                        "7", "8", "9"};
        }
    }
    private int getPosition(int value) {
        switch(dimension) {
            case 4:
                switch (value) {
                    case 1:
                        return 0;
                    case 2:
                        return 2;
                    case 3:
                        return 6;
                    default:
                        return 8;
                }
            case 5:
                switch (value) {
                    case 1:
                        return 0;
                    case 2:
                        return 2;
                    case 3:
                        return 4;
                    case 4:
                        return 6;
                    default:
                        return 8;
                }
            case 6:
                switch (value) {
                    case 1:
                        return 0;
                    case 2:
                        return 1;
                    case 3:
                        return 2;
                    case 4:
                        return 6;
                    case 5:
                        return 7;
                    default:
                        return 8;
                }
            case 7:
                switch (value) {
                    case 1:
                        return 0;
                    case 2:
                        return 1;
                    case 3:
                        return 2;
                    case 4:
                        return 4;
                    case 5:
                        return 6;
                    case 6:
                        return 7;
                    default:
                        return 8;
                }
            case 8:
                switch (value) {
                    case 1:
                        return 0;
                    case 2:
                        return 1;
                    case 3:
                        return 2;
                    case 4:
                        return 3;
                    case 5:
                        return 5;
                    case 6:
                        return 6;
                    case 7:
                        return 7;
                    default:
                        return 8;
                }
            default:
                return value-1;
        }
    }
    private void changePossible(int position, boolean visible) {
        View view = this.getChildAt(position);
        if (view != null) {
            TextView text = view.findViewById(R.id.possibleText);
            text.setVisibility(visible ? VISIBLE : INVISIBLE);
        }
    }
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
