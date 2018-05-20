package sudoku.newgame;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import sudoku.newgame.datahelpers.DataConstants;
import sudoku.newgame.datahelpers.TimeHelper;

public class DimensionFragment extends Fragment {
    public boolean isActive = false;
    View fragment;
    int theme = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragment = inflater.inflate(R.layout.dimension_choose, container, false);
        theme = ((BoardsActivity)getActivity()).theme;
        fragment.findViewById(R.id.dimension_layout).setBackgroundColor(DataConstants.getBackgroundColor(theme));
        setupButtons();
        return fragment;
    }
    void setupButtons(){
        Button bt = fragment.findViewById(R.id.button4x4);
        bt.setOnClickListener(setListener(4));
        bt.setBackgroundColor(DataConstants.getBackgroundColor(theme));
        bt.setTextColor(DataConstants.getMainTextColor(theme));
        bt = fragment.findViewById(R.id.button5x5);
        bt.setOnClickListener(setListener(5));
        bt.setBackgroundColor(DataConstants.getBackgroundColor(theme));
        bt.setTextColor(DataConstants.getMainTextColor(theme));
        bt = fragment.findViewById(R.id.button6x6);
        bt.setOnClickListener(setListener(6));
        bt.setBackgroundColor(DataConstants.getBackgroundColor(theme));
        bt.setTextColor(DataConstants.getMainTextColor(theme));
        bt = fragment.findViewById(R.id.button7x7);
        bt.setOnClickListener(setListener(7));
        bt.setBackgroundColor(DataConstants.getBackgroundColor(theme));
        bt.setTextColor(DataConstants.getMainTextColor(theme));
        bt = fragment.findViewById(R.id.button8x8);
        bt.setOnClickListener(setListener(8));
        bt.setBackgroundColor(DataConstants.getBackgroundColor(theme));
        bt.setTextColor(DataConstants.getMainTextColor(theme));
        bt = fragment.findViewById(R.id.button9x9);
        bt.setOnClickListener(setListener(9));
        bt.setBackgroundColor(DataConstants.getBackgroundColor(theme));
        bt.setTextColor(DataConstants.getMainTextColor(theme));
    }
    View.OnClickListener setListener(final int dimension){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((BoardsActivity)getActivity()).dimension = dimension;
                closefragment();
            }
        };
    }
    private void closefragment() {
        ((BoardsActivity)getActivity()).generator();
        isActive = false;
        getActivity().getFragmentManager().beginTransaction().remove(this).commit();
    }
}
