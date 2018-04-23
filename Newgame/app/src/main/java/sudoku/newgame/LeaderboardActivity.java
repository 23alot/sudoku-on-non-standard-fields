package sudoku.newgame;

import android.app.Activity;
import android.os.Bundle;

import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class LeaderboardActivity extends Activity {
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        Query leaderboard = databaseReference.child("leaderboard").child("7").child("easy").orderByValue().limitToFirst(20);
    }
}
