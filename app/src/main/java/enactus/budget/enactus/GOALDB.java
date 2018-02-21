package enactus.budget.enactus;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by dmitry on 2/19/18.
 */
@Database(entities={GOALBL.class},version = 1)
public abstract class GOALDB extends RoomDatabase {
    private static GOALDB INSTANCE;

    public abstract GOALDAO goalDAO();

    public static GOALDB getGOALDB(Context context){
        if(INSTANCE==null){

            //BAD DESIGN, DO NOT ALLOW MAIN THREAD QUERIES
            //SHOULD ONLY BE MADE FROM INITIALIZER AND CALLED FROM METHODS

            INSTANCE= Room.databaseBuilder(context.getApplicationContext(),GOALDB.class,"GOAL-DB")
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;

    }

    public static void destroyInstance(){
        INSTANCE=null;
    }


}
