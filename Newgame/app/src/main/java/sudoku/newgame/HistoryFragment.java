package sudoku.newgame;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HistoryFragment extends Fragment {
    History history;
    public boolean active = false;
    View fragment;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragment = inflater.inflate(R.layout.fragment_history, container, false);
        TextView text = fragment.findViewById(R.id.historyList);
        StringBuilder result = new StringBuilder();

        for(Event e: history.history) {
            result.append(e);
            result.append("\n");
        }
        text.setText(result);
        return fragment;
    }
    public void setHistory(History history) {
        this.history = history;
    }
}
