package sudoku.newgame;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import sudoku.newgame.datahelpers.DataConstants;

public class HistoryFragment extends Fragment {
    History history;
    public boolean isActive = false;
    View fragment;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragment = inflater.inflate(R.layout.fragment_history, container, false);
        LinearLayout ll = fragment.findViewById(R.id.fragment_history);
        ll.setBackgroundColor(DataConstants.getBackgroundColor(((GameActivity)getActivity()).theme));
        TextView text = fragment.findViewById(R.id.historyList);
        StringBuilder result = new StringBuilder();

        for(Event e: history.history) {
            result.append(e);
            result.append("\n");
        }
        text.setTextColor(DataConstants.getMainTextColor(((GameActivity)getActivity()).theme));
        text.setText(result);
        text = fragment.findViewById(R.id.history_header);
        text.setTextColor(DataConstants.getMainTextColor(((GameActivity)getActivity()).theme));
        return fragment;
    }
    public void setHistory(History history) {
        this.history = history;
    }
}
