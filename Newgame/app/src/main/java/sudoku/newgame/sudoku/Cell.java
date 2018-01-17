package sudoku.newgame.sudoku;

/**
 * Created by boscatov on 06.12.2017.
 */
public class Cell {
    public byte value;
    public byte correctValue;
    boolean[] possibleValues;
    public boolean isInput;
    public Cell(int N){
        this.possibleValues = new boolean[N];
        // TODO: replace to separate method
        for(int i = 0; i < N; ++i)
            possibleValues[i] = false;
        this.value = -1;
        this.isInput = false;
    }
    public boolean isCorrect(){
        return value == correctValue;
    }
}
