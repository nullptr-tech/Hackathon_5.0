package com.meeting.meetingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;

import java.sql.Struct;

public class GraphTest extends AppCompatActivity {

    ImageView pig1, pig2, pig3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_test);


        pig1 = (ImageView) findViewById(R.id.piggie1);
        pig2 = (ImageView) findViewById(R.id.piggie2);
        pig3 = (ImageView) findViewById(R.id.piggie3);

        pig1.setVisibility(View.INVISIBLE);
        pig2.setVisibility(View.INVISIBLE);
        pig3.setVisibility(View.INVISIBLE);

        ValueLineChart mCubicValueLineChart = (ValueLineChart) findViewById(R.id.cubiclinechart);
        ValueLineSeries series = new ValueLineSeries();
        series.setColor(0xFF56B7F1);

        double moneyIn = 10.2 + 3.0+ 12.54 + 34.2;
        double moneyOut = 31.2 + 23 + 12.54 + 10.2;
        if ( moneyIn < moneyOut){
            pig1.setVisibility(View.VISIBLE);
        }else if(moneyIn > moneyOut){
            pig1.setVisibility(View.VISIBLE);
            pig2.setVisibility(View.VISIBLE);
            pig3.setVisibility(View.VISIBLE);
        }else if(moneyIn == moneyOut){
            pig1.setVisibility(View.VISIBLE);
            pig2.setVisibility(View.VISIBLE);
        }

        series.addPoint(new ValueLinePoint("Mon", 0.0f));
        series.addPoint(new ValueLinePoint("Tue", 3.4f));
        series.addPoint(new ValueLinePoint("Wed", .4f));
        series.addPoint(new ValueLinePoint("Thur", 1.2f));
        series.addPoint(new ValueLinePoint("Fri", 2.6f));
        series.addPoint(new ValueLinePoint("Sat", 1.0f));
        series.addPoint(new ValueLinePoint("Sun", 3.5f));



        mCubicValueLineChart.addSeries(series);
        mCubicValueLineChart.startAnimation();
    }
}
