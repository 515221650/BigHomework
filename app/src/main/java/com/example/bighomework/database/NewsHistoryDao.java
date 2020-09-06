package com.example.bighomework.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NewsHistoryDao {
    @Insert
    void insertNewsHistory(NewsHistory... news);

    @Query("SELECT * FROM NewsHistory WHERE news_id = :id")
    List<NewsHistory> getNewsWithId(String id);
}
