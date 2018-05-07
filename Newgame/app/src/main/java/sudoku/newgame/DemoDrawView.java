package sudoku.newgame;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import sudoku.newgame.draw.DrawBoard;
import sudoku.newgame.sudoku.Board;

public class DemoDrawView extends View {
    boolean isDone = false;
    Point size;
    volatile Board bd;
    public DrawBoard board = null;
    Canvas canvas;
    byte[] area = null;
    SharedPreferences sharedPreferences;
    int w;
    int h;
    int n;
    Paint p;
    Context context;
    public DemoDrawView(Context context){
        super(context);
        this.context = context;
        sharedPreferences = context.getSharedPreferences("Structure", Context.MODE_PRIVATE);
        n = sharedPreferences.getInt("Dimension",9);
        size = new Point();
        p = new Paint();

    }
    public DemoDrawView(Context context, AttributeSet attrs) {
        super(context,attrs);
        this.context = context;
        sharedPreferences = context.getSharedPreferences("Structure", Context.MODE_PRIVATE);
        n = sharedPreferences.getInt("Dimension",9);
        size = new Point();
        p = new Paint();
    }

    public DemoDrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        sharedPreferences = context.getSharedPreferences("Structure", Context.MODE_PRIVATE);
        n = sharedPreferences.getInt("Dimension",9);
        size = new Point();
        p = new Paint();
    }

    public DemoDrawView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        sharedPreferences = context.getSharedPreferences("Structure", Context.MODE_PRIVATE);
        n = sharedPreferences.getInt("Dimension",9);
        size = new Point();
        p = new Paint();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        p.setColor(Color.BLACK);

        //boolean newGame = sharedPreferences.getBoolean("New game",false);

        if(board == null) {
            creation();
        }
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if(board.board[0][0].length != (h - 2*10) / n) {
                board.changeLength((h - 2*10) / n);
            }

        }
        else if(board.board[0][0].length != (w-2*10) / n) {
            board.changeLength((w - 2 * 10) / n);
        }

        board.draw(canvas, p);
    }
    private void creation() {

    }
}
