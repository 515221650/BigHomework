package com.example.bighomework.database;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {NewsFavor.class}, version = 1, exportSchema = false)
public abstract class NewsFavorDatabase extends RoomDatabase {
    private static NewsFavorDatabase INSTANCE;
    public static synchronized NewsFavorDatabase getDatabase(Context context)
    {
        if(INSTANCE == null)
        {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    NewsFavorDatabase.class,
                    "newsfavor_database")
                    .build();
        }
        return INSTANCE;
    }

    public abstract NewsFavorDao getNewsFavorDao();
}
