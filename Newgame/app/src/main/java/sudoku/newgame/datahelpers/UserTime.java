package sudoku.newgame.datahelpers;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserTime {
    private long time;
    private String username;
    public UserTime() {

    }
    public UserTime(long time, String username) {
        this.time = time;
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null) {
            this.username = currentUser.getDisplayName();
        }
        else {
            this.username = "unknown";
        }
    }
    public void addToDataBase(String dim, String dif) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference leader = databaseReference.child("leaderboard").child(dim).child(dif);
        leader.push().setValue(this);
    }
    public static void addCreationTime(String dim, String dif, long time) {
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
