package sudoku.newgame.board.possiblevalues;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import sudoku.newgame.R;
import sudoku.newgame.sudoku.Cell;

public class PossibleAdapter extends BaseAdapter {
    String[] array;
    LayoutInflater inflater;
    Context context;
    DisplayMetrics displayMetrics;
    PossibleAdapter(Context context, String[] cells) {
        array = cells;
        inflater = LayoutInflater.from(context);
        this.context = context;
        displayMetrics = context.getResources().getDisplayMetrics();
    }
    @Override
    public int getCount() {
        return array.length;
    }

    @Override
    public Object getItem(int i) {
        return array[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null) {
            view = inflater.inflate(R.layout.possible_values, viewGroup, false);
        }
        TextView value = view.findViewById(R.id.possibleText);
        value.setTextSize((0.2f * viewGroup.getWidth()) / displayMetrics.scaledDensity);
        value.setText(array[i]);
        return view;
    }
    @Override
    public boolean areAllItemsEnabled()
    {
        return false;
    }

    @Override
    public boolean isEnabled(int position)
    {
        return false;
    }
}
