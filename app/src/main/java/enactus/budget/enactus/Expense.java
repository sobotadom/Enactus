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
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.inputmethod.InputMethodManager;
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
import org.w3c.dom.DOMConfiguration;
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

        finalindex = 1;
        tuples = new ArrayList<>();

        //Set   up window
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense);
        expense = 0;
        total = 0;
        finalindex = 0;
        category = "Category";
        isValidExpense = false;

        //CREATE/LOAD Database ETDB
        final ENTDB dataBase=ENTDB.getENTDB(getApplicationContext());



        /**
         *      Configure drop down menu for categories
         */



        final String[] categories = new String[]{
                "Category",
                "Fixed",
                "Flexible",
                "Discretionary"

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
        //Button btnadd = findViewById(R.id.btnadd);
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

        /*
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //number validation


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
        }); */


        expenseinput.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {




                if( num.getText().toString().equals("")){
                    number = 1;
                }
                else{
                    number = Integer.parseInt(num.getText().toString());
                }


                if(s.length() == 0){
                    expenseoutput.setText("");
                    isValidExpense = false;
                }
                //SET GOAL IF A GOAL IS PUT IN
                if(s.length() > 0){
                    double temp = Double.parseDouble(s.toString());
                    if (temp >= 0.01){
                        isValidExpense = true;
                        expense = temp;
                        subtotal = Math.round(number * expense * 100.0) / 100.0;
                        expenseoutput.setText("$" + String.format("%.2f", subtotal));
                    }
                    else{
                        expenseoutput.setText("");
                        isValidExpense = false;
                    }


                }


            }
        });
        num.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {


                if(s.length() == 0){
                    number = 1;
                }
                if(s.length() > 0){
                    number = Integer.parseInt(s.toString());
                }

                if(isValidExpense){
                    subtotal = Math.round(number * expense * 100.0) / 100.0;
                    expenseoutput.setText("$" + String.format("%.2f", subtotal));
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
                        findViewById(R.id.del).setVisibility(View.VISIBLE);

                        TextView newCat = new TextView(btnconfirm.getContext());
                        newCat.setText(category);
                        newCat.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        newCat.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);


                        //comment can be empty, so no validation needed
                        TextView newComment = new TextView(btnconfirm.getContext());
                        comment = commented.getText().toString();
                        newComment.setText(comment);
                        newComment.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        newComment.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);


                        //show number and expense
                        TextView txt = new TextView(btnconfirm.getContext());
                        txt.setText(Integer.toString(number));
                        txt.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        txt.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                        newEntry.addView(txt);


                        TextView txt2 = new TextView(btnconfirm.getContext());
                        txt2.setText("$ " + Double.toString(expense));
                        txt2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        txt2.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                        newEntry.addView(txt2);

                        //show subtotal
                        /*
                        TextView txt3 = new TextView(btnconfirm.getContext());
                        String strsubtotal = "$" + subtotal;
                        txt3.setText(strsubtotal);
                        newEntry.addView(txt3);
                        */

                        //show category
                        newEntry.addView(newCat);

                        //show comment
                        newEntry.addView(newComment);

                        //set tag for index
                        newEntry.setTag(tuples.size());
                        //finalindex++;


                        newEntry.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                int index = entries.indexOfChild(view);
                                entries.removeView(view);



                                tuples.remove(index - 2);
                                Log.i("REMOVING INDEX TUPLES", Integer.toString(index - 2));

                                TextView temp = (TextView) newEntry.getChildAt(0);
                                String str1 = temp.getText().toString();
                                TextView temp2 = (TextView) newEntry.getChildAt(1);
                                String str2 = temp2.getText().toString().substring(2);
                                total -= Integer.parseInt(str1) * Double.parseDouble(str2);

                                TextView totaltext = findViewById(R.id.total);
                                totaltext.setText("Total: $" + String.format("%.2f",total));

                                expense = 0.0;
                                subtotal = 0.0;

                                for(Quintet<Date,Double,Integer,String,String> q : tuples){
                                    Log.d("EFNA:FNAPGBNA:G", Double.toString(q.getValue1()));
                                }


                            }
                        });

                        //show remove button




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

                        //close keyboard
                        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                        //make table visible
                        LinearLayout l = findViewById(R.id.lay);


                        for(int i = l.indexOfChild(btnconfirm);i < l.getChildCount(); i++){
                            l.getChildAt(i).setVisibility(View.VISIBLE);
                        }
                    }

                }








            }
        });



        btnenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(Quintet<Date,Double,Integer, String, String> q : tuples) {

                    /*
                    Adding entry into DATABASE, make sure date is String
                    cannot put object types into Room databases


                    newExp.setCost(double cost);
                    newExp.setQuantity(int quantity);
                    newExp.setComment(String comment);
                    newExp.setDate(String date);
                    newExp.setCategory(String category);



                     */
                    EXPTBL newExp = new EXPTBL();
                    //After setting all values into this EXPTBL can add it to database
                    //through the data access object interface
                    //make sure newExp has correct values stored in correct places


                    newExp.setCost(q.getValue1());
                    newExp.setQuantity(q.getValue2());
                    newExp.setComment(q.getValue3());

                    //date cannot be type Date

                    newExp.setDate(q.getValue0().toString());
                    newExp.setCategory(q.getValue4());

                    //add expense tuple(type EXPTBL) into database
                    dataBase.expDAO().insertExpenses(newExp);


                    Log.i("FINAL TUPLES", q.getValue3() + " " + q.getValue4());
                    finish();
                }
            }
        });


    }




}
