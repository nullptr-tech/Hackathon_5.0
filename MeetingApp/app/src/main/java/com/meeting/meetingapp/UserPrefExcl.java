package com.meeting.meetingapp;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.meeting.meetingapp.adapter.UserChatAdapter2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserPrefExcl extends AppCompatActivity {
    Bundle b;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference mDatabase = database.getReference();
    ArrayList<String> prefTimesToDisplay;
    ArrayList<String> exclTimesToDisplay;
    ArrayAdapter<String> adapter1;
    ArrayAdapter<String> adapter2;
    ListView prefView;
    ListView exclView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_pref_excl);
        Intent iin = getIntent();
        b = iin.getExtras();
        final int position =(int) b.get("position");
        final ArrayList<String> usersToDisplayKeys = (ArrayList<String>) b.get("usersToDisplayKeys");
        prefTimesToDisplay = new ArrayList<String>();
        exclTimesToDisplay = new ArrayList<String>();
        adapter1 = new ArrayAdapter<String>(this, R.layout.user_info, R.id.userInfo, prefTimesToDisplay);
        adapter2 = new ArrayAdapter<String>(this, R.layout.user_info, R.id.userInfo, exclTimesToDisplay);
        prefView = (ListView) findViewById(R.id.listOfPrefUser);
        exclView = (ListView) findViewById(R.id.listOfExclUser);
        DatabaseReference refTwo = database.getReference();
        refTwo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<List<Integer>> conflictListDays = new ArrayList<List<Integer>>();
                List<List<String>> listOfUserTimesPref = (List<List<String>>)dataSnapshot.child("users").child(usersToDisplayKeys.get(position)).child("prefTimes").getValue();
                List<List<String>> listOfUserTimesExcl = (List<List<String>>)dataSnapshot.child("users").child(usersToDisplayKeys.get(position)).child("exclTimes").getValue();
                ArrayList<String> emptyList = new ArrayList<String>() {{
                    add("");
                    add("");
                    add("");
                    add("");
                    add("");
                    add("");
                    add("");
                }};
                if (listOfUserTimesExcl == null){
                    listOfUserTimesExcl = new ArrayList<>();
                    for( int i = 0 ; i < 7 ; i++ ) {
                        listOfUserTimesExcl.add(emptyList);
                    }
                }
                if (listOfUserTimesPref == null){
                    listOfUserTimesPref = new ArrayList<>();
                    for( int i = 0 ; i < 7 ; i++ ) {
                        listOfUserTimesPref.add(emptyList);
                    }
                }
                String[] dayList = {"Mon", "Tues", "Wed", "Thurs", "Fri", "Sat", "Sun"};
                List<String> timeList = Arrays.asList("8:00 - 10:00","10:00 - 12:00","12:00 - 14:00","14:00 - 16:00","16:00 - 18:00","18:00 - 20:00");
                for (int day = 0; day < listOfUserTimesPref.size(); day++) {
                    for (int timeSlot = 0; timeSlot < listOfUserTimesPref.get(day).size(); timeSlot++) {
                        if (listOfUserTimesPref.get(day).get(timeSlot).length() > 0) {
                            prefTimesToDisplay.add(timeList.get(timeSlot) + "" + dayList[day]);
                        }
                    }

                }
                for (int day = 0; day < listOfUserTimesExcl.size(); day++) {
                    for (int timeSlot = 0; timeSlot < listOfUserTimesExcl.get(day).size(); timeSlot++) {
                        if (listOfUserTimesExcl.get(day).get(timeSlot).length() > 0) {
                            exclTimesToDisplay.add(timeList.get(timeSlot) + "" + dayList[day]);
                        }
                    }

                }
                exclView.setAdapter(adapter2);
                prefView.setAdapter(adapter1);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


    }
}

