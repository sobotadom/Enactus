package enactus.budget.enactus;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TableRow;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private double taxRate = 1.13, expense = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addExpense();
            }
        });




        final TableLayout tl = findViewById(R.id.tableLayout);

        /*
        //calculate tax on expense input
        btncalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Log.i("btncalc","User has put in valid expense, displaying tax");
                double taxAmout = Double.parseDouble(taxInput.getText().toString()) * 0.13;
                expense = Math.floor(100 * taxRate * Double.parseDouble(taxInput.getText().toString())) / 100 ;
                taxOutput.setText("$" + String.format ("%.2f", expense));
                btnadd.setVisibility(View.VISIBLE);
            }
        });

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        */
    }
    public void addExpense(){
        /*
        TableRow tr = new TableRow(this);
        TableLayout tl = findViewById(R.id.tableLayout);

        TextView category = new TextView(this);
        category.setText("Category");
        TextView cost = new TextView(this);
        cost.setText(Double.toString(expense));


        tr.addView(category);
        tr.addView(cost);
        tl.addView(tr);
        */
        startActivity(new Intent(MainActivity.this, Expense.class));

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
