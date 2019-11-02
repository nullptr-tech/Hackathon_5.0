package com.meeting.meetingapp;

import android.app.Application;
import android.os.SystemClock;

public class Screen extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SystemClock.sleep(600);
    }
}
