package sudoku.newgame.datahelpers;

public final class DataConstants {
    public static final int EASY = 13;
    public static final int MEDIUM = 8;
    public static final int HARD = 3;

    public static String getDifficulty(int c) {
        switch(c) {
            case EASY:
                return "easy";
            case MEDIUM:
                return "medium";
            case HARD:
                return "hard";
            default:
                return "error";
        }
    }
}
