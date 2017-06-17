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
package com.example.android.odometer.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Database helper for Lease Predictor app. Manages database creation and version management.
 */
public class OdometerDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = OdometerDbHelper.class.getSimpleName();

    /** Name of the database file */
    private static final String DATABASE_NAME = "lease_predictor.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructs a new instance of {@link OdometerDbHelper}.
     *
     * @param context of the app
     */
    public OdometerDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_ODOMETER_TABLE;

        // Create a String that contains the SQL statement to create the odometer readings table
        SQL_CREATE_ODOMETER_TABLE =  "CREATE TABLE " + OdometerContract.OdometerEntry.TABLE_NAME + " ("
                + OdometerContract.OdometerEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + OdometerContract.OdometerEntry.COLUMN_VEHICLE_ID + " INTEGER NOT NULL DEFAULT 1, "
                + OdometerContract.OdometerEntry.COLUMN_DATETIME + " DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, "
                + OdometerContract.OdometerEntry.COLUMN_ODOMETER + " INTEGER NOT NULL);";
        // Execute the SQL statement
        db.execSQL(SQL_CREATE_ODOMETER_TABLE);

        // Create a String that contains the SQL statement to create the lease deatils table
        SQL_CREATE_ODOMETER_TABLE =  "CREATE TABLE " + OdometerContract.LeaseDetails.TABLE_NAME + " ("
                + OdometerContract.LeaseDetails._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + OdometerContract.LeaseDetails.COLUMN_VEHICLE_ID + " INTEGER NOT NULL DEFAULT 1, "
                + OdometerContract.LeaseDetails.COLUMN_START_DATE + " DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, "
                + OdometerContract.LeaseDetails.COLUMN_START_MILEAGE + " INTEGER NOT NULL DEFAULT 0,"
                + OdometerContract.LeaseDetails.COLUMN_DURATION +   " INTEGER NOT NULL DEFAULT 36, "
                + OdometerContract.LeaseDetails.COLUMN_MAX_MILES +  " INTEGER NOT NULL DEFAULT 30000, "
                + OdometerContract.LeaseDetails.COLUMN_OVERAGE_COST + " DOUBLE NOT NULL DEFAULT 0.0);";
        // Execute the SQL statement
        db.execSQL(SQL_CREATE_ODOMETER_TABLE);

        // TODO:  Insert default rows for Vehicles 1 & 2
        ContentValues values = new ContentValues();
        // DO I need these or will the defaults work?  what would I insert then?  Otherwise remove defaults from table def
        values.put(OdometerContract.LeaseDetails.COLUMN_VEHICLE_ID, 1);
        values.put(OdometerContract.LeaseDetails.COLUMN_START_DATE, "2017-01-01");
        values.put(OdometerContract.LeaseDetails.COLUMN_START_MILEAGE, 0);
        values.put(OdometerContract.LeaseDetails.COLUMN_DURATION, 36);
        values.put(OdometerContract.LeaseDetails.COLUMN_MAX_MILES, 30000);
        values.put(OdometerContract.LeaseDetails.COLUMN_OVERAGE_COST, 0.25);
        db.insert(OdometerContract.LeaseDetails.TABLE_NAME, null, values);
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
    }
}