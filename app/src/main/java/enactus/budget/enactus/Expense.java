package enactus.budget.enactus;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TableRow;

import android.widget.Toast;

import org.javatuples.Quintet;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Dominik on 2018-01-25.
 */

public class Expense  extends Activity{
    private double taxRate = 1.13;
    private String expenseStr, category ;
    private double subtotal, expense , total;
    private ArrayList<Quintet> tuples;
    private int number;
    @Override
    public void onCreate(Bundle savedInstanceState){


        //Set   up window
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense);
        expense = 0;
        total = 0;

        //Configure drop down menu for categories
        //final Spinner spinner =  findViewById(R.id.spinner);

        String[] categories = new String[]{
                "Category",
                "Cat1",
                "Cat2",
                "Cat3"

        };
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
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

        //adapter.setDropDownViewResource(R.layout.spinner_item);
        //spinner.setAdapter(adapter);


        /*

        */
        //---------------------------------------------------------------

        //get expense value
        Button btnadd = findViewById(R.id.btnadd);
        final Button btnconfirm = findViewById(R.id.btnconfirm);
        final Button btnenter = findViewById(R.id.btnenter);
        final TextView expenseoutput = findViewById(R.id.expenseoutput);
        final EditText expenseinput = findViewById(R.id.expenseinput);
        final EditText num = findViewById(R.id.num);




        //display expense value * tax


        /***
         *
         * Im planning on replacing this button with just an edit text text change listener b
         */
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expense= Double.parseDouble(expenseinput.getText().toString());

                //number validation
                if( num.getText().toString().equals("")){
                    number = 1;
                }
                else{
                    number = Integer.parseInt(num.getText().toString());
                }
                subtotal = Math.round(number * expense * 100.0) / 100.0;
                Log.d("CALCULATE", String.format("%.2f",subtotal));
                expenseStr = "$ " + String.format("%.2f",subtotal);
                expenseoutput.setText(expenseStr);
            }
        });


        //Enter expense into total and reset expense
        btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final TableLayout entries = findViewById(R.id.entries);
                final TableRow newEntry = new TableRow(btnconfirm.getContext());


                //show sub total
                TextView txt = new TextView(btnconfirm.getContext());
                String entry = number + " x $" + expense;
                txt.setText(entry);
                newEntry.addView(txt);


                TextView txt2 = new TextView(btnconfirm.getContext());
                String strsubtotal = "$" + subtotal;
                txt2.setText(strsubtotal);
                newEntry.addView(txt2);


                //set up subtotal category selection
                Spinner newspin = new Spinner(btnconfirm.getContext());
                newspin.setAdapter(adapter);
                newEntry.addView(newspin);
                newspin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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



                //remove button
                final Button newremove = new Button(btnconfirm.getContext());
                newremove.setText("X");
                newremove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        entries.removeView(newEntry);
                        TextView temp = (TextView) newEntry.getChildAt(1);
                        String str1 = temp.getText().toString().substring(1);
                        total -= Double.parseDouble(str1);
                        TextView totaltext = findViewById(R.id.total);
                        totaltext.setText("Total: $" + total);


                        expense = 0.0;
                        subtotal = 0.0;
                    }
                });
                newEntry.addView(newremove);

                entries.addView(newEntry);

                total += subtotal;
                //clear out previous
                num.setText("");
                expenseinput.setText("");
                expenseoutput.setText("");
                expense = 0.0;
                subtotal = 0.0;

                TextView totalstr = findViewById(R.id.total);
                totalstr.setText("Total: $" + total);

                /***
                 *
                 *
                 * FOR DATABASE, TUPLES WILL HOLD INFORMATION
                 *
                 *
                 *
                 */
                Quintet<Date, Double, Integer, String, String> tuple;
            }
        });


/*
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

        */
    }




}
