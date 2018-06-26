package sudoku.newgame;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import sudoku.newgame.datahelpers.DataConstants;
import sudoku.newgame.datahelpers.TimeHelper;

public class CongratulationFragment extends Fragment {
    public boolean isActive = false;
    View fragment;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragment = inflater.inflate(R.layout.fragment_congratulations, container, false);
        fragment.findViewById(R.id.gratz_layout).setBackgroundColor(DataConstants.getBackgroundColor(((GameActivity)getActivity()).theme));
        Button button = fragment.findViewById(R.id.newGame);
        button.setTextColor(DataConstants.getMainTextColor(((GameActivity)getActivity()).theme));
        button.setBackgroundColor(DataConstants.getBackgroundColor(((GameActivity)getActivity()).theme));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View v = getActivity().findViewById(R.id.drawView);
                ((GameActivity)getActivity()).pw.showAtLocation(v, Gravity.BOTTOM, 0 , 0);
            }
        });
        long time = ((GameActivity)getActivity()).winTime;
        int difficulty = ((GameActivity)getActivity()).winDif;
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("Statistics", Context.MODE_PRIVATE);
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String data = sharedPreferences.getString("Array", null);
        Stat[][] stat = gson.fromJson(data, Stat[][].class);
        TextView text = fragment.findViewById(R.id.gameTime);
        text.setText(getActivity().getString(R.string.time)+": "+TimeHelper.millisecondsToTime(time));
        text.setTextColor(DataConstants.getMainTextColor(((GameActivity)getActivity()).theme));
        text = fragment.findViewById(R.id.congratulationsText);
        text.setText(getActivity().getString(R.string.gratz));
        text.setTextColor(DataConstants.getMainTextColor(((GameActivity)getActivity()).theme));
        text = fragment.findViewById(R.id.difficulty);
        text.setTextColor(DataConstants.getMainTextColor(((GameActivity)getActivity()).theme));
        text.setText(getActivity().getString(R.string.difficulty)+": "+DataConstants.getDifficulty(difficulty, getActivity()));
        text = fragment.findViewById(R.id.bestTime);
        text.setTextColor(DataConstants.getMainTextColor(((GameActivity)getActivity()).theme));
        int dif;
        if(difficulty == DataConstants.EASY) {
            dif = 0;
        }
        else if(difficulty == DataConstants.MEDIUM) {
            dif = 1;
        }
        else {
            dif = 2;
        }
        sharedPreferences = this.getActivity().getSharedPreferences("Structure", Context.MODE_PRIVATE);
        text.setText(getActivity().getString(R.string.best_time)+": "+TimeHelper.millisecondsToTime(stat[dif][sharedPreferences.getInt("Dimension", 9)-4].bestTime));
        return fragment;
    }
}
