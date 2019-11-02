package com.meeting.meetingapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.meeting.meetingapp.adapter.UsersChatAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MeetingScreen extends MainActivity{
    DataSnapshot DStore;
    DatabaseReference refTwo;
    Bundle b;
    ArrayList<String> listOfEachTimeDisplay;
    ArrayList<String> listOfEachTimeDisplay2;
    List<List<String>> listOfEachTime;
    List<String> listOfParticpants;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mUserRefDatabase;
    private ChildEventListener mChildEventListener;
    private String mCurrentUserUid;
    private List<String> mUsersKeyList;
    int oldPosition;
    Toolbar toolbar;
    int tempPosition;
    ArrayList<Integer> listOfConfPref;
    ArrayList<Integer> listOfDays;
    ArrayList<Integer> listOfTimes;

    DataSnapshot tempStore;


    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_meeting_screen);

        toolbar = (Toolbar)findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);


        final TextView descDisplay, titleDisplay, roomDisplay;
        final ListView timeDisplay;
        final Button submit;

        titleDisplay = (TextView) findViewById(R.id.displayTitle);
        descDisplay = (TextView) findViewById(R.id.displayDescription);
        roomDisplay = (TextView) findViewById(R.id.roomNumText);
        timeDisplay = (ListView) findViewById(R.id.viewFinalHours);
        listOfEachTimeDisplay = new ArrayList<>();
        listOfEachTimeDisplay2 = new ArrayList<>();

        adapter = new ArrayAdapter<String>(this, R.layout.user_info, R.id.userInfo, listOfEachTimeDisplay);

        Intent iin = getIntent();
        b = iin.getExtras();
        mCurrentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (b != null) {
            database = FirebaseDatabase.getInstance();
            refTwo = database.getReference();
            refTwo.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    tempStore = dataSnapshot;
                    listOfDays = new ArrayList<>();
                    listOfTimes = new ArrayList<>();
                    listOfConfPref = new ArrayList<Integer>();
                    List<String> listOfKeys = (List<String>) b.get("listOfKeys");
                    int position = (int) b.get("position");
                    tempPosition = position;
                    oldPosition = (int) b.get("position");
                    String[] dayList = {"Mon", "Tues", "Wed", "Thurs", "Fri", "Sat", "Sun"};
                    DStore = dataSnapshot;
                    titleDisplay.setText(DStore.child("meetings").child(listOfKeys.get(position)).child("title").getValue().toString());
                    descDisplay.setText(DStore.child("meetings").child(listOfKeys.get(position)).child("desc").getValue().toString());
                    if (DStore.child("meetings").child(listOfKeys.get(position)).child("meetingRoom").getValue() != null) {
                        roomDisplay.setText(DStore.child("meetings").child(listOfKeys.get(position)).child("meetingRoom").getValue().toString());
                    }

                    //timeDisplay.setText(DStore.child(listOfKeys.get(position)).child("times").getValue().toString());

                    listOfEachTime = (List<List<String>>) DStore.child("meetings").child(listOfKeys.get(position)).child("times").getValue();
                    listOfParticpants = (List<String>) DStore.child("meetings").child(listOfKeys.get(position)).child("participants").getValue();
                    Log.d("test", listOfEachTime.toString());
                    for (int day = 0; day < listOfEachTime.size(); day++) {
                        for (int timeSlot = 0; timeSlot < listOfEachTime.get(day).size(); timeSlot++) {
                            if (listOfEachTime.get(day).get(timeSlot).length() > 0) {
                                listOfEachTimeDisplay.add(listOfEachTime.get(day).get(timeSlot) + " " + dayList[day] + ".");
                                listOfEachTimeDisplay2.add(listOfEachTime.get(day).get(timeSlot) + " " + dayList[day] + ".");
                                listOfDays.add(day);
                                listOfTimes.add(timeSlot);
                            }
                        }

                    }
                    //listOfEachTimeDisplay.add(DStore.child(listOfKeys.get(position)).child("times").getValue().toString());


                    int count = 0;
                    Map<String, List<List<Integer>>> userConflicts = new HashMap<String, List<List<Integer>>> ();

                    for (DataSnapshot userKey : DStore.child("users").getChildren()){
                        List<List<Integer>> conflictListDays = new ArrayList<List<Integer>>();
                        List<List<String>> listOfUserTimesPref = (List<List<String>>)DStore.child("users").child(userKey.getKey()).child("prefTimes").getValue();
                        List<List<String>> listOfUserTimesExcl = (List<List<String>>)DStore.child("users").child(userKey.getKey()).child("exclTimes").getValue();
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
                        for (int day = 0; day < listOfEachTime.size(); day++) {
                            List<Integer> conflictListTimes = new ArrayList<Integer>();
                            for (int timeSlot = 0; timeSlot < listOfEachTime.get(day).size(); timeSlot++) {
                                if (listOfEachTime.get(day).get(timeSlot).length() > 0) {

                                    // 0 is no conflict, 1 is user preference, 2 user exclusion
                                    int conflictLevel = 0;
                                    if (listOfUserTimesPref.get(day).get(timeSlot).equals(listOfEachTime.get(day).get(timeSlot))) {
                                        conflictLevel = 1;
                                    }
                                    else if (listOfUserTimesExcl.get(day).get(timeSlot).equals(listOfEachTime.get(day).get(timeSlot))){
                                        conflictLevel = 2;
                                    }
                                    for(DataSnapshot eachMeeting : dataSnapshot.child("meetings").getChildren()){
                                        long setDay = -1;
                                        long setTime = -1;
                                        String pendString = (String)eachMeeting.child("pending").getValue();
                                        if(pendString == null){
                                            pendString = "scheduled";
                                        }
                                        if(pendString.equals("scheduled")){
                                            setDay = (long) eachMeeting.child("setDay").getValue();
                                            setTime = (long) eachMeeting.child("setTime").getValue();
                                        }
                                        if(!eachMeeting.getKey().equals(listOfKeys.get(position))){
                                            for (int day2 = 0; day2 < 7; day2++) {
                                                for (int timeSlot2 = 0; timeSlot2 < 6 ; timeSlot2++) {
                                                    if(!eachMeeting.child("times").child(String.valueOf(day2)).child(String.valueOf(timeSlot2)).getValue().equals("")
                                                            && day2 == day && timeSlot == timeSlot2 && !pendString.equals("pending...") && day2 == setDay && timeSlot2 == setTime){
                                                        conflictLevel = 2;
                                                    }
                                                }

                                            }
                                        }

                                    }
                                    conflictListTimes.add(conflictLevel);


                                    count++;
                                }
                            }
                            conflictListDays.add(conflictListTimes);
                        }
                        userConflicts.put(userKey.getKey(), conflictListDays);
                    }
                    int count2 = 0;
                    for (int day = 0; day < listOfEachTime.size(); day++) {
                        int timeSlotCount = 0;
                        for (int timeSlot = 0; timeSlot < listOfEachTime.get(day).size(); timeSlot++) {
                            if (listOfEachTime.get(day).get(timeSlot).length() > 0) {
                                int overAll = 0;
                                for (DataSnapshot userKey2 : DStore.child("users").getChildren()){
                                    boolean isAParticipant = listOfParticpants.contains(userKey2.getKey());
                                    if(userConflicts.get(userKey2.getKey()).get(day).get(timeSlotCount) == 1 && overAll != 2 && isAParticipant){
                                        overAll = 1;
                                    }
                                    else if(userConflicts.get(userKey2.getKey()).get(day).get(timeSlotCount ) == 2 && isAParticipant){
                                        overAll = 2;

                                    }
                                }
                                if (overAll == 1){
                                    listOfEachTimeDisplay.set(count2, listOfEachTimeDisplay.get(count2) + "  Pref");
                                    listOfConfPref.add(overAll);

                                }
                                if (overAll == 2){
                                    listOfEachTimeDisplay.set(count2, listOfEachTimeDisplay.get(count2) + "  Conf!");
                                    listOfConfPref.add(overAll);
                                }
                                if (overAll == 0){
                                    listOfConfPref.add(overAll);
                                }

                                count2++;
                                timeSlotCount++;
                            }
                        }

                    }
                    timeDisplay.setAdapter(adapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });
        }

        timeDisplay.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MeetingScreen.this, CheckConflicts.class);
                intent.putExtra("listOfKeys", listOfKeys);
                intent.putExtra("oldPosition", oldPosition);
                intent.putExtra("position", position);
                intent.putExtra("listOfDays", listOfDays);
                intent.putExtra("listOfTimes", listOfTimes);
                intent.putExtra("listOfTimesToDisplay", listOfEachTimeDisplay2);
                startActivity(intent);
            }
        });


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
        getMenuInflater().inflate(R.menu.menu_meeting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_logout){
            logout();
            return true;
        }
        else if (item.getItemId()==R.id.setExclusion){
            Intent intent = new Intent(MeetingScreen.this, SetPrefDates.class);
            startActivity(intent);
            return true;
        }
        else if (item.getItemId()==R.id.action_edit){
            Intent intent = new Intent(MeetingScreen.this, Edit.class);
            intent.putExtra("listOfKeys", listOfKeys);
            intent.putExtra("position", tempPosition);
            startActivity(intent);
            return true;
        }
        else if (item.getItemId()==R.id.action_delete){
            database.getReference().child("meetings").child(listOfKeys.get(tempPosition)).removeValue();
            Intent intent = new Intent(MeetingScreen.this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        else if (item.getItemId()==R.id.action_drop_out){
            for (DataSnapshot part: tempStore.child("meetings").child(listOfKeys.get(tempPosition)).child("participants").getChildren()){
                if(part.getValue().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                    ArrayList<String> yo = (ArrayList<String>) tempStore.child("meetings").child(listOfKeys.get(tempPosition)).child("participants").getValue();
                    yo.remove(Integer.parseInt(part.getKey()));
                    database.getReference().child("meetings").child(listOfKeys.get(tempPosition)).child("participants").setValue(yo);

                    Intent intent = new Intent(MeetingScreen.this, MainActivity.class);
                    startActivity(intent);
                }
            }
            //startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
