package com.example.bighomework.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class NewsSearchItem {
    @PrimaryKey(autoGenerate = true)
    private int _id;

    @ColumnInfo(name = "news_id")
    private String newsId;
    @ColumnInfo(name = "title")
    private String title;

    private String time;
    private String type;

    public NewsSearchItem(String newsId, String title, String time, String type) {
        this.newsId = newsId;
        this.title = title;
        this.time = time;
        this.type = type;
    }

    public NewsSearchItem() {
        this("", "", "", "");
    }


    public int get_id() {
        return _id;
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    public void set_id(int _id) {
        this._id = _id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
