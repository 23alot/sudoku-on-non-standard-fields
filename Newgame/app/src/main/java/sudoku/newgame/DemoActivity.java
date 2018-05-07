package sudoku.newgame;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.MobileAds;

import sudoku.newgame.dancinglinks.Algorithm;
import sudoku.newgame.dancinglinks.Structure;
import sudoku.newgame.datahelpers.SecretKeys;
import sudoku.newgame.sudoku.Board;

public class DemoActivity extends Activity {
    int[] game;
    Board board;
    int n;
    DrawView dw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    private void demoCreation(byte[] area, byte dim, int dif, byte n) {
        Algorithm algorithm = new Algorithm(new Structure(n, area));
        board = algorithm.create(dif-3, dif+7, area, dim*dim);
        algorithm = new Algorithm(board);
        game = algorithm.demoSolve();
    }

    //Set all pencil values
    private void firstStep() {
        dw.board.setBasicPencilValues();
    }

    // Place first pen value
    private void secondStep() {
        int a = game[0];
        int row = (a / (n*n));
        int column = (a / n) % n;

    }
    private void game() {
//        for(int a: result.solution){
//            int row = (a / (structure.N * structure.N));
//            int column = ((a / structure.N) % structure.N);
//            answer[row][column] = (byte) (a % structure.N + 1);
//        }
    }
}
