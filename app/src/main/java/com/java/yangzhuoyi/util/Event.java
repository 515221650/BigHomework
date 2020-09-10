package com.java.yangzhuoyi.util;

import java.io.Serializable;
import java.util.List;

public class Event implements Serializable {
    public static Event event;
    public static void setInstance(Event newEvent) {
        event = newEvent;
    }
    public static Event getInstance()
    {
        return event;
    }
    public List<NewsDigest> events;
    public String[] keywords = new String[3];
    public String[] hotnumber = new String[3];
}
