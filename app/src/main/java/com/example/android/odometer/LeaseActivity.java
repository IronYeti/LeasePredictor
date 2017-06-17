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
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.odometer.database.OdometerContract;
import com.example.android.odometer.database.OdometerDbHelper;

/**
 * Allows user to edit lease details for a vehicle.
 */
public class LeaseActivity extends AppCompatActivity {

    /** Declare the Lease Details edit fields */
    private Spinner mVehicleSpinner;
    private EditText mStartDateEditText;
    private EditText mStartOdometerEditText;
    private EditText mDurationEditText;
    private EditText mMileageEditText;
    private EditText mOverageEditText;

    // Create database helper
    OdometerDbHelper mDbHelper = new OdometerDbHelper(this);

    // Gets the database in write mode
    SQLiteDatabase db = mDbHelper.getWritableDatabase();

    /**
     * The possible valid values are in the OdometerContract.java file:
     * {@link OdometerContract.OdometerEntry#VEHICLE_UNKNOWN},
     * {@link OdometerContract.OdometerEntry#VEHICLE_1}, or
     * {@link OdometerContract.OdometerEntry#VEHICLE_2}.
     */
    private int mVehicle = OdometerContract.OdometerEntry.VEHICLE_1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lease);

        // Find all relevant views that we will need to read user input from
        mVehicleSpinner = (Spinner) findViewById(R.id.spinner_vehicle);
        mStartDateEditText = (EditText) findViewById(R.id.edit_start_date);
        mStartOdometerEditText = (EditText) findViewById(R.id.edit_start_odometer);
        mDurationEditText = (EditText) findViewById(R.id.edit_lease_duration);
        mMileageEditText = (EditText) findViewById(R.id.edit_lease_mileage);
        mOverageEditText = (EditText) findViewById(R.id.edit_lease_overage);

        // TODO: setup the edtiText listeners here?

        setupSpinner();
    }

    /**
     * Setup the dropdown spinner that allows the user to select the Vehicle.
     */
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_vehicle_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mVehicleSpinner.setAdapter(spinnerAdapter);

        // Set the integer mSelected to the constant values
        mVehicleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.vehicle_1))) {
                        mVehicle = OdometerContract.OdometerEntry.VEHICLE_1;
                    } else if (selection.equals(getString(R.string.vehicle_2))) {
                        mVehicle = OdometerContract.OdometerEntry.VEHICLE_2;
                    } else {
                        mVehicle = OdometerContract.OdometerEntry.VEHICLE_UNKNOWN;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mVehicle = OdometerContract.OdometerEntry.VEHICLE_1;
            }
        });
    }

    // TODO: something needs to call this function
    private void updateDuration(){
        // Create a ContentValues object where column names are the keys
        ContentValues values = new ContentValues();
        values.put(OdometerContract.LeaseDetails.COLUMN_DURATION, Integer.parseInt(mDurationEditText.getText().toString()));
        String whereClause = OdometerContract.OdometerEntry.COLUMN_VEHICLE_ID + "=" + mVehicle;
        String[] whereArgs = {};

        db.update(OdometerContract.LeaseDetails.TABLE_NAME, values, whereClause, whereArgs);
    }

}