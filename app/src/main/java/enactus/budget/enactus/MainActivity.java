package enactus.budget.enactus;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TableRow;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.w3c.dom.Text;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static int REQUEST_CODE = 0;
    private double totalSpent = 0;
    private int fixed_days, flexible_days, discretionary_days;
    private double cat1_goal, cat2_goal, cat3_goal, cat1_remain, cat2_remain, cat3_remain;
    private String category;
    private DateTime date_fixed, date_flexible, date_discretionary;
    private TableRow before_set_cat1, before_set_cat2, before_set_cat3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        JodaTimeAndroid.init(this);


        before_set_cat1 = findViewById(R.id.one);
        before_set_cat2 = findViewById(R.id.two);
        before_set_cat3 = findViewById(R.id.three);

           /*

        BAD DESIGN, SHOULD BE CREATED INTO A INITIALIZATION CLASS/METHOD AND
        SHOULD ONLY BE CALLED THROUGH METHODS
        SHOULD NOT ALLOW MAIN THREAD QUERIES



         */
        //CREATE/LOAD Database ENTDB
        final ENTDB dataBase=ENTDB.getENTDB(getApplicationContext());



        //GET TUPLES FROM DATABASE
        List<EXPTBL> expenses=dataBase.expDAO().getAllExpenses();

        //Use iterator to go through list easily to list all elements
        Iterator<EXPTBL> expensesIterator=expenses.iterator();

        while(expensesIterator.hasNext()){
            String expenseOut=expensesIterator.next().toString();
            Log.d("Database search:  ",expenseOut);

        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open new entry page
                startActivityForResult(new Intent(MainActivity.this, Expense.class), REQUEST_CODE);
            }
        });


        //CONFIGURE TABLE DISPLAYING DATABASE ENTRIES

        final TableLayout tl = findViewById(R.id.tableLayout);

        final String[] categories = new String[]{
                "Sort By:",
                "Fixed",
                "Flexible",
                "Discretionary"

        };
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, categories) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        Spinner spin = findViewById(R.id.tableSpin);
        spin.setAdapter(adapter);


        /***
         *
         * THIS IS WHERE WHERE THE DATABASE COMES IN !!!!!!!!!!!!!!!!!!!!
         *
         */


        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if (position > 0) {
                    // Notify the selected item text

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        adapter.setDropDownViewResource(R.layout.spinner_item);

        //FINISH TABLE CONFIG


        /*
        Configure balance and goals

         */


        EditText first = findViewById(R.id.cat1goal);
        first.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {


                if (s.length() == 0) {
                    findViewById(R.id.btngoal1).setVisibility(View.INVISIBLE);
                }
                //SET GOAL IF A GOAL IS PUT IN
                if (s.length() > 0) {
                    cat1_goal = Double.parseDouble(s.toString());
                    findViewById(R.id.btngoal1).setVisibility(View.VISIBLE);
                }


            }
        });

        final EditText second = findViewById(R.id.cat2goal);
        second.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                //SET GOAL IF A GOAL IS PUT IN
                if (s.length() == 0) {
                    findViewById(R.id.btngoal2).setVisibility(View.INVISIBLE);
                }
                if (s.length() > 0) {
                    cat2_goal = Double.parseDouble(s.toString());
                    findViewById(R.id.btngoal2).setVisibility(View.VISIBLE);
                }


            }
        });
        EditText third = findViewById(R.id.cat3goal);
        third.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {


                if (s.length() == 0) {
                    findViewById(R.id.btngoal3).setVisibility(View.INVISIBLE);
                }
                //SET GOAL IF A GOAL IS PUT IN
                if (s.length() > 0) {
                    cat3_goal = Double.parseDouble(s.toString());
                    findViewById(R.id.btngoal3).setVisibility(View.VISIBLE);
                }


            }
        });

        final View.OnClickListener getdate = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show dialogan get the proper date

                Calendar cal = Calendar.getInstance();
                DateTime today = new DateTime(DateTimeZone.UTC);
                Log.i("DATE", Integer.toString(today.getMonthOfYear()));



               // Log.i("CAL",Integer.toString(today.getDay()) + " " + cal.get(Calendar.DAY_OF_MONTH));
                EditText cat1 = findViewById(R.id.cat1goal);
                EditText cat2 = findViewById(R.id.cat2goal);
                EditText cat3 = findViewById(R.id.cat3goal);


                switch(view.getId()){





                    case R.id.btngoal1: //GOAL FOR FIXED


                        Log.i("GOAL1", "USER IS SETTING GOAL FOR FIXED EXPENSES");
                        DatePickerDialog start_time = new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                                EditText ed = findViewById(R.id.cat1goal);
                                cat1_goal = Double.parseDouble(ed.getText().toString());

                                inputGoal(i,i1,i2, R.id.btngoal1);
                                Button btn = findViewById(R.id.btngoal1);
                                btn.setEnabled(false);
                                btn.setText(Integer.toString(fixed_days));
                                ed.setVisibility(View.GONE);

                                TextView txt = findViewById(R.id.txtgoal1);
                                txt.setVisibility(View.VISIBLE);
                                txt.setText(Double.toString(cat1_goal));



                            }
                        }, today.getYear(), today.getMonthOfYear() - 1, today.getDayOfMonth());
                        start_time.getDatePicker().setMinDate(System.currentTimeMillis() + 86400000);

                        start_time.show();


                        break;

                    case R.id.btngoal2://GOAL FOR FLEXIBLE
                        Log.i("GOAL2", "USER IS SETTING GOAL FOR FLEXIBLE EXPENSES");
                        DatePickerDialog start_time2 = new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                EditText ed = findViewById(R.id.cat2goal);
                                cat2_goal = Double.parseDouble(ed.getText().toString());

                                inputGoal(i,i1,i2, R.id.btngoal2);
                                Button btn = findViewById(R.id.btngoal2);
                                btn.setEnabled(false);
                                btn.setText(Integer.toString(flexible_days));

                                ed.setVisibility(View.GONE);

                                TextView txt = findViewById(R.id.txtgoal2);
                                txt.setVisibility(View.VISIBLE);
                                txt.setText(Double.toString(cat2_goal));

                            }
                        }, today.getYear(), today.getMonthOfYear() - 1, today.getDayOfMonth());
                        start_time2.getDatePicker().setMinDate(System.currentTimeMillis() + 86400000 );



                        start_time2.show();
                        break;

                    case R.id.btngoal3: //GOAL FOR DISCRETIONARY
                        Log.i("GOAL3","USER IS SETTING GOAL FOR DISCRETIONARY");

                        DatePickerDialog start_time3 = new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                EditText ed = findViewById(R.id.cat3goal);
                                cat3_goal = Double.parseDouble(ed.getText().toString());

                                inputGoal(i,i1,i2, R.id.btngoal3);
                                Button btn = findViewById(R.id.btngoal3);
                                btn.setEnabled(false);
                                btn.setText(Integer.toString(discretionary_days));

                                ed.setVisibility(View.GONE);

                                TextView txt = findViewById(R.id.txtgoal3);
                                txt.setVisibility(View.VISIBLE);
                                txt.setText(Double.toString(cat3_goal));


                            }
                        }, today.getYear(), today.getMonthOfYear() - 1, today.getDayOfMonth());
                        start_time3.getDatePicker().setMinDate(System.currentTimeMillis() + 86400000);
                        start_time3.show();

                        break;
                }



            }
        };

        Button btngoal1 = findViewById(R.id.btngoal1);
        btngoal1.setOnClickListener(getdate);

        Button btngoal2 = findViewById(R.id.btngoal2);
        btngoal2.setOnClickListener(getdate);

        Button btngoal3 = findViewById(R.id.btngoal3);
        btngoal3.setOnClickListener(getdate);



    }
    private void inputGoal(int y, int m, int d, int id){
        DateTime today = new DateTime(DateTimeZone.UTC);
        switch(id){
            case R.id.btngoal1:
                date_fixed = new DateTime(y,m + 1,d,0,0);
                fixed_days = Days.daysBetween(today.withTimeAtStartOfDay(), date_fixed.withTimeAtStartOfDay()).getDays();
                break;
            case R.id.btngoal2:
                date_flexible = new DateTime(y,m + 1,d,0,0);
                flexible_days = Days.daysBetween(today.withTimeAtStartOfDay(), date_flexible.withTimeAtStartOfDay()).getDays();
                break;
            case R.id.btngoal3:
                date_discretionary = new DateTime(y,m + 1,d,0,0);
                discretionary_days = Days.daysBetween(today.withTimeAtStartOfDay(), date_discretionary.withTimeAtStartOfDay()).getDays();
                break;
        }









    }
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {

        //GET result from entering expense, database entry
       // TableRow tr = new TableRow(this);
       // TableLayout tl = findViewById(R.id.tableLayout);

      //  TextView category = new TextView(this);
       // category.setText("Category");
       // TextView cost = new TextView(this);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // A contact was picked.  Here we will just display it
                // to the user.

                //Bundle b = data.getBundleExtra("EXPENSE");
                //category.setText(b.getString("category"));
                //cost.setText(b.getString("STR"));

               // tr.addView(category);
               // tr.addView(cost);
               // tl.addView(tr);

                //enterExpense(b.getDouble("Expense"));
            }
        }
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
