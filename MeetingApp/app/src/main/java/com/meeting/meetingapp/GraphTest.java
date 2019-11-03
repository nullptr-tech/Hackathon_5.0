package com.meeting.meetingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;

public class GraphTest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_test);


        ValueLineChart mCubicValueLineChart = (ValueLineChart) findViewById(R.id.cubiclinechart);

        ValueLineSeries series = new ValueLineSeries();
        series.setColor(0xFF56B7F1);

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
