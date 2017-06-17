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

import android.provider.BaseColumns;

/**
 * API Contract for the Pets app.
 */
public final class OdometerContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private OdometerContract() {}

    /**
     * Inner class that defines constant values for the pets database table.
     * Each entry in the table represents a single pet.
     */
    public static final class OdometerEntry implements BaseColumns {

        /** Name of database table for odometer */
        public final static String TABLE_NAME = "odometer";

        /**
         * Unique ID number for the date/mileage entry (only for use in the database table).
         *
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * ID of the vehicle.
         *
         * Type: INTEGER
         */
        public final static String COLUMN_VEHICLE_ID = "vehicle_id";

        /**
         * Date & time of the odometer entry.
         *
         * Type: DATETIME
         */
        public final static String COLUMN_DATETIME = "date";

        /**
         * Odometer reading for a specific date.
         *
         * Type: INTEGER
         */
        public final static String COLUMN_ODOMETER = "odometer";

        /**
         * Possible values for the vehicle ID.
         */
        public static final int VEHICLE_UNKNOWN = 0;
        public static final int VEHICLE_1 = 1;
        public static final int VEHICLE_2 = 2;
    }

}

