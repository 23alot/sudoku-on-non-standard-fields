package sudoku.newgame;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class RulesFragment extends Fragment {
    public boolean active = false;
    View fragment;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragment = inflater.inflate(R.layout.fragment_rules, container, false);
        Button bt = fragment.findViewById(R.id.demo_game);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DemoActivity.class);
                startActivity(intent);
            }
        });
        return fragment;
    }
}
