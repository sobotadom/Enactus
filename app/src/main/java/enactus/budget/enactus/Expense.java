package enactus.budget.enactus;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;

/**
 * Created by Dominik on 2018-01-25.
 */

public class Expense  extends Activity{

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout( (int)(width * 0.6), (int)(height * 0.8));

    }



}
