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
import android.widget.AdapterView;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckConflicts extends AppCompatActivity {
    Bundle b;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference mDatabase = database.getReference();
    DatabaseReference refTwo;
    List<String> listOfKeys;
    List<String> usersToDisplay;
    ArrayList<String> usersToDisplayKeys;
    ArrayAdapter<String> adapter;
    ListView listViewOfUsers;
    Button confirmButton;
    boolean isAStrongConflict;

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_conflicts);
                Intent iin = getIntent();
                b = iin.getExtras();

                isAStrongConflict = false;
                listOfKeys = (List<String>) b.get("listOfKeys");
                listViewOfUsers = (ListView) findViewById(R.id.listOfUsers);
                usersToDisplay = new ArrayList<>();
                usersToDisplayKeys = new ArrayList<>();
                adapter = new ArrayAdapter<String>(this, R.layout.user_info, R.id.userInfo, usersToDisplay);
                final int position = (int) b.get("oldPosition");
                final int newPosition = (int) b.get("position");

                final ArrayList<Integer> listOfTimes = (ArrayList<Integer>) b.get("listOfTimes");
                final ArrayList<Integer> listOfDays = (ArrayList<Integer>) b.get("listOfDays");
                final ArrayList<String> listOfDisplayedTimes = (ArrayList<String>) b.get("listOfTimesToDisplay");
                TextView myAwesomeTextView = (TextView)findViewById(R.id.finalDate);
                myAwesomeTextView.setText("Date: " + listOfDisplayedTimes.get(newPosition));
                database = FirebaseDatabase.getInstance();
                confirmButton = findViewById(R.id.confirmButton);
                confirmButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if(!isAStrongConflict) {
                            database.getReference().child("meetings").child(listOfKeys.get(position)).child("pending").setValue(null);
                            database.getReference().child("meetings").child(listOfKeys.get(position)).child("setDay").setValue(listOfDays.get(newPosition));
                            database.getReference().child("meetings").child(listOfKeys.get(position)).child("setTime").setValue(listOfTimes.get(newPosition));
                            Intent intent = new Intent(CheckConflicts.this, MainActivity.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(CheckConflicts.this, "Participant cannot attend, please choose another date", Toast.LENGTH_SHORT).show();
                        }
                    }

                });
                refTwo = database.getReference();
                refTwo.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<String> listOfParticipants = (List<String>) dataSnapshot.child("meetings").child(listOfKeys.get(position)).child("participants").getValue();
                        List<List<String>> listOfEachTime = (List<List<String>>) dataSnapshot.child("meetings").child(listOfKeys.get(position)).child("times").getValue();
                        int count = 0;
                        Map<String, List<List<Integer>>> userConflicts = new HashMap<String, List<List<Integer>>>();
                        for(DataSnapshot userKey : dataSnapshot.child("users").getChildren()){
                            if(!listOfParticipants.contains(userKey.getKey())){
                                continue;
                            }
                            List<List<Integer>> conflictListDays = new ArrayList<List<Integer>>();
                            List<List<String>> listOfUserTimesPref = (List<List<String>>)dataSnapshot.child("users").child(userKey.getKey()).child("prefTimes").getValue();
                            List<List<String>> listOfUserTimesExcl = (List<List<String>>)dataSnapshot.child("users").child(userKey.getKey()).child("exclTimes").getValue();
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
                                    else{
                                        conflictListTimes.add(null);
                                    }
                                }
                                conflictListDays.add(conflictListTimes);
                            }
                            userConflicts.put(userKey.getKey(), conflictListDays);
                            if(userConflicts.get(userKey.getKey()).get(listOfDays.get(newPosition)).get(listOfTimes.get(newPosition)) == 0){
                                usersToDisplay.add(userKey.child("displayName").getValue().toString() + "");
                                usersToDisplayKeys.add(userKey.getKey());
                            }
                            else if(userConflicts.get(userKey.getKey()).get(listOfDays.get(newPosition)).get(listOfTimes.get(newPosition)) == 1){
                                usersToDisplay.add(userKey.child("displayName").getValue().toString() + " - Preferred time");
                                usersToDisplayKeys.add(userKey.getKey());
                            }
                            else if(userConflicts.get(userKey.getKey()).get(listOfDays.get(newPosition)).get(listOfTimes.get(newPosition)) == 2){
                                usersToDisplay.add(userKey.child("displayName").getValue().toString() + " - Cannot attend");
                                usersToDisplayKeys.add(userKey.getKey());
                                isAStrongConflict = true;
                            }

                        }

                        listViewOfUsers.setAdapter(adapter);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("The read failed: " + databaseError.getCode());
                    }
                });
                listViewOfUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(CheckConflicts.this, UserPrefExcl.class);
                        intent.putExtra("position", position);
                        intent.putStringArrayListExtra("usersToDisplayKeys", usersToDisplayKeys);
                        startActivity(intent);
                    }
                });

            }
}

