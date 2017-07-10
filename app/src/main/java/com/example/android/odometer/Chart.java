package com.example.android.odometer;

/**
 * Created by ACornelius on 6/2/2017.
 */

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android.odometer.database.OdometerDbHelper;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
//import com.jjoe64.graphview.GraphView;
//import com.jjoe64.graphview.series.BarGraphSeries;
//import com.jjoe64.graphview.series.DataPoint;
//import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

public class Chart extends AppCompatActivity {

//    public GraphView graph;

    BarChart mChart;

    /** Database helper that will provide us access to the database */
    private OdometerDbHelper mDbHelper;

    private Data data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void setDBConnection(OdometerDbHelper mDbHelper) {
        this.mDbHelper = mDbHelper;
    }

    public void setChartObj(BarChart chart) {
        this.mChart = chart;
    }

    public void setDataObj(Data data) {
        this.data = data;
    }

//    public void updateGraph_OLD() {
//        System.out.println("--------  Chart.updateGraph()");
//        deleteGraphData();
//        data.refresh();
//
////        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {});
////        series = new LineGraphSeries(actualData());
//        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {});
//        series = new BarGraphSeries<>(actualData());
////        series.setThickness(8);
//        series.setDataWidth(1d);
//        series.setSpacing(20);
//        graph.addSeries(series);
//
//        LineGraphSeries<DataPoint> series2 ;//= new LineGraphSeries<DataPoint>(new DataPoint[] {});
//        series2 = new LineGraphSeries(idealData());
//        series2.setColor(Color.argb(255, 60, 200, 128));  // this is green
//        series2.setThickness(10);
//        graph.addSeries(series2);
//
//        if (data.numberOfReadings > 20) {
//            graph.getViewport().setXAxisBoundsManual(true);
//            graph.getViewport().setMinX(0);
//            graph.getViewport().setMaxX(20);
//
//        graph.getViewport().setScrollable(true); // enables horizontal scrolling
//        graph.getViewport().scrollToEnd();
//        graph.getViewport().computeScroll();
////        graph.getLegendRenderer().setVisible(true);
////        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
//        }
//    }

    public void updateGraph() {
        System.out.println("--------  Chart.updateGraph()");
        deleteGraphData();
        data.refresh();

        ArrayList<BarEntry> entries = new ArrayList<>();
        BarDataSet dataSet = new BarDataSet(data.entries, "Odometer Readings");
        BarData data = new BarData(dataSet);

        // set the bar dataset attributes
        dataSet.setColors(Color.RED);
//        dataSet.setBarBorderWidth(.5f);
        data.setBarWidth(30f);

        // don't show the values on top of the bars
        dataSet.setDrawValues(false);

        // set max # of bars to show      // DOESN'T SEEM TO BE WORKING
        mChart.setVisibleXRangeMaximum(15);

//        // if more than 60 entries are displayed in the chart, no values will be drawn
//        mChart.setMaxVisibleValueCount(10);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);
        mChart.setScaleYEnabled(false);
        mChart.setDoubleTapToZoomEnabled(true);
        

        mChart.setAutoScaleMinMaxEnabled(true);

        // specify the width each bar should have
//        float barWidth = 0.03f;
//        mChart.getBarData().setBarWidth(barWidth);

        mChart.setData(data);


        IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(mChart);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setTypeface(mTfLight);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);


//        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {});
//        series = new BarGraphSeries<>(actualData());
//        series.setThickness(8);
//        series.setDataWidth(1d);
//        series.setSpacing(20);
//        graph.addSeries(series);

//        LineGraphSeries<DataPoint> series2 ;//= new LineGraphSeries<DataPoint>(new DataPoint[] {});
//        series2 = new LineGraphSeries(idealData());
//        series2.setColor(Color.argb(255, 60, 200, 128));  // this is green
//        series2.setThickness(10);
//        graph.addSeries(series2);

//        if (data.numberOfReadings > 20) {
//            graph.getViewport().setXAxisBoundsManual(true);
//            graph.getViewport().setMinX(0);
//            graph.getViewport().setMaxX(20);
//
//            graph.getViewport().setScrollable(true); // enables horizontal scrolling
//            graph.getViewport().scrollToEnd();
//            graph.getViewport().computeScroll();
//        }
    }

    public void deleteGraphData() {
        System.out.println("--------  Chart.deleteGraphData()");
//        graph.removeAllSeries();
    }

//    private Entry actualData() {
//        System.out.println("--------  Chart.actualData()");
//
//        ArrayList<Entry> entries = new ArrayList<>();
//        BarDataSet dataSet = new BarDataSet(data.entries, "Odometer Readings");
//    }

//    private DataPoint[] actualData_OLD() {
////        System.out.println("--------  Graph.actualData()");
//
//        ArrayList<Integer> readings = data.readings;
//        int count = readings.size();
//
//        DataPoint[] values = new DataPoint[count];
//        for (int i=0; i < count; i++) {
//            DataPoint v = new DataPoint(i, readings.get(i));
//            values[i] = v;
//        }
//        return values;
//    }

//    private DataPoint[] idealData_OLD() {
////        System.out.println("--------  Graph.idealData()");
//
//        int count = data.numberOfReadings;
//        double idealOdometer = 0;
//
//        DataPoint[] values = new DataPoint[count];
//        for (int i=0; i < count; i++) {
//            idealOdometer += data.dailyDistance;
//            DataPoint v = new DataPoint(i, idealOdometer);
//            values[i] = v;
//        }
//        System.out.println("......... Ideal Odometer = " + idealOdometer + "   @ " + data.dailyDistance + " per day");
//        return values;
//    }


}
