package enactus.budget.enactus;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
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
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;



public class MainActivity extends AppCompatActivity {

    private static int REQUEST_CODE = 0;
    private int sort_by;
    private TableRow header;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);



        GOALDB dataBase = GOALDB.getGOALDB(getApplicationContext());


        JodaTimeAndroid.init(this);

        //UPDATE DATABASE

        showNotification("h","O");
        updateGoal1();
        updateGoal2();
        updateGoal3();
        updateTable();






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
                "Discretionary",
                "Goals"

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

                    findViewById(R.id.btngoal3).setEnabled(true);
                }


            }
        });

        final View.OnClickListener getdate = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show dialogan get the proper date

                Calendar cal = Calendar.getInstance();
                DateTime today = new DateTime(DateTimeZone.getDefault());
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
    private void showNotification(String msg, String passfail){

       /*
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR, 8);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND,0);



        Intent intent = new Intent(this, Notification_Reciever.class);
        intent.putExtra("msg", msg);
        intent.putExtra("passfail", passfail);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);


        /*NotificationCompat.Builder b = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentText(msg)
                .setContentTitle(passfail)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg));

        NotificationManager m = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            String CHANNEL_ID = "my_channel";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Noitfication", NotificationManager.IMPORTANCE_DEFAULT);
            m.createNotificationChannel(channel);
            b.setChannelId(CHANNEL_ID);

        }

        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent );

        //
        */

    }

    private void inputGoal(int y, int m, int d, int id){

        GOALDB dataBase = GOALDB.getGOALDB(getApplicationContext());
        GOALBL newGoal = new GOALBL();


        DateTime today = new DateTime(DateTimeZone.getDefault());
        String s = today.getYear() + "-" + today.getMonthOfYear() + "-" + today.getDayOfMonth() + " " + today.getHourOfDay() + ":" + today.getMinuteOfHour() + ":" + today.getSecondOfMinute();


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
   
   


    public void updateGoal1(){
        Log.i("Main","Updating Goal1 ");


        GOALBL fixed = currentFixed();//should only be one goal in progress per category

        if( fixed != null){

            DateTime end = DateTimeFormat.forPattern("yyyy-MM-dd").parseDateTime(fixed.getEnd());
            DateTime today = new DateTime(DateTimeZone.getDefault());



            double goal = fixed.getGoal();





            Log.i("DAYS", today.toString());
            Log.i("DAYS", end.withTimeAtStartOfDay().toString());
            Log.i("DAYS", Integer.toString(Days.daysBetween(today.withTimeAtStartOfDay(), end.withTimeAtStartOfDay()).getDays()));
            if(Days.daysBetween(today.withTimeAtStartOfDay(), end.withTimeAtStartOfDay()).getDays() < 1 ){ //goal is complete, reset back

                if (fixed.getProgress() >= 0.00) {  //test is progress is negative or positive
                    resetGoals(R.id.txtgoal1,R.id.cat1goal, R.id.btngoal1);
                    GOALDB.getGOALDB(this).goalDAO().goalPassed("Fixed");

                    Log.i("FIXED STATUS",fixed.getStatus());
                    String s = "You completed your fixed Goal of " + fixed.getGoal() + " \nfrom " + fixed.getStart() + " to " + fixed.getEnd();
                    showPopup(s );
                    //NOTIFACTION?????
                    showNotification(s, "Pass");

                }
                else{
                    resetGoals(R.id.txtgoal1,R.id.cat1goal, R.id.btngoal1);
                    GOALDB.getGOALDB(this).goalDAO().goalFailed("Fixed");
                    Log.i("MAIN","fixed Goal failed!");
                    String s = "You failed your fixed Goal of " + fixed.getGoal() + " \nfrom " + fixed.getStart() + " to " + fixed.getEnd();
                    showPopup(s );
                    //NOTIFACTION?????
                    showNotification(s, "Fail");
                }

                threeFails();



            }
            else{ //goal is still going


                Button btn = findViewById(R.id.btngoal1);
                btn.setEnabled(false);
                btn.setText(Integer.toString(Days.daysBetween(today.withTimeAtStartOfDay(),end.withTimeAtStartOfDay()).getDays()) + " days remain");
                findViewById(R.id.cat1goal).setVisibility(View.GONE);

                TextView txt = findViewById(R.id.txtgoal1);
                txt.setVisibility(View.VISIBLE);

                double progress = goal - fixedCost();
                GOALDB.getGOALDB(this).goalDAO().updateProgress(progress,"Fixed");

                if(progress >= 0){
                    txt.setTextColor(Color.GREEN);
                }
                else{
                    txt.setTextColor(Color.RED);
                }
                txt.setText(String.format("%.2f", progress));
            }


        }
        updateTable();


    }





    public void updateGoal2(){
        Log.i("Main","Updating Goal2 ");


        GOALBL flex = currentFlexible();//should only be one goal in progress per category

        if(flex != null){

            DateTime end = DateTimeFormat.forPattern("yyyy-MM-dd").parseDateTime(flex.getEnd());
            DateTime today = new DateTime(DateTimeZone.getDefault());



            double goal = flex.getGoal();






            if(Days.daysBetween(today.withTimeAtStartOfDay(),end.withTimeAtStartOfDay()).getDays() < 1 ){ //goal is complete, reset back

                if (flex.getProgress() >= 0.00) {  //test is progress is negative or positive
                    resetGoals(R.id.txtgoal2,R.id.cat2goal, R.id.btngoal2);
                    GOALDB.getGOALDB(this).goalDAO().goalPassed("Flexible");
                    String s = "You completed your flexible Goal of " + flex.getGoal() + " \nfrom " + flex.getStart() + " to " + flex.getEnd();
                    showPopup(s );
                    //NOTIFACTION?????
                    showNotification(s, "Pass");
                }
                else{
                    resetGoals(R.id.txtgoal2,R.id.cat2goal, R.id.btngoal2);
                    GOALDB.getGOALDB(this).goalDAO().goalFailed("Flexible");
                    String s = "You failed your flexible Goal of " + flex.getGoal() + " \nfrom " + flex.getStart() + " to " + flex.getEnd();
                    showPopup(s );
                    //NOTIFACTION?????
                    showNotification(s, "Fail");
                }

                threeFails();




            }
            else{ //goal is still going


                Button btn = findViewById(R.id.btngoal2);
                btn.setEnabled(false);
                btn.setText(Integer.toString(Days.daysBetween(today.withTimeAtStartOfDay(),end.withTimeAtStartOfDay()).getDays()) + " days remain");
                findViewById(R.id.cat2goal).setVisibility(View.GONE);

                TextView txt = findViewById(R.id.txtgoal2);
                txt.setVisibility(View.VISIBLE);
                double progress = goal - flexCost() ;
                GOALDB.getGOALDB(this).goalDAO().updateProgress(progress,"Flexible");

                if(progress >= 0){

                    txt.setTextColor(Color.GREEN);
                }
                else{
                    txt.setTextColor(Color.RED);
                }
                txt.setText(String.format("%.2f",progress));
            }
        }
        updateTable();

    }

    public void updateGoal3(){
        Log.i("Main","Updating Goal3 ");

        GOALBL disc = currentDiscretionary();

        if(disc != null){

            DateTime end = DateTimeFormat.forPattern("yyyy-MM-dd").parseDateTime(disc.getEnd());
            DateTime today = new DateTime(DateTimeZone.getDefault());



            double goal = disc.getGoal();






            if(Days.daysBetween(today.withTimeAtStartOfDay(), end.withTimeAtStartOfDay()).getDays() < 1 ){ //goal is complete, reset back



                if (disc.getProgress() >= 0.00) {  //test is progress is negative or positive
                    resetGoals(R.id.txtgoal3,R.id.cat3goal, R.id.btngoal3);
                    GOALDB.getGOALDB(this).goalDAO().goalPassed("Discretionary");
                    Log.i("MAIN","disc Goal complete!");
                    String s = "You completed your discretionary Goal of " + disc.getGoal() + " \nfrom " + disc.getStart() + " to " + disc.getEnd();
                    showPopup(s );
                    //NOTIFACTION?????
                    showNotification(s, "Pass");
                }
                else{
                    resetGoals(R.id.txtgoal3,R.id.cat3goal, R.id.btngoal3);
                    GOALDB.getGOALDB(this).goalDAO().goalFailed("Discretionary");
                    Log.i("MAIN","disc Goal failed!");
                    String s = "You failed your discretionary Goal of " + disc.getGoal() + " \nfrom " + disc.getStart() + " to " + disc.getEnd();
                    showPopup(s );
                    //NOTIFACTION?????
                    showNotification(s, "Fail");
                    //NOTIFICATION????
                }

                threeFails();



            }
            else{ //goal is still going


                Button btn = findViewById(R.id.btngoal3);
                btn.setEnabled(false);
                btn.setText(Integer.toString(Days.daysBetween(today.withTimeAtStartOfDay(),end.withTimeAtStartOfDay()).getDays()) + " days remain");
                findViewById(R.id.cat3goal).setVisibility(View.GONE);

                TextView txt = findViewById(R.id.txtgoal3);
                txt.setVisibility(View.VISIBLE);
                double progress = goal - discCost();
                GOALDB.getGOALDB(this).goalDAO().updateProgress(progress,"Discretionary");

                if(progress >= 0){
                    txt.setTextColor(Color.GREEN);
                }
                else{
                    txt.setTextColor(Color.RED);
                }
                txt.setText(String.format("%.2f",progress));
            }
        }
        updateTable();

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

    private void showAllGoals(){
        GOALDB db = GOALDB.getGOALDB(this);
        List<GOALBL> list =  db.goalDAO().getAllGoals();
        TableLayout tl = findViewById(R.id.tableLayout);
        tl.removeAllViews();
        tl.addView(header);
        goalHeader();



        for(GOALBL g : list){
            TableRow tr = new TableRow(this);
            double cost = 0;
            switch (g.getCategory()){
                case "Fixed": cost = fixedCost();
                    break;
                case "Flexible": cost = flexCost();
                    break;
                case "Discretionary": cost = discCost();
                    break;

            }
            //show Remaining
            TextView goal = new TextView(this);
            goal.setText(String.format("%.2f",g.getGoal() - cost ));
            goal.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tr.addView(goal);

            //show Status
            TextView progress = new TextView(this);
            progress.setText(g.getStatus());
            progress.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tr.addView(progress);

            //show cat
            TextView cat = new TextView(this);
            cat.setText(g.getCategory());
            cat.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tr.addView(cat);

            //show start
            TextView start = new TextView(this);
            start.setText(g.getStart().substring(0,9));
            start.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tr.addView(start);

            //show end
            TextView end = new TextView(this);
            end.setText(g.getEnd());
            end.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tr.addView(end);

            tl.addView(tr);
        }

    }

    private void tableHeader(){
        TextView txt1 = (TextView) header.getChildAt(0);
        txt1.setText("Cost");

        txt1 = (TextView) header.getChildAt(1);
        txt1.setText("Quantity");

        txt1 = (TextView) header.getChildAt(2);
        txt1.setText("Category");

        txt1 = (TextView) header.getChildAt(3);
        txt1.setText("Comment");

        txt1 = (TextView) header.getChildAt(4);
        txt1.setText("Date");

    }
    private void goalHeader(){
        TextView txt1 = (TextView) header.getChildAt(0);
        txt1.setText("Goal");

        txt1 = (TextView) header.getChildAt(1);
        txt1.setText("Status");

        txt1 = (TextView) header.getChildAt(2);
        txt1.setText("Category");

        txt1 = (TextView) header.getChildAt(3);
        txt1.setText("Start");

        txt1 = (TextView) header.getChildAt(4);
        txt1.setText("End");
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
        header = findViewById(R.id.header);


        tl.removeAllViews();
        tl.addView(header);
        tableHeader();

        if (sort_by == 0) {  //GETS ALL EXPENSES FROM DATABASE
            //GET TUPLES FROM DATABASE
            expenses = dataBase.expDAO().getAllExpenses();
            displayTable(expenses, tl);

        } else if (sort_by == 1) {
            expenses = dataBase.expDAO().getFixedExpenses();
            displayTable(expenses, tl);

        } else if (sort_by == 2) {
            expenses = dataBase.expDAO().getFlexibleExpenses();
            displayTable(expenses, tl);

        } else if (sort_by == 3) {
            expenses = dataBase.expDAO().getDiscretionaryExpenses();
            displayTable(expenses, tl);

        }else if (sort_by == 4){
            showAllGoals();



        }
        else {
            Log.e("SPINNER", "Somehow someone selected a category not on the list");
        }



    }

    private void displayTable(List<EXPTBL> expenses, TableLayout tl) {
        //Use iterator to go through list easily to list all elements
        Iterator<EXPTBL> expensesIterator = expenses.iterator();

        while (expensesIterator.hasNext()) {
            TableRow tr = new TableRow(this);

            EXPTBL current = expensesIterator.next();
           // String expenseOut = current.toString();
            Log.i("Inputting into Table:  ", "HELLO " + current.getCategory());


            //cost
            TextView cost = new TextView(this);
            cost.setText(String.format("%.2f", current.getCost()));
            cost.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tr.addView(cost);

            //quantity
            TextView quantity = new TextView(this);
            quantity.setText(Integer.toString(current.getQuantity()));
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
            date.setText(current.getDate().substring(0,9));
            date.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tr.addView(date);

            tl.addView(tr);
        }
    }
    private void deleteAll(){
        ENTDB dataBase=ENTDB.getENTDB(getApplicationContext());
        dataBase.expDAO().deleteAllExpenses();
        GOALDB db = GOALDB.getGOALDB(this);
        db.goalDAO().deleteAllGoals();

        updateTable();

    }

    private double fixedCost(){
        //get the total fixed cost within the current fixed goals timeframe
        double cost = 0.0;

        ENTDB dataBase = ENTDB.getENTDB(getApplicationContext());
        GOALBL fixed = currentFixed();
        if (fixed != null){
            DateTimeFormatter dt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
            DateTime start = dt.parseDateTime(fixed.getStart());


            List<EXPTBL> list = dataBase.expDAO().getFixedExpenses();
            Iterator<EXPTBL> it = list.iterator();
            while(it.hasNext()){
                EXPTBL exp = it.next();
                DateTime day = dt.parseDateTime(exp.getDate());

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
            DateTimeFormatter dt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
            DateTime start = dt.parseDateTime(flex.getStart());

            List<EXPTBL> list = dataBase.expDAO().getFlexibleExpenses();
            Iterator<EXPTBL> it = list.iterator();
            while(it.hasNext()){
                EXPTBL exp = it.next();
                DateTime day = dt.parseDateTime(exp.getDate());

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
        GOALBL disc = currentDiscretionary();
        if (disc != null){
            DateTimeFormatter dt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
            DateTime start = dt.parseDateTime(disc.getStart());

            List<EXPTBL> list = dataBase.expDAO().getDiscretionaryExpenses();
            Iterator<EXPTBL> it = list.iterator();
            while(it.hasNext()){
                EXPTBL exp = it.next();
                DateTime day = dt.parseDateTime(exp.getDate());

                if(!day.isBefore(start)){ //if the date of the expense happens before the goal was set, dont update the goal
                    cost += exp.getQuantity() * exp.getCost();
                }

            }
        }

        return cost;
    }

    private void threeFails(){
        int fails = GOALDB.getGOALDB(this).goalDAO().numFail();
        if( fails > 2 && fails < 6){
            showPopup("Resource One");
        }
        else if (fails > 5 && fails < 9 ){
            showPopup("Resource two");
        }



    }

    private void showPopup(String s){
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setMessage(s);
        build.setCancelable(true) ;
        build.setPositiveButton(
            "Yes",
         new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();        
         }
         });







        AlertDialog a = build.create();
        a.show();
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
        else if( id == R.id.about){
            Intent intentAbout = new Intent(MainActivity.this, About.class);
            startActivity(intentAbout);
        }

        return super.onOptionsItemSelected(item);
    }
}
