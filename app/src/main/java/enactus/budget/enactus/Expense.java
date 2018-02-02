package enactus.budget.enactus;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TableRow;
import android.widget.Toast;

/**
 * Created by Dominik on 2018-01-25.
 */

public class Expense  extends Activity{
    private double taxRate = 1.13;
    private String expenseStr;
    private double expense;
    @Override
    public void onCreate(Bundle savedInstanceState){


        //Set   up window
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense);
        expense = 0;

        //Configure drop down menu for categories
        final Spinner spinner =  findViewById(R.id.spinner);

        String[] categories = new String[]{
                "Select a category...",
                "Cat1",
                "Cat2",
                "Cat3",
                "Cat4"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,R.layout.spinner_item,categories){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if(position > 0){
                    // Notify the selected item text

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //---------------------------------------------------------------

        //get expense value
        Button btnadd = findViewById(R.id.btnadd);
        final Button btnenter = findViewById(R.id.btnenter);
        final TextView expenseoutput = findViewById(R.id.expenseoutput);
        final EditText expenseinput = findViewById(R.id.expenseinput);

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //display expense value * tax
                String inputtext = expenseinput.getText().toString();

                /*****
                 * MICHEAL CAN YOU DO INPUT VALIDATION HERE
                 */

                double input = Double.parseDouble(inputtext) * taxRate;
                expense = Math.round(input * 100.0) / 100.0;
                Log.d("CALCULATE", String.format("%.2f",expense));
                expenseStr = "$ " + String.format("%.2f",expense);
                expenseoutput.setText(expenseStr);


            }
        });

        btnenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //ensure a category is seleceted
                if ((!spinner.getSelectedItem().toString().equals("Select a category..."))
                       && (expense != 0)){
                    Intent intent = new Intent();
                    Bundle b = new Bundle();

                    b.putDouble("Expense",expense);
                    b.putString("STR",expenseStr);
                    b.putString("category",spinner.getSelectedItem().toString());
                    intent.putExtra("EXPENSE",b);
                    setResult(RESULT_OK,intent);
                    finish();
                }
                else {
                    //prompt user to select a category

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(btnenter.getContext());
                    if(spinner.getSelectedItem().toString().equals("Select a category...")){
                        builder1.setMessage("Please select a category");
                    }
                    if( expense == 0){
                        builder1.setMessage("Please enter an expense");
                    }
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });


                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                }

            }
        });


    }





}
