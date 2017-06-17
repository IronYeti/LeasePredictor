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
package com.example.android.odometer.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Database helper for Lease Mileage app. Manages database creation and version management.
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
        // Create a String that contains the SQL statement to create the pets table
        String SQL_CREATE_ODOMETER_TABLE =  "CREATE TABLE " + OdometerContract.OdometerEntry.TABLE_NAME + " ("
                + OdometerContract.OdometerEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + OdometerContract.OdometerEntry.COLUMN_VEHICLE_ID + " INTEGER NOT NULL DEFAULT 1, "
                + OdometerContract.OdometerEntry.COLUMN_DATETIME + " DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, "
                + OdometerContract.OdometerEntry.COLUMN_ODOMETER + " INTEGER NOT NULL);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_ODOMETER_TABLE);
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
    }
}