package sudoku.newgame.datahelpers;

import android.content.Context;
import android.graphics.Color;

import sudoku.newgame.R;

public final class DataConstants {
    // Difficulty constants
    public static final int EASY = 13;
    public static final int MEDIUM = 8;
    public static final int HARD = 3;
    // Theme constants
    public static final int LIGHT = 0;
    public static final int DARK = 1;

    public static String getDifficulty(int c, Context context) {
        switch(c) {
            case EASY:
                return context.getString(R.string.new_easy);
            case MEDIUM:
                return context.getString(R.string.new_medium);
            case HARD:
                return context.getString(R.string.new_hard);
            default:
                return "error";
        }
    }
    public static int getBackgroundColor(int theme) {
        if(theme == DARK) {
            return Color.rgb(5, 14, 22);
        }
        else {
            return Color.rgb(240,240,240);
        }
    }
    public static int getAreaColor(int theme) {
        if(theme == DARK) {
            return Color.rgb(15,20,23);
        }
        else {
            return Color.rgb(153,204,255);
        }
    }
    public static int getHeaderColor(int theme) {
        if(theme == DARK) {
            return Color.rgb(0, 9, 17);
        }
        else {
            return Color.rgb(219,219,219);
        }
    }
    public static int getSameColor(int theme) {
        if(theme == DARK) {
            return Color.rgb(56, 84, 71);
        }
        else {
            return Color.rgb(131, 196, 167);
        }
    }
    public static int getErrorTextColor(int theme) {
        if(theme == DARK) {
            return Color.rgb(160, 51, 51);
        }
        else {
            return Color.rgb(196, 62, 62);
        }
    }
    public static int getErrorFillColor(int theme) {
        if(theme == DARK) {
            return Color.rgb(165, 91, 91);
        }
        else {
            return Color.rgb(211, 116, 116);
        }
    }
    public static int getFillColor(int theme) {
        if(theme == DARK) {
            return Color.rgb(15, 24, 32);
        }
        else {
            return Color.WHITE;
        }
    }
    public static int getMainTextColor(int theme) {
        if(theme == DARK) {
            return Color.rgb(120, 123, 126);
        }
        else {
            return Color.rgb(27, 27, 30);
        }
    }
    public static int getPenTextColor(int theme) {
        if(theme == DARK) {
            return Color.rgb(53, 95, 117);
        }
        else {
            return Color.rgb(76, 88, 181);
        }
    }
    public static int getHelpTextColor(int theme) {
        if(theme == DARK) {
            return Color.rgb(53, 95, 117);
        }
        else {
            return Color.rgb(161, 177, 196);
        }
    }
    public static int getTouchColor(int theme) {
        if(theme == DARK) {
            return Color.GRAY;
        }
        else {
            return Color.rgb(111, 162, 193);
        }
    }
}
