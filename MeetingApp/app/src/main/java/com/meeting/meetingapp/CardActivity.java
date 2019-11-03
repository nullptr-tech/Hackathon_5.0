package com.meeting.meetingapp;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CardActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mUserRefDatabase;
    private FirebaseDatabase database;
    private DatabaseReference ref;


    private TextView username,bb;
    ViewPager viewPager;
    Adapter adapter;
    List<ModelClass> models;
    Integer[] colors = null;
    Button CreateBtn;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        models = new ArrayList<>();
        username = (TextView) findViewById(R.id.card_displayname);
<<<<<<< Updated upstream
        CreateBtn = (Button) findViewById(R.id.createBtn);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
=======
        bb = (TextView) findViewById(R.id.card_balance);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        ref = database.getReference();
        ref.child("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
>>>>>>> Stashed changes
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println("yousd abjkasd pasdfjnasd o");
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    System.out.println("aaaaaaaaaaaaaaappppppppppppp " + postSnapshot);
                    if(postSnapshot.getKey().equals("displayName")){
                        username.setText(postSnapshot.getValue().toString());
                    }
                    if(postSnapshot.getKey().equals("startingBalance")){
                        bb.setText(postSnapshot.getValue().toString());
                    }
                }
                // System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaa ppppp : " +snapshot.getValue() + "\n");  //prints "Do you have data? You'll love Firebase."
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        models.add(new ModelClass(R.drawable.city, "Chore 1", "Brochure is an informative paper document (often also used for advertising) that can be folded into a template"));
        models.add(new ModelClass(R.drawable.city, "Chore 2", "Sticker is a type of label: a piece of printed paper, plastic, vinyl, or other material with pressure sensitive adhesive on one side"));
        models.add(new ModelClass(R.drawable.city, "Chore 3", "Poster is any piece of printed paper designed to be attached to a wall or vertical surface."));
        models.add(new ModelClass(R.drawable.city, "Chore 4", "Business cards are cards bearing business information about a company or individual."));
        models.add(new ModelClass(R.drawable.city, "Chore 5", "Brochure is an informative paper document (often also used for advertising) that can be folded into a template"));
        models.add(new ModelClass(R.drawable.city, "Chore 6", "Brochure is an informative paper document (often also used for advertising) that can be folded into a template"));
        models.add(new ModelClass(R.drawable.city, "Chore 7", "Brochure is an informative paper document (often also used for advertising) that can be folded into a template"));
        models.add(new ModelClass(R.drawable.city, "Chore 8", "Brochure is an informative paper document (often also used for advertising) that can be folded into a template"));

        adapter = new Adapter(models, this);

        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(130, 0, 130, 0);

        Integer[] colors_temp = {
              //  getResources().getColor(R.color.color1),
              //  getResources().getColor(R.color.color1),
              //  getResources().getColor(R.color.color1),
               // getResources().getColor(R.color.color1)
        };

        colors = colors_temp;

        CreateBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (v==CreateBtn){
                    Intent nextBut = new Intent(CardActivity.this, SelectParticipants.class);
                    startActivity(nextBut);
                }
            }
        });
    }
}
