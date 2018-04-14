package sudoku.newgame.datahelpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import sudoku.newgame.Stat;

public class CleanData {
    public void cleanStatistic(SharedPreferences sharedPreferences) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String data = sharedPreferences.getString("Array", null);
        Stat[][] stat = gson.fromJson(data, Stat[][].class);
        for(int i = 0; i < stat.length; ++i) {
            for(int z = 0; z < stat[i].length; ++z) {
                stat[i][z].winGames = 0;
                stat[i][z].numGames = 0;
                stat[i][z].avgTime = 0;
                stat[i][z].bestTime = 0;
            }
        }
        editor.putString("Array", gson.toJson(stat));
        editor.apply();
    }
}
