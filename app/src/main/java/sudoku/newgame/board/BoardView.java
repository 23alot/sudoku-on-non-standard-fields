package sudoku.newgame.board;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;

import sudoku.newgame.sudoku.Board;
import sudoku.newgame.sudoku.Cell;

public class BoardView extends GridView {
    int dimension;
    ArrayList<Cell> arrayList;
    public BoardView(Context context) {
        super(context);
    }

    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BoardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BoardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public BoardView(Context context, int dimension, Board board) {
        super(context);
        this.setNumColumns(9);
        this.dimension = dimension;
        createBoard(null, context, this);
    }
    private void createBoard(Board board, Context context, View v) {
        arrayList = new ArrayList<>();
        String[] inputValues = new String[dimension*dimension];
        for(int i = 0; i < dimension; ++i) {
            for(int z = 0; z < dimension; ++z) {
                inputValues[dimension*i+z] = "5";//board.cells[i][z].isInput?"":board.cells[i][z].value+"";
                arrayList.add(new Cell(9, (byte)5));
            }
        }
        this.setPadding(2, 2, 2, 2);
        this.setAdapter(new BoardAdapter(context, arrayList));
    }
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
