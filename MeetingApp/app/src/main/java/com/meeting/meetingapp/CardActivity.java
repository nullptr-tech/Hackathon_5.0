package com.meeting.meetingapp;

import android.animation.ArgbEvaluator;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class CardActivity extends AppCompatActivity {


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
