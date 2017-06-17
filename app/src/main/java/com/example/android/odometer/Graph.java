package com.example.android.odometer;

/**
 * Created by ACornelius on 6/2/2017.
 */

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android.odometer.database.OdometerDbHelper;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

public class Graph extends AppCompatActivity {

    public GraphView graph;

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

    public void setGraphObj(GraphView graph) {
        this.graph = graph;
    }

    public void setDataObj(Data data) {
        this.data = data;
    }

    public void updateGraph() {
        System.out.println("--------  Updating Graph");
        deleteGraphData();
        data.refresh();

//        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {});
//        series = new LineGraphSeries(actualData());
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {});
        series = new BarGraphSeries<>(actualData());
//        series.setThickness(8);
        series.setDataWidth(1d);
        series.setSpacing(20);
        graph.addSeries(series);

        LineGraphSeries<DataPoint> series2 ;//= new LineGraphSeries<DataPoint>(new DataPoint[] {});
        series2 = new LineGraphSeries(idealData());
        series2.setColor(Color.argb(255, 60, 200, 128));  // this is green
        series2.setThickness(10);
        graph.addSeries(series2);

        if (data.numberOfReadings > 20) {
            graph.getViewport().setXAxisBoundsManual(true);
            graph.getViewport().setMinX(0);
            graph.getViewport().setMaxX(20);

        graph.getViewport().setScrollable(true); // enables horizontal scrolling
        graph.getViewport().scrollToEnd();
        graph.getViewport().computeScroll();
//        graph.getLegendRenderer().setVisible(true);
//        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        }
    }

    public void deleteGraphData() {
        System.out.println("--------  Deleting Graph Data");
        graph.removeAllSeries();
    }

    private DataPoint[] actualData() {
        System.out.println("--------  Getting actualData");

        ArrayList<Integer> readings = data.readings;
        int count = readings.size();

        DataPoint[] values = new DataPoint[count];
        for (int i=0; i < count; i++) {
            DataPoint v = new DataPoint(i, readings.get(i));
            values[i] = v;
        }
        return values;
    }

    private DataPoint[] idealData() {
        System.out.println("--------  Getting idealData");

        int count = data.numberOfReadings;
        double idealOdometer = 0;

        DataPoint[] values = new DataPoint[count];
        for (int i=0; i < count; i++) {
            idealOdometer += data.dailyDistance;
            DataPoint v = new DataPoint(i, idealOdometer);
            values[i] = v;
        }
        System.out.println("......... Ideal Odometer = " + idealOdometer + "   @ " + data.dailyDistance + " per day");
        return values;
    }

//    private ArrayList getAllOdometerReadings(int vehicle_ID) {
////        // Create and/or open a database to read from it
////        SQLiteDatabase db = mDbHelper.getReadableDatabase();
////        ArrayList <Integer> readings = new ArrayList<>();
////
////        // Define a projection that specifies which columns from the database
////        // you will actually use after this query.
////        String[] projection = {OdometerEntry.COLUMN_ODOMETER};
////
////        // Perform a query on the mileage table
////        Cursor cursor = db.query(
////                OdometerEntry.TABLE_NAME,   // The table to query
////                projection,            // The columns to return
////                OdometerEntry.COLUMN_VEHICLE_ID + "=" + vehicle_ID,                  // The WHERE clause
////                null,
////                null,                  // Don't group the rows
////                null,                  // Don't filter by row groups
////                OdometerEntry._ID + " ASC");                   // The sort order
////
////        try {
////            if(cursor.getCount() > 0) {
////                cursor.moveToFirst();
////                for (int i = 0; i < cursor.getCount(); i++) {
////                    readings.add(cursor.getInt(cursor.getColumnIndex(OdometerEntry.COLUMN_ODOMETER)));
////                    cursor.moveToNext();
////                }
////            } else{
////                readings.add(0);
////            }
////
////        } finally {
////            // Always close the cursor when you're done reading from it. This releases all its
////            // resources and makes it invalid.
////            cursor.close();
////        }
////
////        return readings;
//        return data.readings;
//    }

}
