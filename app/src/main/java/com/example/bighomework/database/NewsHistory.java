package com.example.bighomework.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class NewsHistory {
    @PrimaryKey(autoGenerate = true)
    private int _id;

    @ColumnInfo(name = "news_id")
    private String newsId;
    private String title;
    private String time;
    private String source;
    private String content;

    public NewsHistory(String newsId, String title, String time, String source, String content) {
        this.newsId = newsId;
        this.title = title;
        this.time = time;
        this.source = source;
        this.content = content;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
