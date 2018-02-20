package enactus.budget.enactus;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by dmitry on 2/19/18.
 */
@Entity(tableName = "expenses")
public class EXPTBL {

    //Primary Key for Expenses, will be autogenerated by ROOM
    @PrimaryKey(autoGenerate = true)
    private int eid;

    //Cost of expense
    @ColumnInfo(name="cost")
    private double cost;

    //Quantity of expense
    @ColumnInfo(name="quantity")
    private int quantity;

    //Category of expense
    //TODO: Change to type Category?? Or leave as String?
    @ColumnInfo(name="category")
    private String category;


    //DATE of expense
    @ColumnInfo(name="date")
    private String date;

    //Comment of expense
    @ColumnInfo(name="comment")
    private String comment;


    //Getters and Setters for variables in table

    public int getEid() {
        return eid;
    }

    public void setEid(int eid) {
        this.eid = eid;
    }


    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString(){
        return "eid: "+eid+" cost: "+cost+" quantity: "+quantity+" comment: "+comment+" category: " +category;
    }



}
