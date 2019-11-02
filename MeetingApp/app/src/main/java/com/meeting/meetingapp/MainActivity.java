package com.meeting.meetingapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import com.meeting.meetingapp.adapter.UsersChatAdapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ValueEventListener;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity{
    private static String TAG =  MainActivity.class.getSimpleName();

    private String mCurrentUserUid;
    private List<String> mUsersKeyList;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mUserRefDatabase;
    private ChildEventListener mChildEventListener;
    ProgressBar mProgressBarForUsers;
    Toolbar toolbar;
    ListView listView;
    FirebaseDatabase database;
    DatabaseReference ref;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;
    Meeting meeting;
    Button createMeeting;
    ArrayList<String> listOfKeys;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_main);


        meeting = new Meeting();

        createMeeting = (Button) findViewById(R.id.create_Meeting);
        listView = (ListView) findViewById(R.id.listView);

        toolbar = (Toolbar)findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        list = new ArrayList<>();
        listOfKeys = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, R.layout.user_info, R.id.userInfo, list);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                listOfKeys.clear();
                for (DataSnapshot ds : dataSnapshot.child("meetings").getChildren()) {
                    meeting = ds.getValue(Meeting.class);
                    Object timesObject = ds.child("times").getValue();
                    boolean earlyTimeFound = false;
                    boolean lateTimeFound = false;
                    String earlyTime = "";
                    String earlyDay = "";
                    String lateTime = "";
                    String lateDay = "";
                    String[] dayList = {"Mon", "Tues", "Wed", "Thurs", "Fri", "Sat", "Sun"};
                    if (timesObject instanceof ArrayList){
                        ArrayList<ArrayList<String>> timeList = (ArrayList<ArrayList<String>>) timesObject;
                        if (timeList != null) {
                            for (int day = 0; day < timeList.size(); day++) {
                                if (timeList.get(day) != null) {
                                    for (int timeSlot = 0; timeSlot < timeList.get(day).size(); timeSlot++) {
                                        if (timeList.get(day).get(timeSlot) != null && !timeList.get(day).get(timeSlot).equals("")) {
                                            earlyTimeFound = true;
                                            earlyTime = timeList.get(day).get(timeSlot);
                                            earlyDay = dayList[day];
                                            break;
                                        }
                                    }
                                    if (earlyTimeFound == true) {
                                        break;
                                    }
                                }
                            }
                            for (int day = timeList.size() - 1; day > -1; day--) {
                                if (timeList.get(day) != null) {
                                    for (int timeSlot = timeList.get(day).size() - 1; timeSlot > -1; timeSlot--) {
                                        if (timeList.get(day).get(timeSlot) != null && !timeList.get(day).get(timeSlot).equals("")) {
                                            lateTimeFound = true;
                                            lateTime = timeList.get(day).get(timeSlot);
                                            lateDay = dayList[day];
                                            break;
                                        }
                                    }
                                    if (lateTimeFound == true) {
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    else{
                        HashMap timeList = (HashMap) timesObject;

                        if (timeList != null) {
                            List<String> keyList = new ArrayList<String>(timeList.keySet());
                            Collections.sort(keyList);
                            int size = Integer.valueOf(keyList.get(keyList.size() - 1)) + 1;
                            for (int day = 0; day < size; day++) {
                                ArrayList<String> tempList = (ArrayList<String>) timeList.get(String.valueOf(day));
                                if (tempList != null) {
                                    for (int timeSlot = 0; timeSlot < size; timeSlot++) {
                                        if (tempList.get(timeSlot) != null && !tempList.get(timeSlot).equals("")) {
                                            earlyTimeFound = true;
                                            earlyTime = tempList.get(timeSlot);
                                            earlyDay = dayList[day];
                                            break;
                                        }
                                    }
                                    if (earlyTimeFound == true) {
                                        break;
                                    }
                                }
                            }
                            for (int day = size - 1; day > -1; day--) {
                                ArrayList<String> tempList = (ArrayList<String>) timeList.get(String.valueOf(day));
                                if (tempList != null) {
                                    for (int timeSlot = size - 1; timeSlot > -1; timeSlot--) {
                                        if (tempList.get(timeSlot) != null && !tempList.get(timeSlot).equals("")) {
                                            lateTimeFound = true;
                                            lateTime = tempList.get(timeSlot);
                                            lateDay = dayList[day];
                                            break;
                                        }
                                    }
                                    if (lateTimeFound == true) {
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    int dashIndex = earlyTime.indexOf("-");
                    if (dashIndex != -1)
                    {
                        earlyTime = earlyTime.substring(0, dashIndex);
                    }

                    dashIndex = lateTime.indexOf("-");
                    if (dashIndex != -1)
                    {
                        lateTime = lateTime.substring(0, dashIndex);
                    }

                    String search = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    String pendingString = meeting.getPending();
                    if (pendingString == null){
                        pendingString = "scheduled";
                    }

                    for(String str: (List<String>)ds.child("participants").getValue()) {
                        if(str.equals(search)){
                            if (earlyDay == lateDay){
                                if (pendingString.equals("scheduled")){
                                    Long tempDay = (Long) ds.child("setDay").getValue();
                                    Long tempTime = (Long) ds.child("setTime").getValue();
                                    List<String> times = Arrays.asList("8:00 - 10:00","10:00 - 12:00","12:00 - 14:00","14:00 - 16:00","16:00 - 18:00","18:00 - 20:00");
                                    list.add(meeting.getTitle() + "\n" + times.get(tempTime.intValue()) + " " + dayList[tempDay.intValue()]+ "\n" + pendingString);
                                }
                                else{
                                    list.add(meeting.getTitle() + "\n" + earlyTime + " - " + lateTime + " " + earlyDay + "\n" + pendingString);
                                }

                                listOfKeys.add(ds.getKey());
                            }
                            else{
                                list.add(meeting.getTitle() + "\n" + earlyTime + " " + earlyDay + "  - " + lateTime + " " + lateDay + "\n" + meeting.getPending());
                                listOfKeys.add(ds.getKey());
                            }
                        }

                    }
                }
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, MeetingScreen.class);
                intent.putExtra("listOfKeys", listOfKeys);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });

        createMeeting.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (v == createMeeting){
                    Intent intent = new Intent(MainActivity.this, SelectParticipants.class);
                    startActivity(intent);
                }
            }
        });

        toolbar = (Toolbar)findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);


        setAuthInstance();
        setUsersDatabase();
        setUsersKeyList();
        setAuthListener();
    }

    private void setAuthInstance() {
        mAuth = FirebaseAuth.getInstance();
    }

    private void setUsersDatabase() {
        mUserRefDatabase = FirebaseDatabase.getInstance().getReference().child("users");
    }


    private void setUsersKeyList() {
        mUsersKeyList = new ArrayList<String>();
    }

    private void setAuthListener() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    setUserData(user);
                } else {
                    // User is signed out
                    goToLogin();
                }
            }
        };
    }

    private void setUserData(FirebaseUser user) {
        mCurrentUserUid = user.getUid();
    }

    private void goToLogin() {
        Intent intent = new Intent(this, Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // LoginActivity is a New Task
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); // The old task when coming back to this activity should be cleared so we cannot come back to it.
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();

        clearCurrentUsers();

        if (mChildEventListener != null) {
            mUserRefDatabase.removeEventListener(mChildEventListener);
        }

        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }

    }

    private void clearCurrentUsers() {
        mUsersKeyList.clear();
    }

    private void logout() {
        setUserOffline();
        mAuth.signOut();
    }

    private void setUserOffline() {
        if(mAuth.getCurrentUser()!=null ) {
            String userId = mAuth.getCurrentUser().getUid();
            mUserRefDatabase.child(userId).child("connection").setValue(UsersChatAdapter.OFFLINE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_logout){
            logout();
            return true;
        }
        else if (item.getItemId()==R.id.setExclusion){
            Intent intent = new Intent(MainActivity.this, SetPrefDates.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }




}