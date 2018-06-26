package sudoku.newgame.board;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import sudoku.newgame.R;
import sudoku.newgame.board.possiblevalues.PossibleValuesView;
import sudoku.newgame.sudoku.Cell;

public class CellAdapter extends BaseAdapter {
    LayoutInflater inflater;
    Cell cell;
    Context context;
    CellAdapter(Context context, Cell cell)
    {
        inflater = LayoutInflater.from(context);
        this.cell = cell;
        this.context = context;
    }
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null) {
            view = inflater.inflate(R.layout.cell, viewGroup, false);
            PossibleValuesView possible = view.findViewById(R.id.possibleCell);
            possible.setupValues(context, 9);
        }
        return view;
    }
}
