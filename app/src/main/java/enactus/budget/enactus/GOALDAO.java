package enactus.budget.enactus;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by dmitry on 2/19/18.
 */
@Dao
public interface GOALDAO {

    @Insert
    void insertGoal(GOALBL... goalbls);



    @Query("SELECT * FROM goals")
    List<GOALBL> getAllGoals();

    @Query("SELECT * FROM goals WHERE category=\"Fixed\" and status=\"In Progress\" ")
    List<GOALBL> getCurrentFixedGoal();

    @Query("SELECT * FROM goals WHERE category=\"Flexible\" and status=\"In Progress\" ")
    List<GOALBL> getCurrentFlexibleGoal();

    @Query("SELECT * FROM goals WHERE category=\"Discretionary\" and status=\"In Progress\" ")
    List<GOALBL> getCurrentDiscretionaryGoal();

    @Query("DELETE FROM goals")
    void deleteAllGoals();

    @Query("UPDATE goals SET status = \"Pass\" WHERE category= :cat and status=\"In Progress\" ")
    void goalPassed(String cat);

    @Query("UPDATE goals SET status = \"Fail\" WHERE category= :cat and status=\"In Progress\" ")
    void goalFailed(String cat);

    @Query("UPDATE goals SET progress= :prog WHERE category= :cat and status=\"In Progress\" ")
    void updateProgress(double prog, String cat);

    @Query("SELECT COUNT(*) from goals WHERE status=\"Pass\" ")
    int numPass();

    @Query("SELECT COUNT(*) from goals WHERE status=\"Fail\" ")
    int numFail();


}
