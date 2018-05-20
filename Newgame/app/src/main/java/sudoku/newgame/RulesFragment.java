package sudoku.newgame;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import sudoku.newgame.datahelpers.DataConstants;

public class RulesFragment extends Fragment {
    public boolean isActive = false;
    View fragment;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragment = inflater.inflate(R.layout.fragment_rules, container, false);
        fragment.findViewById(R.id.rules_layout).setBackgroundColor(DataConstants.getBackgroundColor(((GameActivity)getActivity()).theme));
        ((TextView)fragment.findViewById(R.id.text_rules)).setTextColor(DataConstants.getMainTextColor(((GameActivity)getActivity()).theme));
        fragment.findViewById(R.id.demo_game).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DemoActivity.class);
                startActivity(intent);
            }
        });
        ((TextView)fragment.findViewById(R.id.demo_game)).setTextColor(DataConstants.getMainTextColor(((GameActivity)getActivity()).theme));
        return fragment;
    }
}
