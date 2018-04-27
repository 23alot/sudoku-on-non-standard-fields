package sudoku.newgame;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import sudoku.newgame.datahelpers.TimeHelper;
import sudoku.newgame.datahelpers.UserTime;

public class LeaderBoardFragment extends Fragment {
    View fragment;
    private DatabaseReference databaseReference;
    private android.widget.RadioGroup radioGroupDif;
    private android.widget.RadioGroup radioGroupBoard;
    private String curDifficulty;
    private String curBoard;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragment = inflater.inflate(R.layout.activity_leaderboard, container, false);
        radioGroupBoard = fragment.findViewById(R.id.radioGroupBoard);
        radioGroupDif = fragment.findViewById(R.id.radioGroupDifficulty);
        curBoard = "4";
        curDifficulty = "easy";
        loadInfo(curBoard,curDifficulty);
        radioGroupBoard.setOnCheckedChangeListener(listenerBoard());
        radioGroupDif.setOnCheckedChangeListener(listenerDifficulty());
        return fragment;
    }
    private RadioGroup.OnCheckedChangeListener listenerBoard() {
        return new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch (checkedId)
                {
                    case R.id.radio4:
                        curBoard = "4";
                        break;
                    case R.id.radio5:
                        curBoard = "5";
                        break;
                    case R.id.radio6:
                        curBoard = "6";
                        break;
                    case R.id.radio7:
                        curBoard = "7";
                        break;
                    case R.id.radio8:
                        curBoard = "8";
                        break;
                    case R.id.radio9:
                        curBoard = "9";
                        break;
                    default:
                        break;
                }
                loadInfo(curBoard, curDifficulty);
            }
        };
    }
    private RadioGroup.OnCheckedChangeListener listenerDifficulty() {
        return new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch (checkedId)
                {
                    case R.id.radioEasy:
                        curDifficulty = "easy";
                        break;
                    case R.id.radioMedium:
                        curDifficulty = "medium";
                        break;
                    case R.id.radioHard:
                        curDifficulty = "hard";
                        break;
                    default:
                        break;
                }
                loadInfo(curBoard+"", curDifficulty);
            }
        };
    }

    private void getInfo(UserTime user) {
        if(getActivity() == null) {
            return;
        }
        TableLayout tableLayout = fragment.findViewById(R.id.leaders_table);
        Log.d("LeaderBoardFragmentinfo",user.getUsername());
        tableLayout.addView(makeRow(user));
    }
    private TableRow makeRow(UserTime user)
    {
        TableRow tableRow = new TableRow(getActivity());
        TableRow.LayoutParams params = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tableRow.setLayoutParams(params);
        tableRow.setBackgroundResource(R.drawable.table_border);
        TextView username = new TextView(getActivity());
        params.weight = 2.5f;
        username.setLayoutParams(params);
        username.setGravity(Gravity.CENTER);
        username.setTextSize(20);
        username.setPadding(5,5,5,5);
        username.setText(user.getUsername());
        tableRow.addView(username);
        TextView time = new TextView(getActivity());
        params.weight = 0.5f;
        time.setLayoutParams(params);
        time.setGravity(Gravity.CENTER);
        time.setTextSize(20);
        time.setText(TimeHelper.millisecondsToTime(user.getTime()));
        tableRow.addView(time);
        return tableRow;
    }
    private void loadInfo(String dim, String dif) {
        TableLayout tableLayout = fragment.findViewById(R.id.leaders_table);
        tableLayout.removeAllViews();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        Query leaderboard = databaseReference.child("leaderboard").child(dim).child(dif).orderByChild("time").limitToLast(20);
        leaderboard.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                UserTime user = dataSnapshot.getValue(UserTime.class);
                getInfo(user);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }


        });
    }
}
