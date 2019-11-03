package com.meeting.meetingapp;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class CardActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mUserRefDatabase;
    private TextView username;
    ViewPager viewPager;
    Adapter adapter;
    List<ModelClass> models;
    Integer[] colors = null;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        models = new ArrayList<>();
        username = (TextView) findViewById(R.id.card_displayname);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    username.setText(user.getDisplayName().toUpperCase());
                } else {
                    // User is signed out
                    Intent intent = new Intent(CardActivity.this, Login.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // LoginActivity is a New Task
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); // The old task when coming back to this activity should be cleared so we cannot come back to it.
                    startActivity(intent);
                }
            }
        };

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
                getResources().getColor(R.color.color1),
                getResources().getColor(R.color.color1),
                getResources().getColor(R.color.color1),
                getResources().getColor(R.color.color1)
        };

        colors = colors_temp;

    }
}
