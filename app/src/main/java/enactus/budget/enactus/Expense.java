package enactus.budget.enactus;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TableRow;

/**
 * Created by Dominik on 2018-01-25.
 */

public class Expense  extends Activity{

    @Override
    public void onCreate(Bundle savedInstanceState){

        //Set up window
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout( width, height);

        //Program



    }



}
