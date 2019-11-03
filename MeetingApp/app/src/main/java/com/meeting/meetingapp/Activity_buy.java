package com.meeting.meetingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.meeting.meetingapp.Model.Item;
import com.meeting.meetingapp.Model.ItemAdapter;

import java.util.ArrayList;

public class Activity_buy extends AppCompatActivity {

    private ListView itemListView;
    private ItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        itemListView = (ListView) findViewById(R.id.rewardItemListView);


        ArrayList<Item> itemsList = new ArrayList<>();

        itemsList.add(new Item("Banana", "A Banananaa" , 5, R.drawable.banan));
        itemsList.add(new Item("Banana", "A Banananaa" , 5, R.drawable.banan));
        itemsList.add(new Item("Banana", "A Banananaa" , 5, R.drawable.banan));
        itemsList.add(new Item("Banana", "A Banananaa" , 5, R.drawable.banan));


        itemAdapter = new ItemAdapter(this, itemsList);
        itemListView.setAdapter(itemAdapter);
    }
}