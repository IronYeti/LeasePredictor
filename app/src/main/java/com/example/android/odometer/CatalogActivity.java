/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.odometer;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.odometer.database.OdometerContract;
import com.example.android.odometer.database.OdometerContract.OdometerEntry;
import com.example.android.odometer.database.OdometerDbHelper;

//import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
//import com.github.mikephil.charting.charts.Chart;
//import com.example.android.odometer.SampleData;
//import com.example.android.odometer.Graph;

//import com.jjoe64.graphview.GraphView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Displays list of mileage entries that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity implements OnClickListener {

    /** Database helper that will provide us access to the database */
    private OdometerDbHelper mDbHelper;

    private NumberPad numberpad;

//    private Graph graph;

//    private Chart chart;
//    private LineChartObj lineChart;

    private LeaseActivity leaseActivity;

    private Data data;

    private SampleData sampleData;

    private TextView display;

    private LineChart mChart;
//    protected Typeface mTfRegular;
//    protected Typeface mTfLight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        mDbHelper = new OdometerDbHelper(this);

        numberpad = new NumberPad();

        data = new Data();
        data.setDBConnection(mDbHelper);

//        graph = new Graph();
//        GraphView graphObj = (GraphView) findViewById(R.id.graphDisplay);
//        graph.setGraphObj(graphObj);
//        graph.setDBConnection(mDbHelper);
//        graph.setDataObj(data);

//        chart = new Chart();
//        BarChart chartObj = (BarChart) findViewById(R.id.barchart);
//        chart.setChartObj(chartObj);
//        chart.setDBConnection(mDbHelper);
//        chart.setDataObj(data);

//        lineChart = new LineChartObj();
////        LineChart lineChartObj = (LineChart) findViewById(R.id.linechart);
////        lineChart.setChartObj(lineChartObj);
//        lineChart.setDBConnection(mDbHelper);
//        lineChart.setDataObj(data);

//        leaseActivity = new LeaseActivity();
//        leaseActivity.setDBConnection(mDbHelper);
//        leaseActivity.setDataObj(data);

//        mTfRegular = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
//        mTfLight = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");

        initChart();

        final ViewGroup standard = (ViewGroup) findViewById(R.id.numberpad);
        final Queue<ViewGroup> views = new LinkedList<ViewGroup>();
        views.add(standard);

        while(!views.isEmpty()) {
            final ViewGroup g = views.poll();
            final int children = g.getChildCount();

            for(int i = 0; i < children; i++) {
                final View v = g.getChildAt(i);

                if(v instanceof ViewGroup) {
                    views.add((ViewGroup)v);
                } else if(v instanceof Button) {
                    v.setOnClickListener(this);
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
//        refreshScreen();
    }

    private void initChart() {
//        final Typeface mTfRegular = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
//        final Typeface mTfLight = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");

        mChart = (LineChart) findViewById(R.id.linechart);

        // no description text
        mChart.getDescription().setEnabled(false);

        // enable touch gestures
        mChart.setTouchEnabled(true);

        mChart.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);
        mChart.setHighlightPerDragEnabled(true);

        // set an alternative background color
        mChart.setBackgroundColor(Color.WHITE);
        mChart.setViewPortOffsets(0f, 0f, 0f, 0f);

        // Data
        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);

        // Add empty data
        mChart.setData(data);

//        // add data
////        setData(100, 30);
//        updateGraph();
////        mChart.invalidate();

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();
        l.setEnabled(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.TOP_INSIDE);
//        xAxis.setTypeface(mTfLight);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(true);
        xAxis.setTextColor(Color.rgb(255, 192, 56));
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularity(1f); // one hour
        xAxis.setValueFormatter(new IAxisValueFormatter() {

            //            private SimpleDateFormat mFormat = new SimpleDateFormat("dd MMM HH:mm");
            private SimpleDateFormat mFormat = new SimpleDateFormat("dd MMM");

            @Override
            public String getFormattedValue(float value, AxisBase axis) {

                long millis = TimeUnit.HOURS.toMillis((long) value);
                return mFormat.format(new Date(millis));
            }
        });

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
//        leftAxis.setTypeface(mTfLight);
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(170f);
        leftAxis.setYOffset(-9f);
        leftAxis.setTextColor(Color.rgb(255, 192, 56));

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);

    }
    private void updateNewReading() {
        System.out.println("--------- Main.updateNewReading");
        TextView displayView = (TextView) findViewById(R.id.displayNewEntry);
        displayView.setText(numberpad.getCurrentDisplay());
//        updateGraph();
    }


    public void updateGraph() {
        System.out.println("--------  LineChartData.updateGraph()");

//        List<Entry> valsComp1 = new ArrayList<Entry>();
//        List<Entry> valsComp2 = new ArrayList<Entry>();
//        Entry c1e1 = new Entry(0f, 100000f); // 0 == quarter 1
//        valsComp1.add(c1e1);
//        Entry c1e2 = new Entry(1f, 140000f); // 1 == quarter 2 ...
//        valsComp1.add(c1e2);
//        // and so on ...
//
//        Entry c2e1 = new Entry(0f, 130000f); // 0 == quarter 1
//        valsComp2.add(c2e1);
//        Entry c2e2 = new Entry(1f, 115000f); // 1 == quarter 2 ...
//        valsComp2.add(c2e2);
//
//        LineDataSet setComp1 = new LineDataSet(valsComp1, "Company 1");
//        setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);
//        LineDataSet setComp2 = new LineDataSet(valsComp2, "Company 2");
//        setComp2.setAxisDependency(YAxis.AxisDependency.LEFT);
//        // use the interface ILineDataSet
//        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
//        dataSets.add(setComp1);
//        dataSets.add(setComp2);

//        List<Entry> valsActual = new ArrayList<Entry>();
//        List<Entry> valsIdeal  = new ArrayList<Entry>();
//        Entry c1e1 = new Entry(0f, 100000f); // 0 == quarter 1
//        valsActual.add(c1e1);
//        Entry c1e2 = new Entry(1f, 140000f); // 1 == quarter 2 ...
//        valsActual.add(c1e2);
        // and so on ...

//        Entry c2e1 = new Entry(0f, 130000f); // 0 == quarter 1
//        valsIdeal.add(c2e1);
//        Entry c2e2 = new Entry(1f, 115000f); // 1 == quarter 2 ...
//        valsIdeal.add(c2e2);

        LineDataSet setActual = new LineDataSet(data.lineDataEntries, "Odometer Readings");
//        LineDataSet setActual = new LineDataSet(valsActual, "Actual");
        setActual.setAxisDependency(YAxis.AxisDependency.LEFT);
//        LineDataSet setIdeal = new LineDataSet(valsIdeal, "Ideal");
//        setIdeal.setAxisDependency(YAxis.AxisDependency.LEFT);
        // use the interface ILineDataSet
        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(setActual);
//        dataSets.add(setIdeal);


        LineData data = new LineData(dataSets);
        mChart.setData(data);
        mChart.invalidate(); // refresh

        setActual.setAxisDependency(YAxis.AxisDependency.LEFT);
        setActual.setColor(ColorTemplate.getHoloBlue());
        setActual.setValueTextColor(ColorTemplate.getHoloBlue());
        setActual.setLineWidth(1.5f);
        setActual.setDrawCircles(false);
        setActual.setDrawValues(false);
        setActual.setFillAlpha(65);
        setActual.setFillColor(ColorTemplate.getHoloBlue());
        setActual.setHighLightColor(Color.rgb(244, 117, 117));
        setActual.setDrawCircleHole(false);


//        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
////        dataSets.add(setActual);
////        dataSets.add(setIdeal);
//        System.out.println("          dataSets = " + dataSets);
//
//        // create a data object with the datasets
//        LineData dataObj = new LineData(dataSets);
//        System.out.println("          dataObj = " + dataObj);
////        dataObj.setValueTextColor(Color.BLUE);
////        dataObj.setValueTextSize(9f);
//
//        // set data
//        System.out.println("          trying mChart.setData()");
//        mChart.setData(dataObj);
//        System.out.println("          trying mChart.invalidate()");
//        mChart.invalidate();

//        ArrayList<Entry> values = new ArrayList<Entry>();

//        float from = now;
//
//        // count = hours
//        float to = now + count;
//
//        // increment by 1 hour
//        for (float x = from; x < to; x++) {
//
//            float y = (float) Math.random() * range;
//            values.add(new Entry(x, y)); // add one entry per hour
//        }

        // create a dataset and give it a type
//        LineDataSet set1 = new LineDataSet(values, "DataSet 1");
//        set1.setAxisDependency(AxisDependency.LEFT);
//        set1.setColor(ColorTemplate.getHoloBlue());
//        set1.setValueTextColor(ColorTemplate.getHoloBlue());
//        set1.setLineWidth(1.5f);
//        set1.setDrawCircles(false);
//        set1.setDrawValues(false);
//        set1.setFillAlpha(65);
//        set1.setFillColor(ColorTemplate.getHoloBlue());
//        set1.setHighLightColor(Color.rgb(244, 117, 117));
//        set1.setDrawCircleHole(false);
//
//        // create a data object with the datasets
//        LineData data = new LineData(set1);
//        data.setValueTextColor(Color.WHITE);
//        data.setValueTextSize(9f);
//
//        // set data
//        mChart.setData(data);
//        mChart.invalidate();
        System.out.println("--------  LineChartData.updateGraph() Done");

    }


    public void deleteGraphData() {
        System.out.println("--------  LineChartData.deleteGraphData()");
        mChart.clear();
    }


//    private void updateLastReading() {
//        Integer odometer = data.lastOdometerValue;
//        String date = data.lastOdometerDate;
//
//        TextView displayView = (TextView) findViewById(R.id.displayLastEntryValue);
//        displayView.setText(String.valueOf(odometer));
//        displayView = (TextView) findViewById(R.id.displayLastEntryDate);
//        displayView.setText(String.valueOf(date));
//    }

    private void refreshScreen() {
        System.out.println("--------- Main.refreshScreen");

        for (int i = 0; i < 1000; i++) {
            // delay
        }
        numberpad.clearDisplay();
        data.refresh();

        TextView displayView;
        updateNewReading();
//        // Update the New Reading
//        displayView = (TextView) findViewById(R.id.displayNewEntry);
//        displayView.setText(numberpad.getCurrentDisplay());

//        updateLastReading();
        // Update the Last Reading
        displayView = (TextView) findViewById(R.id.displayLastEntryValue);
        displayView.setText(String.valueOf(data.lastOdometerValue));
        displayView = (TextView) findViewById(R.id.displayLastEntryDate);
        displayView.setText(String.valueOf(data.lastOdometerDate));

//        updateGraph();
//        graph.updateGraph();

//        chart.updateGraph();
//        lineChart.updateGraph();
        updateGraph();
    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the database.
     */
    private void updateDatabaseInfo() {
        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                OdometerContract.OdometerEntry._ID,
                OdometerEntry.COLUMN_VEHICLE_ID,
                OdometerEntry.COLUMN_DATETIME,
                OdometerEntry.COLUMN_ODOMETER};

        // Perform a query on the mileage table
        Cursor cursor = db.query(
                OdometerEntry.TABLE_NAME,   // The table to query
                projection,            // The columns to return
                null,                  // The columns for the WHERE clause
                null,                  // The values for the WHERE clause
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                OdometerEntry._ID + " DESC");                   // The sort order

        TextView displayView = (TextView) findViewById(R.id.text_summary);



        try {
            // Create a header in the Text View that looks like this:
            //
            // The pets table contains <number of rows in Cursor> pets.
            // _id - name - breed - gender - weight
            //
            // In the while loop below, iterate through the rows of the cursor and display
            // the information from each column in this order.
            displayView.setText("The mileage table contains " + cursor.getCount() + " entries.\n\n");
            displayView.append(OdometerEntry._ID + " - " +
                    OdometerContract.OdometerEntry.COLUMN_VEHICLE_ID + " - " +
                    OdometerEntry.COLUMN_DATETIME + " - " +
                    OdometerEntry.COLUMN_ODOMETER + "\n");

            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(OdometerContract.OdometerEntry._ID);
            int vehicleIdColumnIndex = cursor.getColumnIndex(OdometerEntry.COLUMN_VEHICLE_ID);
            int dateColumnIndex = cursor.getColumnIndex(OdometerEntry.COLUMN_DATETIME);
            int mileageColumnIndex = cursor.getColumnIndex(OdometerEntry.COLUMN_ODOMETER);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                int currentVehicleId = cursor.getInt(vehicleIdColumnIndex);
                String currentDate = cursor.getString(dateColumnIndex);
                int currentMileage = cursor.getInt(mileageColumnIndex);
                // Display the values from each column of the current row in the cursor in the TextView
                displayView.append(("\n" + currentID + " - " +
                        currentVehicleId + " - " +
                        currentDate + " - " +
                        currentMileage));
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }

//    private void updateGraph() {
//        graph.updateGraph();
//
////        System.out.println("--------  Starting Graph update");
////
////        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {});
////
////        graph.getViewport().setScrollable(true); // enables horizontal scrolling
////        graph.addSeries(new LineGraphSeries(dbData()));
////
////        System.out.println("--------  Graph Updated");
//    }

//    private DataPoint[] dbData() {
//        System.out.println("--------  Getting dbData");
//
//        ArrayList<Integer> readings = getAllOdometerReadings(1);
//        int count = readings.size();
//
//        DataPoint[] values = new DataPoint[readings.size()];
//        for (int i=0; i < count; i++) {
//            DataPoint v = new DataPoint(i, readings.get(i));
//            values[i] = v;
//        }
//        System.out.println("--------  Done w/ dbData");
//        return values;
//    }

    /**
     * Helper method to insert random data into the database. For debugging purposes only.
     */
    private void insertDummyEntry() {
        System.out.println("--------- Main.insertDummyEntry");
//        Integer odometer;
        Integer vehicleID = 1;

//        odometer = getLastOdometerReading(new String[]{String.valueOf(vehicleID)});
        Integer odometer = data.lastOdometerValue;
        Random ran = new Random();
        odometer = odometer + ran.nextInt(50);

        saveReading(vehicleID, odometer);
    }

    private void saveReading(Integer vehicleID, Integer odometer) {
        System.out.println("--------- Main.saveReading (no datestamp");
//        Long millis = System.currentTimeMillis();

        String now = new SimpleDateFormat("MM/dd/yyyy").format(Calendar.getInstance().getTime());;
        saveReading(vehicleID, odometer, now);
    }

    private void saveReading(Integer vehicleID, Integer odometer, String datestamp){
        System.out.println("--------- Main.saveReading (w/ datestamp");
        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        // Create a ContentValues object where column names are the keys,
        // and attributes are the values.
        ContentValues values = new ContentValues();

        values.put(OdometerEntry.COLUMN_VEHICLE_ID, vehicleID);
//        values.put(OdometerEntry.COLUMN_DATETIME, "1/1/2017");
        values.put(OdometerEntry.COLUMN_ODOMETER, odometer);
        values.put(OdometerEntry.COLUMN_DATETIME, datestamp);

        // Insert a new row for Jan 1, 2017 in the database, returning the ID of that new row.
        // The first argument for db.insert() is the table name.
        // The second argument provides the name of a column in which the framework
        // can insert NULL in the event that the ContentValues is empty (if
        // this is set to "null", then the framework will not insert a row when
        // there are no values).
        // The third argument is the ContentValues object containing the info for this entry.
        long newRowId = db.insert(OdometerEntry.TABLE_NAME, null, values);

//        updateDatabaseInfo();
        refreshScreen();
        // TODO: Add toast msg
//        updateLastReading();
    }

//    private Integer getLastOdometerReading(String[] vehicle_ID) {
//        // Create and/or open a database to read from it
//        SQLiteDatabase db = mDbHelper.getReadableDatabase();
//        Integer odometer = 0;
//
//        // Define a projection that specifies which columns from the database
//        // you will actually use after this query.
//        String[] projection = {OdometerEntry._ID, OdometerEntry.COLUMN_ODOMETER};
//
//        // Perform a query on the mileage table
//        Cursor cursor = db.query(
//                OdometerEntry.TABLE_NAME,   // The table to query
//                projection,            // The columns to return
//                OdometerEntry.COLUMN_VEHICLE_ID + "=" + vehicle_ID[0],                  // The WHERE clause
//                null,
//                null,                  // Don't group the rows
//                null,                  // Don't filter by row groups
////                OdometerEntry.COLUMN_DATETIME + " DESC");                   // The sort order
//                OdometerEntry._ID + " DESC");                   // The sort order
//
//        try {
//            if(cursor.getCount() > 0) {
//                cursor.moveToFirst();
//                odometer = cursor.getInt(cursor.getColumnIndex(OdometerEntry.COLUMN_ODOMETER));
//            } else{
//                odometer = 0;
//            }
//
//        } finally {
//            // Always close the cursor when you're done reading from it. This releases all its
//            // resources and makes it invalid.
//            cursor.close();
//        }
//        return odometer;
//    }

//    private String getLastDateReading(String[] vehicle_ID) {
//        // Create and/or open a database to read from it
//        SQLiteDatabase db = mDbHelper.getReadableDatabase();
//        String date;
//
//        // Define a projection that specifies which columns from the database
//        // you will actually use after this query.
//        String[] projection = {OdometerEntry.COLUMN_DATETIME};
//
//        // Perform a query on the mileage table
//        Cursor cursor = db.query(
//                OdometerEntry.TABLE_NAME,   // The table to query
//                projection,            // The columns to return
//                OdometerEntry.COLUMN_VEHICLE_ID + "=" + vehicle_ID[0],                  // The WHERE clause
//                null,
//                null,                  // Don't group the rows
//                null,                  // Don't filter by row groups
//                OdometerEntry._ID + " DESC");                   // The sort order
//
//        try {
//            if(cursor.getCount() > 0) {
//                cursor.moveToFirst();
//                date = cursor.getString(cursor.getColumnIndex(OdometerEntry.COLUMN_DATETIME));
//            } else{
//                date = "";
//            }
//
//        } finally {
//            // Always close the cursor when you're done reading from it. This releases all its
//            // resources and makes it invalid.
//            cursor.close();
//        }
//        return date;
//    }

//    private Integer getLastID(String[] vehicle_ID) {
//        // Create and/or open a database to read from it
//        SQLiteDatabase db = mDbHelper.getReadableDatabase();
//        Integer id = 0;
//
//        // Define a projection that specifies which columns from the database
//        // you will actually use after this query.
//        String[] projection = {OdometerEntry._ID};
//
//        // Perform a query on the mileage table
//        Cursor cursor = db.query(
//                OdometerEntry.TABLE_NAME,   // The table to query
//                projection,            // The columns to return
//                OdometerEntry.COLUMN_VEHICLE_ID + "=" + vehicle_ID[0],                  // The WHERE clause
//                null,
//                null,                  // Don't group the rows
//                null,                  // Don't filter by row groups
//                OdometerEntry._ID + " DESC");                   // The sort order
//
//        try {
//            if(cursor.getCount() > 0) {
//                cursor.moveToFirst();
//                id = cursor.getInt(cursor.getColumnIndex(OdometerEntry._ID));
//            } else{
//                id = 0;
//            }
//
//        } finally {
//            // Always close the cursor when you're done reading from it. This releases all its
//            // resources and makes it invalid.
//            cursor.close();
//        }
//        return id;
//    }

    private ArrayList getAllOdometerReadings(int vehicle_ID) {
        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        ArrayList <Integer> readings = new ArrayList<>();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {OdometerEntry.COLUMN_ODOMETER};

        // Perform a query on the mileage table
        Cursor cursor = db.query(
                OdometerEntry.TABLE_NAME,   // The table to query
                projection,            // The columns to return
                OdometerEntry.COLUMN_VEHICLE_ID + "=" + vehicle_ID,                  // The WHERE clause
                null,
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                OdometerEntry._ID + " ASC");                   // The sort order

        try {
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount(); i++) {
                    readings.add(cursor.getInt(cursor.getColumnIndex(OdometerEntry.COLUMN_ODOMETER)));
                    cursor.moveToNext();
                }
            } else{
                readings.add(0);
            }

        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }

        return readings;
    }

    private Boolean deleteLastEntry() {
        System.out.println("--------- Main.deleteLastEntry");
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

//        Integer id = getLastID(new String[]{String.valueOf(1)});
        Integer id = data.lastRecordID;

        if (id > 0) {
            String whereClause = OdometerEntry._ID + "=?";
            String[] whereArgs = new String[] { String.valueOf(id) };

            db.delete(OdometerEntry.TABLE_NAME, whereClause, whereArgs);
            return true;
        } else {
            return false;
        }
    }

    private void deleteAllEntries() {
        System.out.println("--------- Main.deleteAllEntries");
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String whereClause = "1=1";
        String[] whereArgs = new String[] { };

        db.delete(OdometerEntry.TABLE_NAME, whereClause, whereArgs);
//        graph.deleteGraphData();
//        chart.deleteGraphData();
//        lineChart.deleteGraphData();
        mChart.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        System.out.println("--------- Main.onOptionsItemSelected");
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            case R.id.action_insert_sample_data:
                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                SampleData call = new SampleData();
                call.saveSampleReadings(db, 1);
                refreshScreen();
                System.out.println(">>>>>>>>  Inserted Sample Data");
                return true;
            case R.id.action_insert_dummy_data:
                insertDummyEntry();
                refreshScreen();
                System.out.println(">>>>>>>>  Inserted Dummy Entry");
                return true;
            case R.id.action_delete_last_entry:
                if (deleteLastEntry()) {
                    Toast.makeText(this, getString(R.string.toast_delete_last_entry), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, getString(R.string.toast_delete_last_entry_cant), Toast.LENGTH_SHORT).show();
                }
                refreshScreen();
                System.out.println(">>>>>>>>  Deleted Last Entry");
                return true;
            case R.id.action_viewedit_data:
                // Do nothing for now
                // TODO: Either implement this or remove from menu
                System.out.println("--------  Selected View/Edit Data (but it's not developed yet)");

                return true;
            case R.id.action_delete_all_entries:
                // TODO:  Add confirmation dialog
                deleteAllEntries();
//                graph.removeAllSeries();
                refreshScreen();
                System.out.println(">>>>>>>>  Deleted All Entries!!");
                return true;
            case R.id.action_edit_lease_data:
                // TODO: call the LeaseActivity and pass Intent (is there anything to pass?)

                Intent editLeaseDataIntent = new Intent(this, LeaseActivity.class);
                startActivity(editLeaseDataIntent);
                return true;
        }
//        refreshScreen();
        System.out.println("????????  Nothing selected me thinks");
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(
            final int keyCode,
            final KeyEvent event) {

        super.onKeyDown(keyCode, event);

        boolean handled = false;

        // TODO:  Do i need these?  EIther fill out remaining values or remove this.
        if((event.getFlags() & KeyEvent.FLAG_SOFT_KEYBOARD) == 0) {
            switch(keyCode) {
                case KeyEvent.KEYCODE_0:
                    numberpad.zero();
                    handled = true;
                    break;
                case KeyEvent.KEYCODE_1:
                    numberpad.one();
                    handled = true;
                    break;
            }

            display.setText(numberpad.getCurrentDisplay());
        }

        return handled;
    }

    public void onClick(final View clicked) {
        boolean operation = false;

        switch(clicked.getId()) {
            case R.id.zero:
                numberpad.zero();
                break;
            case R.id.one:
                numberpad.one();
                break;
            case R.id.two:
                numberpad.two();
                break;
            case R.id.three:
                numberpad.three();
                break;
            case R.id.four:
                numberpad.four();
                break;
            case R.id.five:
                numberpad.five();
                break;
            case R.id.six:
                numberpad.six();
                break;
            case R.id.seven:
                numberpad.seven();
                break;
            case R.id.eight:
                numberpad.eight();
                break;
            case R.id.nine:
                numberpad.nine();
                break;
            case R.id.delete:
                numberpad.delete();
                break;
            case R.id.save:
                // TODO: Only save if new odometer is changed, or if previous date is today -1
                Integer newValue = Integer.valueOf(String.valueOf(numberpad.getCurrentDisplay()));
                if (newValue > 0 && newValue <= data.lastOdometerValue) {
                    Toast.makeText(this, getString(R.string.toast_bad_value), Toast.LENGTH_SHORT).show();

                } else if (newValue == 0) {
                    break;  // do nothing
                } else {
                    System.out.println("--------  Main.onClick - Saving New Entry");
                    saveReading(1, Integer.valueOf(String.valueOf(numberpad.getCurrentDisplay())));
//                    numberpad.clearDisplay();
                    System.out.println("--------  Main.onClick - Updating graph");
                    updateGraph();
                    break;
                }
        }

        updateNewReading();
    }

}
