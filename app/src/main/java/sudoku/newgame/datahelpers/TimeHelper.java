package sudoku.newgame.datahelpers;

public class TimeHelper {
    public static String millisecondsToTime(long time) {
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
        else if(minutes < 10){
            result += "0" + minutes + ":";
        }
        else {
            result += minutes + ":";
        }
        if(seconds == 0) {
            result += "00";
        }
        else if(seconds < 10){
            result += "0" + seconds;
        }
        else {
            result += seconds;
        }

        return result;
    }
}
