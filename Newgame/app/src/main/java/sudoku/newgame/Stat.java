package sudoku.newgame;

/**
 * Created by sanya on 08.03.2018.
 */

public class Stat {
    public long numGames;
    public long avgTime;
    public long bestTime;
    public long winGames;
    public Stat(long num, long avg, long best, long win) {
        numGames = num;
        avgTime = avg;
        bestTime = best;
        winGames = win;
    }
}

