package com.example.android.odometer;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android.odometer.database.OdometerContract.OdometerEntry;
import com.example.android.odometer.database.OdometerDbHelper;

/**
 * Created by ACornelius on 6/2/2017.
 */

public class SampleData extends AppCompatActivity {

    /** Database helper that will provide us access to the database */
    private OdometerDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDbHelper = new OdometerDbHelper(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void saveSampleReadings(SQLiteDatabase db, Integer vehicleID){
        // Define the sample data in an Array
        String[][] sampleData = new String[][] {
                {"12/2/2016", "160"},
                {"12/12/2016", "365"},
                {"12/18/2016", "435"},
                {"12/20/2016", "730"},
                {"12/23/2016", "1000"},
                {"12/28/2016", "1035"},
                {"12/29/2016", "1066"},
                {"1/3/2017", "1121"},
                {"1/4/2017", "1134"},
                {"1/9/2017", "1194"},
                {"1/10/2017", "1204"},
                {"1/19/2017", "1297"},
                {"1/27/2017", "1362"},
                {"2/1/2017", "1478"},
                {"2/4/2017", "1519"},
                {"2/5/2017", "1519"},
                {"2/6/2017", "1519"},
                {"2/7/2017", "1758"},
                {"2/8/2017", "1770"},
                {"2/10/2017", "1807"},
                {"2/11/2017", "2062"},
                {"2/12/2017", "2087"},
                {"2/16/2017", "2087"},
                {"2/18/2017", "2148"},
                {"2/20/2017", "2156"},
                {"2/21/2017", "2165"},
                {"2/23/2017", "2225"},
                {"2/25/2017", "2266"},
                {"2/26/2017", "2272"},
                {"2/27/2017", "2272"},
                {"2/28/2017", "2500"}
        };

        // Create a ContentValues object where column names are the keys,
        // and attributes are the values.
        ContentValues values = new ContentValues();

        for (int i=0; i < sampleData.length; i++) {

            values.put(OdometerEntry.COLUMN_VEHICLE_ID, vehicleID);
            values.put(OdometerEntry.COLUMN_DATETIME, sampleData[i][0]);
            values.put(OdometerEntry.COLUMN_ODOMETER, sampleData[i][1]);

            long newRowId = db.insert(OdometerEntry.TABLE_NAME, null, values);
            System.out.println("--- Inserted " + sampleData[i][0] + ", " + sampleData[i][1]);
        }
        System.out.println("--------  Added " + sampleData.length + " rows of sample data");

    }

}
