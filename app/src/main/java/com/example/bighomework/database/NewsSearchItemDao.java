package com.example.bighomework.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NewsSearchItemDao {
    @Insert
    void insertNewsSearch(NewsSearchItem... news);

    @Query("DELETE FROM NEWSSEARCHITEM")
    void deleteAllNews();

    @Query("SELECT * FROM NEWSSEARCHITEM WHERE title LIKE '%'|| :keyword || '%'")
    List<NewsSearchItem> getNewsContains(String keyword);
}
