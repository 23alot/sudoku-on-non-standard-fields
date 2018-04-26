package sudoku.newgame;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.firebase.ui.auth.data.model.User;
import com.google.firebase.FirebaseError;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import sudoku.newgame.datahelpers.TimeHelper;
import sudoku.newgame.datahelpers.UserTime;

public class LeaderboardActivity extends Activity {
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        loadInfo("4", "easy");
    }
    private void getInfo(UserTime user) {
        TableLayout tableLayout = findViewById(R.id.leaders_table);
        tableLayout.addView(makeRow(user),1);
    }
    private TableRow makeRow(UserTime user)
    {
        TableRow tableRow = new TableRow(getApplicationContext());
        TextView username = new TextView(getApplicationContext());
        username.setText(user.getUsername());
        tableRow.addView(username);
        TextView time = new TextView(getApplicationContext());
        time.setText(TimeHelper.millisecondsToTime(user.getTime()));
        tableRow.addView(time);
        return tableRow;
    }
    private void loadInfo(String dim, String dif) {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        Query leaderboard = databaseReference.child("leaderboard").child(dim).child(dif).orderByValue().limitToLast(20);
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
