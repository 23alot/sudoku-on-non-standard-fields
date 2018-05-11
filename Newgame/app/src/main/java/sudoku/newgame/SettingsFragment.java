package sudoku.newgame;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import sudoku.newgame.datahelpers.DataConstants;
import sudoku.newgame.firebaseauth.ChooserActivity;

public class SettingsFragment extends Fragment {
    public boolean isActive = false;
    View fragment;
    SharedPreferences sp;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragment = inflater.inflate(R.layout.fragment_settings, container, false);
        sp = getActivity().getSharedPreferences("Structure", Context.MODE_PRIVATE);

        ToggleButton tbut = fragment.findViewById(R.id.theme);
        int theme = sp.getInt("Theme",0);
        if(theme == DataConstants.DARK) {
            tbut.setChecked(false);
        }
        else {
            tbut.setChecked(true);
        }
        changeTheme(theme);
        Button bt = fragment.findViewById(R.id.login_button);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChooserActivity.class);
                startActivity(intent);
//                finish();
            }
        });
        tbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sp.edit();
                if(((ToggleButton)view).isChecked()) {
                    editor.putInt("Theme", DataConstants.LIGHT);
                    changeTheme(DataConstants.LIGHT);
                }
                else {
                    editor.putInt("Theme", DataConstants.DARK);
                    changeTheme(DataConstants.DARK);
                }
                editor.apply();
            }
        });
        return fragment;
    }
    private void changeTheme(int theme) {
        LinearLayout ll = fragment.findViewById(R.id.fragment_settings);
        ll.setBackgroundColor(DataConstants.getBackgroundColor(theme));
        Button bt = fragment.findViewById(R.id.login_button);
        bt.setTextColor(DataConstants.getMainTextColor(theme));
        bt.setBackgroundColor(DataConstants.getBackgroundColor(theme));
        ToggleButton tbut = fragment.findViewById(R.id.theme);
        tbut.setTextColor(DataConstants.getMainTextColor(theme));
        tbut.setBackgroundColor(DataConstants.getBackgroundColor(theme));
    }
}
