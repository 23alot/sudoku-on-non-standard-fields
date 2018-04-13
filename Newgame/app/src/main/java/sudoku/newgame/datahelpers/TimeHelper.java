package sudoku.newgame.datahelpers;

public class TimeHelper {
    public String millisecondsToTime(long time) {
        String result = "";
        int hours = (int)(time / 1000 / 60 / 60);
        int minutes = (int)(time / 1000 / 60 % 60);
        int seconds = (int)(time / 1000 % 60);
        if(hours != 0) {
            result += hours + ":";
        }
        if(minutes == 0) {
            result += "00:";
        }
        else {
            result += minutes + ":";
        }
        if(seconds == 0) {
            result += "00";
        }
        else {
            result += seconds;
        }

        return result;
    }
}
