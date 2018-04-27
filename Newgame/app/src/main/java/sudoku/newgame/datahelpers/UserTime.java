package sudoku.newgame.datahelpers;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserTime {
    long time;
    String username;
    public UserTime() {

    }
    public UserTime(long time, String username) {
        this.time = time;
        this.username = username;
    }
    public void addToDataBase(String dim, String dif) {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference leader = databaseReference.child("leaderboard").child(dim).child(dif);
        leader.push().setValue(this);
    }
    public static void addCreationTime(String dim, String dif, long time) {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference creation = databaseReference.child("generation_time").child(dim).child(dif);
        creation.push().setValue(time);
    }
    public String getUsername() {
        return username;
    }
    public long getTime() {
        return time;
    }
}
