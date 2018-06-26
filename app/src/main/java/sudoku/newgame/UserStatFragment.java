package sudoku.newgame;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import sudoku.newgame.datahelpers.DataConstants;
import sudoku.newgame.datahelpers.TimeHelper;

public class UserStatFragment extends Fragment {
    View fragment;
    private SharedPreferences sharedPreferences;
    private int curDifficulty;
    private int curBoard;
    private Stat[][] stat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragment = inflater.inflate(R.layout.statistics, container, false);
        RadioGroup radioGroupBoard = fragment.findViewById(R.id.radioGroupBoard);
        RadioGroup radioGroupDif = fragment.findViewById(R.id.radioGroupDifficulty);
        sharedPreferences = this.getActivity().getSharedPreferences("Statistics", Context.MODE_PRIVATE);
        setupStatistics();
        radioGroupBoard.setOnCheckedChangeListener(listenerBoard());
        radioGroupDif.setOnCheckedChangeListener(listenerDifficulty());
        refreshText();
        return fragment;
    }

    private RadioGroup.OnCheckedChangeListener listenerBoard() {
        return new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio4:
                        curBoard = 0;
                        break;
                    case R.id.radio5:
                        curBoard = 1;
                        break;
                    case R.id.radio6:
                        curBoard = 2;
                        break;
                    case R.id.radio7:
                        curBoard = 3;
                        break;
                    case R.id.radio8:
                        curBoard = 4;
                        break;
                    case R.id.radio9:
                        curBoard = 5;
                        break;
                    default:
                        break;
                }
                refreshText();
            }
        };
    }

    private RadioGroup.OnCheckedChangeListener listenerDifficulty() {
        return new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioEasy:
                        curDifficulty = 0;
                        break;
                    case R.id.radioMedium:
                        curDifficulty = 1;
                        break;
                    case R.id.radioHard:
                        curDifficulty = 2;
                        break;
                    default:
                        break;
                }
                refreshText();
            }
        };
    }

    private void setupStatistics() {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String data = sharedPreferences.getString("Array", null);
        stat = gson.fromJson(data, Stat[][].class);
        curBoard = 0;
        curDifficulty = 0;
    }

    private void refreshText() {
        TextView num = fragment.findViewById(R.id.textViewNum);
        num.setTextColor(DataConstants.getMainTextColor(((TuturuActivity)getActivity()).theme));
        TextView best = fragment.findViewById(R.id.textViewBest);
        best.setTextColor(DataConstants.getMainTextColor(((TuturuActivity)getActivity()).theme));
        TextView avg = fragment.findViewById(R.id.textViewAvg);
        avg.setTextColor(DataConstants.getMainTextColor(((TuturuActivity)getActivity()).theme));
        TimeHelper timeHelper = new TimeHelper();
        num.setText(getActivity().getString(R.string.num_game)+": " + stat[curDifficulty][curBoard].numGames);
        best.setText(getActivity().getString(R.string.best_time)+": " + timeHelper.millisecondsToTime(stat[curDifficulty][curBoard].bestTime));
        avg.setText(getActivity().getString(R.string.avg_time)+": " + timeHelper.millisecondsToTime(stat[curDifficulty][curBoard].avgTime));
    }
}