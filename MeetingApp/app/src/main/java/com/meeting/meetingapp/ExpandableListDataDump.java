package com.meeting.meetingapp;

import android.widget.ListView;
import android.widget.TextView;

import java.nio.file.WatchEvent;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ExpandableListDataDump {
    public static LinkedHashMap<String, List<String>> getData() {
        LinkedHashMap<String, List<String>> expandableListDetail = new LinkedHashMap<String, List<String>>();

        List<String> Monday = new ArrayList<String>();
        Monday.add("09:00am-11:00am");
        Monday.add("11:00am-01:00pm");
        Monday.add("01:00pm-03:00pm");
        Monday.add("03:00pm-05:00pm");
        Monday.add("05:00pm-07:00pm");
        Monday.add("07:00pm-09:00pm");

        List<String> Tuesday = new ArrayList<String>();
        Tuesday.add("09:00am-11:00am");
        Tuesday.add("11:00am-01:00pm");
        Tuesday.add("01:00pm-03:00pm");
        Tuesday.add("03:00pm-05:00pm");
        Tuesday.add("05:00pm-07:00pm");
        Tuesday.add("07:00pm-09:00pm");

        List<String> Wednesday = new ArrayList<String>();
        Wednesday.add("09:00am-11:00am");
        Wednesday.add("11:00am-01:00pm");
        Wednesday.add("01:00pm-03:00pm");
        Wednesday.add("03:00pm-05:00pm");
        Wednesday.add("05:00pm-07:00pm");
        Wednesday.add("07:00pm-09:00pm");

        List<String> Thursday = new ArrayList<String>();
        Thursday.add("09:00am-11:00am");
        Thursday.add("11:00am-01:00pm");
        Thursday.add("01:00pm-03:00pm");
        Thursday.add("03:00pm-05:00pm");
        Thursday.add("05:00pm-07:00pm");
        Thursday.add("07:00pm-09:00pm");

        List<String> Friday = new ArrayList<String>();
        Friday.add("09:00am-11:00am");
        Friday.add("11:00am-01:00pm");
        Friday.add("01:00pm-03:00pm");
        Friday.add("03:00pm-05:00pm");
        Friday.add("05:00pm-07:00pm");
        Friday.add("07:00pm-09:00pm");


        List<String> Saturday = new ArrayList<String>();
        Saturday.add("09:00am-11:00am");
        Saturday.add("11:00am-01:00pm");
        Saturday.add("01:00pm-03:00pm");
        Saturday.add("03:00pm-05:00pm");
        Saturday.add("05:00pm-07:00pm");
        Saturday.add("07:00pm-09:00pm");


        List<String> Sunday = new ArrayList<String>();
        Sunday.add("09:00am-11:00am");
        Sunday.add("11:00am-01:00pm");
        Sunday.add("01:00pm-03:00pm");
        Sunday.add("03:00pm-05:00pm");
        Sunday.add("05:00pm-07:00pm");
        Sunday.add("07:00pm-09:00pm");


        expandableListDetail.put("MONDAY", Monday);
        expandableListDetail.put("TUESDAY", Tuesday);
        expandableListDetail.put("WEDNESDAY", Wednesday);
        expandableListDetail.put("THURSDAY", Thursday);
        expandableListDetail.put("FRIDAY", Friday);
        expandableListDetail.put("SATURDAY", Saturday);
        expandableListDetail.put("SUNDAY", Sunday);

        return expandableListDetail;
    }
}