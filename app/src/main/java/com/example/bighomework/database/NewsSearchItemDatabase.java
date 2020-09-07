package com.example.bighomework.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

//singleton
@Database(entities = {NewsSearchItem.class}, version = 1, exportSchema = false)
public abstract class NewsSearchItemDatabase extends RoomDatabase {
    private static NewsSearchItemDatabase INSTANCE;
    public static synchronized NewsSearchItemDatabase getDatabase(Context context)
    {
        if(INSTANCE == null)
        {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    NewsSearchItemDatabase.class,
                    "newssearch_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;

    }
    public abstract NewsSearchItemDao getNewsSearchItemDao();
}
