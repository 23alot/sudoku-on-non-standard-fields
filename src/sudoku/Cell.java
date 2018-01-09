package sudoku;

/**
 * Created by boscatov on 06.12.2017.
 */
public class Cell {
    public byte value;
    public boolean isInput = false;
    public Cell(byte value){
        this.value = value;
    }
}
