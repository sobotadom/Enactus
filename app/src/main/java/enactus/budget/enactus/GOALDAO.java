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
public interface GOALDAO {

    @Insert
    void insertGoal(GOALBL... goalbls);

    @Query("SELECT * FROM goals WHERE category=\"Fixed\" and status=\"In Progress\" ")
    List<GOALBL> getCurrentFixedGoal();

    @Query("SELECT * FROM goals WHERE category=\"Flexible\" and status=\"In Progress\" ")
    List<GOALBL> getCurrentFlexibleGoal();

    @Query("SELECT * FROM goals WHERE category=\"Discretionary\" and status=\"In Progress\" ")
    List<GOALBL> getCurrentDiscretionaryGoal();

    @Query("DELETE FROM goals")
    void deleteAllGoals();
}
