package com.java.yangzhuoyi.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NewsFavorDao {
    @Insert
    void insertNewsFavor(NewsFavor... news);

    @Query("SELECT * FROM NewsFavor WHERE news_id = :id")
    List<NewsFavor> getNewsWithId(String id);

    @Query("SELECT * FROM NewsFavor ORDER BY _ID DESC")
    List<NewsFavor> getAllFavor();

}
