package enactus.budget.enactus;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by dmitry on 2/19/18.
 */
@Database(entities={EXPTBL.class},version = 1)
public abstract class ENTDB extends RoomDatabase {
    private static ENTDB INSTANCE;

    public abstract ENTDAO expDAO();

    public static ENTDB getENTDB(Context context){
        if(INSTANCE==null){

            //BAD DESIGN, DO NOT ALLOW MAIN THREAD QUERIES
            //SHOULD ONLY BE MADE FROM INITIALIZER AND CALLED FROM METHODS

            INSTANCE= Room.databaseBuilder(context.getApplicationContext(),ENTDB.class,"EXP-DB")
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;

    }

    public static void destroyInstance(){
        INSTANCE=null;
    }


}
