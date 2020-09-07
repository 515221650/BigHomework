package com.java.yangzhuoyi.util;

public class NewsDigest {
    String title;
    String digest;
    String time;
    String source;
    String Id;
    boolean hasRead = false;

    public NewsDigest() {
        title = digest = time = source = Id = "";
        hasRead = false;
    }

    public void setTitle(String t){title = t;}
    public void setDigest(String d){digest = d;}
    public void setTime(String t){time = t;}
    public void setSource(String s){source = s;}
    public void setId(String i){Id = i;}
    public void setHasRead(boolean s){hasRead = s;}

    public String getTitle(){return title;}
    public String getDigest(){return digest;}
    public String getTime(){return time;}
    public String getSource(){return source;}
    public String getId(){return Id;}
    public boolean getHasRead(){return hasRead;}
}
