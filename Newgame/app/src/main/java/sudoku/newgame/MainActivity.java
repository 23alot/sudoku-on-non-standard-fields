package sudoku.newgame;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
    private Button mbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(new DrawView(this));
        setContentView(R.layout.main);
        System.out.println(1);
        final DrawView drawView = findViewById(R.id.drawView);
        final EditText time = findViewById(R.id.editText);
        String ss = ((Integer)((-1)%9)).toString();
        time.setText(ss);
        mbutton = (Button)findViewById(R.id.button4);
        View.OnClickListener oclmbutton = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Long start = System.currentTimeMillis();
                drawView.drawThread.run();
                Long finish = System.currentTimeMillis();
                String s = ((Long)(finish-start)).toString();
                time.setText(s);
            }
        };
        mbutton.setOnClickListener(oclmbutton);
    }

}