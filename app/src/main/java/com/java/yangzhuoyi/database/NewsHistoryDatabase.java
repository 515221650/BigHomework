package com.java.yangzhuoyi.database;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {NewsHistory.class}, version = 1, exportSchema = false)
public abstract class NewsHistoryDatabase extends RoomDatabase {
    private static NewsHistoryDatabase INSTANCE;
    public static synchronized NewsHistoryDatabase getDatabase(Context context)
    {
        if(INSTANCE == null)
        {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    NewsHistoryDatabase.class,
                    "newshistory_database")
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }

    public abstract NewsHistoryDao getNewsHistoryDao();
}
