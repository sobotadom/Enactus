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
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Dominik on 2018-01-25.
 */

public class Expense  extends Activity{
    private double taxRate = 1.13;
    private String expenseStr, category, comment;
    private double subtotal, expense , total;
    private ArrayList<Quintet<Date,Double,Integer,String,String>> tuples;
    private int number, finalindex;
    private boolean isValidExpense;
    @Override
    public void onCreate(Bundle savedInstanceState){

        tuples = new ArrayList<>();
        //Set   up window
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense);
        expense = 0;
        total = 0;
        finalindex = 0;
        category = "Category";
        isValidExpense = false;


        /**
         *      Configure drop down menu for categories
         */



        final String[] categories = new String[]{
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
        final Spinner spin = findViewById(R.id.spinner);
        spin.setAdapter(adapter);

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if(position > 0){
                    // Notify the selected item text
                    category = spin.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        adapter.setDropDownViewResource(R.layout.spinner_item);


        /**
         *
         *
         * SETUP for popup message for vvalidation
         */


        final AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });




        /*
            end spinner and popup config
        */
        //---------------------------------------------------------------





        //get expense value
        Button btnadd = findViewById(R.id.btnadd);
        final Button btnconfirm = findViewById(R.id.btnconfirm);
        final Button btnenter = findViewById(R.id.btnenter);
        final TextView expenseoutput = findViewById(R.id.expenseoutput);
        final EditText expenseinput = findViewById(R.id.expenseinput);
        final EditText num = findViewById(R.id.num);
        final EditText commented = findViewById(R.id.com);




        /***
         *
         * Im planning on replacing this button with just an edit text text change listener but thats just cosmetic
         */
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //number validation
                if( num.getText().toString().equals("")){
                    number = 1;
                }
                else{
                    number = Integer.parseInt(num.getText().toString());
                }

                //expense validation, minumum one cent
                String temp = expenseinput.getText().toString();
                if (temp.matches("")){
                    //invalid

                    Log.e("BTNADD", "EXPENSE NOT ENTERED");
                    builder1.setMessage("Please enter an expense");
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
                else if(Double.parseDouble(temp) < 0.01) {
                    //invalid

                    Log.e("BTNADD","EXPENSE LESS THAN 1 CENT");
                    builder1.setMessage("Please enter at least $0.01");
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
                else {
                    //valid
                    isValidExpense = true;
                    expense= Double.parseDouble(temp);
                    subtotal = Math.round(number * expense * 100.0) / 100.0;
                    Log.d("BTNADD", String.format("%.2f",subtotal));
                    expenseStr = "$ " + String.format("%.2f",subtotal);
                    expenseoutput.setText(expenseStr);
                }



            }
        });


        //Enter expense into tuples and reset expense
        btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isValidExpense){
                    Log.e("BTNCONFIRM", "INVALID EXPENSE");
                    builder1.setMessage("Please enter a valid expense");
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
                else{

                    final TableLayout entries = findViewById(R.id.entries);
                    final TableRow newEntry = new TableRow(btnconfirm.getContext());




                    /***
                     * category validation
                     */
                    if(category.equals("Category")){
                        Log.e("BTNCONFIRM", "CATEGORY NOT SELECTED");
                        builder1.setMessage("Please enter a category");
                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    }
                    else{
                        TextView newCat = new TextView(btnconfirm.getContext());
                        newCat.setText(category);

                        //comment can be empty, so no validation needed
                        TextView newComment = new TextView(btnconfirm.getContext());
                        comment = commented.getText().toString();
                        newComment.setText(comment);


                        //show number and expense
                        TextView txt = new TextView(btnconfirm.getContext());
                        txt.setText(Integer.toString(number));
                        newEntry.addView(txt);

                        TextView txt2 = new TextView(btnconfirm.getContext());
                        txt2.setText("$ " + Double.toString(expense));
                        newEntry.addView(txt2);
                        Log.i("EXPENSE",Double.toString(expense));
                        Log.i("EXPENSE", Double.toString(subtotal));
                        //show subtotal
                        TextView txt3 = new TextView(btnconfirm.getContext());
                        String strsubtotal = "$" + subtotal;
                        txt3.setText(strsubtotal);
                        newEntry.addView(txt3);


                        //show category
                        newEntry.addView(newCat);

                        //show comment
                        newEntry.addView(newComment);




                        //show remove button
                        final Button newremove = new Button(btnconfirm.getContext());
                        newEntry.addView(newremove);
                        newremove.setText("X");
                        newremove.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.i("REMOVE", "REMOVING ENTRY FROM LIST AND RESET");

                                entries.removeView(newEntry);
                                TextView temp = (TextView) newEntry.getChildAt(2);
                                String str1 = temp.getText().toString().substring(1);
                                total -= Double.parseDouble(str1);
                                TextView totaltext = findViewById(R.id.total);
                                totaltext.setText("Total: $" + total);
                                expense = 0.0;
                                subtotal = 0.0;





                                //get the index of entry to remove it from the tuples list

                                /*************
                                 *
                                 *
                                 * STILL NOT WORKING LOL
                                 *
                                 */

                                // tuples.remove(index);
                            }
                        });



                        //ADD THIS NEW VALID ENTRY TO TABLE OF ENTRIES
                        entries.addView(newEntry);
                        //INCREASE THE OVERALL TOTAL BY THE SUBTOTAL(NUMBER * EXPENSE)
                        total += subtotal;




                        /***
                         *
                         *
                         * FOR DATABASE, TUPLES WILL HOLD INFORMATION
                         *
                         *
                         *
                         */
                        Date currentDay = Calendar.getInstance().getTime();
                        Quintet<Date, Double, Integer, String, String> tuple = new Quintet<>(currentDay,expense,number,category,comment);
                        tuples.add(tuple);
                        Log.i("TUPLES", "ADDED NEW TUPLE IN EXPENSE.JAVA < " + currentDay + " " +  expense + " " + number + " " + category + " " + comment + " >");



                        /////////////////////////////////////////////////////////////////////
                        //clear out previous
                        Log.i("BTNCONFIRM", "RESETTING EXPENSES AFTER SUCCESSFUL ADDITION OF ENTRY");
                        num.setText("");
                        expenseinput.setText("");
                        expenseoutput.setText("");
                        commented.setText("");
                        spin.setAdapter(null);
                        spin.setAdapter(adapter);
                        category = "Category";
                        expense = 0.0;
                        subtotal = 0.0;
                        isValidExpense = false;
                        TextView totalstr = findViewById(R.id.total);
                        totalstr.setText("Total: $" + total);



                    }

                }








            }
        });



        btnenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /***
                 *
                 *
                 * HERE IS WHERE THE USER WILL CONFIRM THEIR ENTRIES, FROM HERE YOU CAN UPDATE THE DATABASE
                 *
                 *
                 */


            }
        });


    }




}
