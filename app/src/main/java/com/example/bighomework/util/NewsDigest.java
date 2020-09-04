package com.example.bighomework.util;

public class NewsDigest {
    String title;
    String digest;
    String time;
    String source;
    String Id;

    public void setTitle(String t){title = t;}
    public void setDigest(String d){digest = d;}
    public void setTime(String t){time = t;}
    public void setSource(String s){source = s;}
    public void setId(String i){Id = i;}

    public String getTitle(){return title;}
    public String getDigest(){return digest;}
    public String getTime(){return time;}
    public String getSource(){return source;}
    public String getId(){return Id;}
}
