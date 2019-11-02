package com.meeting.meetingapp;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Meeting {
    private String title;
    private String desc;
    private Object times;
    private String pending;

    public Meeting() {

    }
    public Meeting(String title, String desc, Object times, String pending) {
        this.title = title;
        this.desc = desc;
        this.times = times;
        this.pending = pending;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPending(){return pending;}

    public void setPending(){this.pending=pending;}

}