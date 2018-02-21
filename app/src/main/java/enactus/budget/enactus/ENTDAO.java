package enactus.budget.enactus;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by dmitry on 2/19/18.
 */
@Dao
public interface ENTDAO {

    //Query to Select all attributes from expenses
    @Query("SELECT * FROM expenses")
    List<EXPTBL> getAllExpenses();

    //Insert extbls(1 or more) into expenses
    @Insert
    void insertExpenses(EXPTBL... exptbls);


    //Delete exptbl from expenses
    @Delete
    void deleteExpense(EXPTBL exptbl);

    @Query("DELETE FROM expenses")
    void deleteAllExpenses();

    @Query("SELECT * FROM expenses WHERE category=\"Fixed\" ")
    List<EXPTBL> getFixedExpenses();

    @Query("SELECT * FROM expenses WHERE category=\"Flexible\" ")
    List<EXPTBL> getFlexibleExpenses();

    @Query("SELECT * FROM expenses WHERE category=\"Discretionary\" ")
    List<EXPTBL> getDiscretionaryExpenses();

}
