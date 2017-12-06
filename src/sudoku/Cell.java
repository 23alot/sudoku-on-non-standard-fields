package sudoku;

/**
 * Created by boscatov on 06.12.2017.
 */
public class Cell {
    byte value;
    byte area;
    public Cell(byte value,byte area){
        this.value = value;
        this.area = area;
    }
}
