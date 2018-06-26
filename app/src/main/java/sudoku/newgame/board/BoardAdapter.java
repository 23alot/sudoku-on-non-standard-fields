package sudoku.newgame.board;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import sudoku.newgame.R;
import sudoku.newgame.board.possiblevalues.PossibleValuesView;
import sudoku.newgame.board.textview.PenTextView;
import sudoku.newgame.sudoku.Cell;

public class BoardAdapter extends BaseAdapter {
    ArrayList<Cell> array;
    LayoutInflater inflater;
    Context context;
    DisplayMetrics displayMetrics;
    BoardAdapter(Context context, ArrayList<Cell> cells) {
        array = cells;
        inflater = LayoutInflater.from(context);
        this.context = context;
        displayMetrics = context.getResources().getDisplayMetrics();
    }
    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int i) {
        return array.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 1;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null || i == 0) {
            view = inflater.inflate(R.layout.cell, viewGroup, false);
        }
        PenTextView value = view.findViewById(R.id.penText);
        value.setText(array.get(i).value+"");
        value.setTextSize(0.8f * (viewGroup.getWidth() / (float)Math.sqrt(array.size())) / displayMetrics.scaledDensity);
        PossibleValuesView possible = view.findViewById(R.id.possibleCell);
        possible.setupValues(context, 9);
//        ImageView image = view.findViewById(R.id.border);
//        image.setBackgroundResource(R.drawable.horizontal_line);
//        image.setLayoutParams(new RelativeLayout.LayoutParams(2, ViewGroup.LayoutParams.MATCH_PARENT));
        return view;
    }
}
