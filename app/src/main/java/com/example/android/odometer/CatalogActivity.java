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

import com.github.mikephil.charting.charts.BarChart;
//import com.github.mikephil.charting.charts.Chart;
//import com.example.android.odometer.SampleData;
//import com.example.android.odometer.Graph;

//import com.jjoe64.graphview.GraphView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 * Displays list of mileage entries that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity implements OnClickListener {

    /** Database helper that will provide us access to the database */
    private OdometerDbHelper mDbHelper;

    private NumberPad numberpad;

//    private Graph graph;

    private Chart chart;

    private LeaseActivity leaseActivity;

    private Data data;

    private SampleData sampleData;

    private TextView display;

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

        chart = new Chart();
        BarChart chartObj = (BarChart) findViewById(R.id.chart);
        chart.setChartObj(chartObj);
        chart.setDBConnection(mDbHelper);
        chart.setDataObj(data);

//        leaseActivity = new LeaseActivity();
//        leaseActivity.setDBConnection(mDbHelper);
//        leaseActivity.setDataObj(data);

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
        refreshScreen();
    }

    private void updateNewReading() {
        System.out.println("--------- Main.updateNewReading");
        TextView displayView = (TextView) findViewById(R.id.displayNewEntry);
        displayView.setText(numberpad.getCurrentDisplay());
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
        chart.updateGraph();
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
        chart.deleteGraphData();
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
                    saveReading(1, Integer.valueOf(String.valueOf(numberpad.getCurrentDisplay())));
//                    numberpad.clearDisplay();
                    System.out.println("--------  Saved New Entry");
                    break;
                }
        }

        updateNewReading();
    }

}
