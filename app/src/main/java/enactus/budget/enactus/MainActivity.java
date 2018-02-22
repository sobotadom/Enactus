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
import android.widget.Toast;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.w3c.dom.Text;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

// TODO: 2018-02-21 figure out how to make sure expenses the same day but before the goal was made being reflected in the goal progress

public class MainActivity extends AppCompatActivity {

    private static int REQUEST_CODE = 0;
    private double totalSpent = 0;
    private int sort_by;
    //private double cat1_goal, cat2_goal, cat3_goal;
   // private String category;
    //private DateTime date_fixed, date_flexible, date_discretionary;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        JodaTimeAndroid.init(this);

        //UPDATE DATABASE
        updateTable();
        updateGoal1();
        updateGoal2();
        updateGoal3();
        //deleteAll();





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
                "All",
                "Fixed",
                "Flexible",
                "Discretionary"

        };
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, categories) {
            @Override
            public boolean isEnabled(int position) {

                    return true;
            }


            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {

                View view = super.getDropDownView(position, convertView, parent);
                /*TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }*/
                return view;
            }
        };
        Spinner spin = findViewById(R.id.tableSpin);
        spin.setAdapter(adapter);



        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                sort_by = position;
                updateTable();

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
                    findViewById(R.id.btngoal1).setEnabled(false);
                }
                //SET GOAL IF A GOAL IS PUT IN
                if (s.length() > 0) {
                    //cat1_goal = Double.parseDouble(s.toString());
                    findViewById(R.id.btngoal1).setEnabled(true);
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
                    findViewById(R.id.btngoal2).setEnabled(false);
                }
                if (s.length() > 0) {
                    //cat2_goal = Double.parseDouble(s.toString());
                    findViewById(R.id.btngoal2).setEnabled(true);
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
                    findViewById(R.id.btngoal3).setEnabled(false);
                }
                //SET GOAL IF A GOAL IS PUT IN
                if (s.length() > 0) {
                    //cat3_goal = Double.parseDouble(s.toString());
                    findViewById(R.id.btngoal3).setEnabled(true);
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


                switch(view.getId()){

                    case R.id.btngoal1: //GOAL FOR FIXED

                        Log.i("GOAL1", "USER IS SETTING GOAL FOR FIXED EXPENSES");
                        DatePickerDialog start_time = new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                inputGoal(i,i1,i2, R.id.btngoal1);
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
                                inputGoal(i,i1,i2, R.id.btngoal2);
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
                                inputGoal(i,i1,i2, R.id.btngoal3);

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

        GOALDB dataBase = GOALDB.getGOALDB(getApplicationContext());
        GOALBL newGoal = new GOALBL();

        //dataBase.goalDAO().deleteAllGoals();
        DateTime today = new DateTime(DateTimeZone.UTC);
        String s = today.getYear() + "-" + today.getMonthOfYear() + "-" + today.getDayOfMonth();


        switch(id){
            case R.id.btngoal1:
                EditText ed = findViewById(R.id.cat1goal);
                double cat1_goal = Double.parseDouble(ed.getText().toString());

                DateTime date_fixed = new DateTime(y,m + 1,d,0,0);


                //set goal
                newGoal.setGoal(cat1_goal);

                //set cat
                newGoal.setCategory("Fixed");

                //set start date

                newGoal.setStart(s);

                //set end date
                String e = date_fixed.getYear() + "-" + date_fixed.getMonthOfYear() + "-" + date_fixed.getDayOfMonth();
                newGoal.setEnd(e);

                //set status
                newGoal.setStatus("In Progress");
                newGoal.setProgress(0);

                dataBase.goalDAO().insertGoal(newGoal);
                updateGoal1();

                break;

            case R.id.btngoal2:
                EditText ed1 = findViewById(R.id.cat2goal);
                DateTime date_flexible = new DateTime(y,m + 1,d,0,0);

                double cat2_goal = Double.parseDouble(ed1.getText().toString());

                //set goal
                newGoal.setGoal(cat2_goal);

                //set cat
                newGoal.setCategory("Flexible");

                //set start date
               newGoal.setStart(s);

                //set end date
                String e1 = date_flexible.getYear() + "-" + date_flexible.getMonthOfYear() + "-" + date_flexible.getDayOfMonth();
                newGoal.setEnd(e1);

                //set status
                newGoal.setStatus("In Progress");
                newGoal.setProgress(0);

                dataBase.goalDAO().insertGoal(newGoal);
                updateGoal2();
                break;
            case R.id.btngoal3:
                EditText ed2 = findViewById(R.id.cat3goal);
                DateTime date_discretionary = new DateTime(y,m + 1,d,0,0);

                double cat3_goal = Double.parseDouble(ed2.getText().toString());

                //set goal
                newGoal.setGoal(cat3_goal);

                //set cat
                newGoal.setCategory("Discretionary");

                //set start date

                newGoal.setStart(s);

                //set end date
                String e2 = date_discretionary.getYear() + "-" + date_discretionary.getMonthOfYear() + "-" + date_discretionary.getDayOfMonth();
                newGoal.setEnd(e2);

                //set status
                newGoal.setStatus("In Progress");
                newGoal.setProgress(0);

                dataBase.goalDAO().insertGoal(newGoal);
                updateGoal3();
                break;
        }











    }

    private void updateGoal1(){
        Log.i("Main","Updating Goal1 ");


        GOALBL fixed = currentFixed();//should only be one goal in progress per category

        if( fixed != null){
            DateTime start = DateTimeFormat.forPattern("yyyy-MM-dd").parseDateTime(fixed.getStart());
            DateTime end = DateTimeFormat.forPattern("yyyy-MM-dd").parseDateTime(fixed.getEnd());
            DateTime today = new DateTime(DateTimeZone.UTC);
            String s = today.getYear() + "-" + today.getMonthOfYear() + "-" + today.getDayOfMonth();


            double goal = fixed.getGoal();
            String category = fixed.getCategory();
            String status = fixed.getStatus();





            if(Days.daysBetween(today.withTimeAtStartOfDay(), end.withTimeAtStartOfDay()).getDays() < 0 ){ //goal is complete, reset back

                if (fixed.getProgress() >= 0.00) {  //test is progress is negative or positive
                    fixed.setStatus("Pass");
                    Log.i("MAIN","fixed Goal complete!");
                    //NOTIFACTION?????
                }
                else{
                    fixed.setStatus("Fail");
                    Log.i("MAIN","fixed Goal failed!");
                    //NOTIFICATION????
                }

                resetGoals(R.id.txtgoal1,R.id.cat1goal, R.id.btngoal1);



            }
            else{ //goal is still going


                Button btn = findViewById(R.id.btngoal1);
                btn.setEnabled(false);
                btn.setText(Integer.toString(Days.daysBetween(today.withTimeAtStartOfDay(),end.withTimeAtStartOfDay()).getDays()));
                findViewById(R.id.cat1goal).setVisibility(View.GONE);

                TextView txt = findViewById(R.id.txtgoal1);
                txt.setVisibility(View.VISIBLE);

                double progress = goal - fixedCost();
                fixed.setProgress(progress);

                if(progress > 0){
                    txt.setTextColor(Color.GREEN);
                }
                else{
                    txt.setTextColor(Color.RED);
                }
                txt.setText(Double.toString(progress));
            }


        }

    }





    private void updateGoal2(){
        Log.i("Main","Updating Goal2 ");


        GOALBL flex = currentFlexible();//should only be one goal in progress per category

        if(flex != null){
            DateTime start = DateTimeFormat.forPattern("yyyy-MM-dd").parseDateTime(flex.getStart());
            DateTime end = DateTimeFormat.forPattern("yyyy-MM-dd").parseDateTime(flex.getEnd());
            DateTime today = new DateTime(DateTimeZone.UTC);
            String s = today.getYear() + "-" + today.getMonthOfYear() + "-" + today.getDayOfMonth();


            double goal = flex.getGoal();
            String category = flex.getCategory();
            String status = flex.getStatus();





            if(Days.daysBetween(today.withTimeAtStartOfDay(),end.withTimeAtStartOfDay()).getDays() < 0 ){ //goal is complete, reset back

                if (flex.getProgress() >= 0.00) {  //test is progress is negative or positive
                    flex.setStatus("Pass");
                    Log.i("MAIN","flex Goal complete!");
                    //NOTIFACTION?????
                }
                else{
                    flex.setStatus("Fail");
                    Log.i("MAIN","flex Goal failed!");
                    //NOTIFICATION????
                }


                resetGoals(R.id.txtgoal2,R.id.cat2goal, R.id.btngoal2);



            }
            else{ //goal is still going


                Button btn = findViewById(R.id.btngoal2);
                btn.setEnabled(false);
                btn.setText(Integer.toString(Days.daysBetween(today.withTimeAtStartOfDay(),end.withTimeAtStartOfDay()).getDays()));
                findViewById(R.id.cat2goal).setVisibility(View.GONE);

                TextView txt = findViewById(R.id.txtgoal2);
                txt.setVisibility(View.VISIBLE);
                double progress = goal - flexCost() ;
                flex.setProgress(progress);

                if(progress > 0){

                    txt.setTextColor(Color.GREEN);
                }
                else{
                    txt.setTextColor(Color.RED);
                }
                txt.setText(Double.toString(progress));
            }
        }


    }

    private void updateGoal3(){
        Log.i("Main","Updating Goal3 ");

        GOALBL disc = currentDiscretionary();

        if(disc != null){
            DateTime start = DateTimeFormat.forPattern("yyyy-MM-dd").parseDateTime(disc.getStart());
            DateTime end = DateTimeFormat.forPattern("yyyy-MM-dd").parseDateTime(disc.getEnd());
            DateTime today = new DateTime(DateTimeZone.UTC);
            String s = today.getYear() + "-" + today.getMonthOfYear() + "-" + today.getDayOfMonth();


            double goal = disc.getGoal();
            String category = disc.getCategory();
            String status = disc.getStatus();





            if(Days.daysBetween(today.withTimeAtStartOfDay(), end.withTimeAtStartOfDay()).getDays() < 0 ){ //goal is complete, reset back

                if (disc.getProgress() >= 0.00) {  //test is progress is negative or positive
                    disc.setStatus("Pass");
                    Log.i("MAIN","disc Goal complete!");
                    //NOTIFACTION?????
                }
                else{
                    disc.setStatus("Fail");
                    Log.i("MAIN","disc Goal failed!");
                    //NOTIFICATION????
                }


                resetGoals(R.id.txtgoal3,R.id.cat3goal, R.id.btngoal3);


            }
            else{ //goal is still going


                Button btn = findViewById(R.id.btngoal3);
                btn.setEnabled(false);
                btn.setText(Integer.toString(Days.daysBetween(start.withTimeAtStartOfDay(),end.withTimeAtStartOfDay()).getDays()));
                findViewById(R.id.cat3goal).setVisibility(View.GONE);

                TextView txt = findViewById(R.id.txtgoal3);
                txt.setVisibility(View.VISIBLE);
                double progress = goal - discCost();
                disc.setProgress(progress);

                if(progress > 0){
                    txt.setTextColor(Color.GREEN);
                }
                else{
                    txt.setTextColor(Color.RED);
                }
                txt.setText(Double.toString(progress));
            }
        }


    }

    private void resetGoals(int txt2, int cat2, int btn2){ //reset goal table
        Button btn = findViewById(btn2);
        btn.setEnabled(false);
        btn.setText("Set Date");
        findViewById(cat2).setVisibility(View.GONE);

        TextView txt = findViewById(txt2);
        txt.setVisibility(View.GONE);
        txt.setText("");

        EditText ed = findViewById(cat2);
        ed.setVisibility(View.VISIBLE);
        ed.setText("");
    }
    private GOALBL currentFixed(){
        GOALDB dataBase = GOALDB.getGOALDB(getApplicationContext());
        List<GOALBL> list = dataBase.goalDAO().getCurrentFixedGoal();

        Iterator<GOALBL> it = list.iterator();
        if(it.hasNext()){
            return it.next();
        }
        else{
            return null;
        }

    }
    private GOALBL currentFlexible(){
        GOALDB dataBase = GOALDB.getGOALDB(getApplicationContext());
        List<GOALBL> list = dataBase.goalDAO().getCurrentFlexibleGoal();
        Iterator<GOALBL> it = list.iterator();
        if(it.hasNext()){
            return it.next();
        }
        else{
            return null;
        }

    }
    private GOALBL currentDiscretionary(){
        GOALDB dataBase = GOALDB.getGOALDB(getApplicationContext());
        List<GOALBL> list = dataBase.goalDAO().getCurrentDiscretionaryGoal();
        Iterator<GOALBL> it = list.iterator();
        if(it.hasNext()){
            return it.next();
        }
        else{
            return null;
        }

    }

    private void updateTable() {
        Log.i("Main", "Updating table with database");
        //CREATE/LOAD Database ENTDB
        ENTDB dataBase = ENTDB.getENTDB(getApplicationContext());
        List<EXPTBL> expenses = null;

        //clear out table so we get no duplicates
        TableLayout tl = findViewById(R.id.tableLayout);
        TableRow header = findViewById(R.id.header);


        tl.removeAllViews();
        tl.addView(header);


        if (sort_by == 0) {  //GETS ALL EXPENSES FROM DATABASE
            //GET TUPLES FROM DATABASE
            expenses = dataBase.expDAO().getAllExpenses();

        } else if (sort_by == 1) {
            expenses = dataBase.expDAO().getFixedExpenses();
        } else if (sort_by == 2) {
            expenses = dataBase.expDAO().getFlexibleExpenses();
        } else if (sort_by == 3) {
            expenses = dataBase.expDAO().getDiscretionaryExpenses();
        } else {
            Log.e("SPINNER", "Somehow someone selected a category not on the list");
        }

        //Use iterator to go through list easily to list all elements
        Iterator<EXPTBL> expensesIterator = expenses.iterator();

        while (expensesIterator.hasNext()) {
            TableRow tr = new TableRow(this);

            EXPTBL current = expensesIterator.next();
            String expenseOut = current.toString();
            Log.i("Inputting into Table:  ", "HELLO " + current.getCategory());



            //cost
            TextView cost = new TextView(this);
            cost.setText(Double.toString(current.getCost()));
            cost.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tr.addView(cost);

            //quantity
            TextView quantity = new TextView(this);
            quantity.setText(Double.toString(current.getQuantity()));
            quantity.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tr.addView(quantity);

            //category
            TextView cat = new TextView(this);
            cat.setText(current.getCategory());
            cat.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tr.addView(cat);

            //comment
            TextView comment = new TextView(this);
            comment.setText(current.getComment());
            comment.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tr.addView(comment);

            //date
            TextView date = new TextView(this);
            date.setText(current.getDate());
            date.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tr.addView(date);

            tl.addView(tr);

        }
    }
    private void deleteAll(){
        ENTDB dataBase=ENTDB.getENTDB(getApplicationContext());
        dataBase.expDAO().deleteAllExpenses();
        updateTable();
    }

    private double fixedCost(){
        //get the total fixed cost within the current fixed goals timeframe
        double cost = 0.0;

        ENTDB dataBase = ENTDB.getENTDB(getApplicationContext());
        GOALBL fixed = currentFixed();
        if (fixed != null){
            DateTime start = DateTime.parse(fixed.getStart());


            List<EXPTBL> list = dataBase.expDAO().getFixedExpenses();
            Iterator<EXPTBL> it = list.iterator();
            while(it.hasNext()){
                EXPTBL exp = it.next();
                DateTime day = DateTime.parse(exp.getDate());

                if(!day.isBefore(start)){ //if the date of the expense happens before the goal was set, dont update the goal
                    cost += exp.getQuantity() * exp.getCost();

                }

            }
        }
        Log.i("FIXEDCOST", Double.toString(cost));
        return cost;
    }

    private double flexCost(){
        double cost = 0.0;

        ENTDB dataBase = ENTDB.getENTDB(getApplicationContext());
        GOALBL flex = currentFlexible();
        if (flex != null){
            DateTime start = DateTime.parse(flex.getStart());

            List<EXPTBL> list = dataBase.expDAO().getFlexibleExpenses();
            Iterator<EXPTBL> it = list.iterator();
            while(it.hasNext()){
                EXPTBL exp = it.next();
                DateTime day = DateTime.parse(exp.getDate());

                if(!day.isBefore(start)){ //if the date of the expense happens before the goal was set, dont update the goal
                    cost += exp.getQuantity() * exp.getCost();
                }

            }
        }

        return cost;
    }

    private double discCost(){
        double cost = 0.0;

        ENTDB dataBase = ENTDB.getENTDB(getApplicationContext());
        GOALBL flex = currentDiscretionary();
        if (flex != null){
            DateTime start = DateTime.parse(flex.getStart());

            List<EXPTBL> list = dataBase.expDAO().getDiscretionaryExpenses();
            Iterator<EXPTBL> it = list.iterator();
            while(it.hasNext()){
                EXPTBL exp = it.next();
                DateTime day = DateTime.parse(exp.getDate());

                if(!day.isBefore(start)){ //if the date of the expense happens before the goal was set, dont update the goal
                    cost += exp.getQuantity() * exp.getCost();
                }

            }
        }

        return cost;
    }
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {



        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                //THIS IS WHERE WE UPDATE PAGE
                updateTable();
                updateGoal1();
                updateGoal2();
                updateGoal3();



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
        if (id == R.id.clear) {
            deleteAll();
            GOALDB dataBase = GOALDB.getGOALDB(getApplicationContext());
            dataBase.goalDAO().deleteAllGoals();
            resetGoals(R.id.txtgoal1,R.id.cat1goal,R.id.btngoal1);
            resetGoals(R.id.txtgoal2,R.id.cat2goal,R.id.btngoal2);
            resetGoals(R.id.txtgoal3,R.id.cat3goal,R.id.btngoal3);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
