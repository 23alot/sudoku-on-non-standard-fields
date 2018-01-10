package sudoku;

/**
 * Created by boscatov on 06.12.2017.
 */
public class Cell {
    public byte value;
    public boolean isInput;
    public Cell(){
        this.value = 0;
        this.isInput = false;
    }
}
