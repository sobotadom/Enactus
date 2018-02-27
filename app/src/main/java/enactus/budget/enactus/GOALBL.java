package enactus.budget.enactus;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "goals")
public class GOALBL {

    //Primary Key for GOALS, will be autogenerated by ROOM
    @PrimaryKey(autoGenerate = true)
    private int gid;

    //goal cat
    @ColumnInfo(name="category")
    private String category;
    //goal price
    @ColumnInfo(name="goal")
    private double goal;

    //start date
    @ColumnInfo(name="start")
    private String start;

    //end_date
    @ColumnInfo(name="end")
    private String end;

    //status of goal
    @ColumnInfo(name="status")
    private String status;

    //progress of goal
    @ColumnInfo(name="progress")
    private double progress;


    //Getters and Setters for variables in table

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public String getCategory(){ return category;}

    public void setCategory(String category){ this.category = category;}

    public double getGoal() {
        return goal;
    }

    public void setGoal(double goal) {
        this.goal = goal;
    }


    public String getStart() {
        return start;
    }

    public void setStart(String date) {
        this.start = date;
    }


    public String getEnd() {
        return end;
    }

    public void setEnd(String date) {
        this.end = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String stat) {
        this.status = stat;
    }

    public double getProgress(){return progress;}
    public void setProgress(double expense){this.progress = expense;}
    public void addProgress(double expense){this.progress += expense;}




}
