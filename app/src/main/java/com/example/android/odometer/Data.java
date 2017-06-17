package com.example.android.odometer;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android.odometer.database.OdometerContract;
import com.example.android.odometer.database.OdometerDbHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

//import static android.R.id.input;

/**
 * Created by ACornelius on 6/7/2017.
 */

public class Data extends AppCompatActivity {

    private OdometerDbHelper mDbHelper;
    private Cursor cursor;

    public Integer lastOdometerValue = 0;
    public String lastOdometerDate = "";
    public Integer numberOfReadings = 0;
    public Integer lastRecordID = 0;

//    public Integer leaseDuration = 36;   // in months
//    public Integer leaseMileage = 30000; //
//    public Double dailyDistance = leaseMileage / (leaseDuration / 12) / 365.25 ;
    public Integer leaseStartMileage;
    public Date leaseStartDate;
    public Double leaseOverageCost;
    public Integer leaseDuration;
    public Integer leaseMileage;
    public Double dailyDistance;

    private DateFormat formatter;
//    private DateTimeFormatter formatter;

    public ArrayList <Integer> readings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateLeaseDetails(1);
    }

    public void setDBConnection(OdometerDbHelper mDbHelper) {
        this.mDbHelper = mDbHelper;
    }

    public void refresh() {
        System.out.println("......... Data refresh()");
        updateReadings(1);
    }

//    private ArrayList getAllOdometerReadings(int vehicle_ID) {
    private void updateReadings(int vehicle_ID) {
        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
//        ArrayList <Integer> readings = new ArrayList<>();

        readings = new ArrayList<>();
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = { OdometerContract.OdometerEntry._ID,
                                OdometerContract.OdometerEntry.COLUMN_ODOMETER,
                                OdometerContract.OdometerEntry.COLUMN_DATETIME};

        // Perform a query on the mileage table
        cursor = db.query(
                OdometerContract.OdometerEntry.TABLE_NAME,   // The table to query
                projection,            // The columns to return
                OdometerContract.OdometerEntry.COLUMN_VEHICLE_ID + "=" + vehicle_ID,  // The WHERE clause
                null,
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                OdometerContract.OdometerEntry._ID + " ASC");                   // The sort order

        try {
            if(cursor.getCount() > 0) {
                numberOfReadings = cursor.getCount();
                cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount(); i++) {
                    readings.add(cursor.getInt(cursor.getColumnIndex(OdometerContract.OdometerEntry.COLUMN_ODOMETER)));
                    cursor.moveToNext();
                }
                cursor.moveToLast();
                lastOdometerValue = cursor.getInt(cursor.getColumnIndex(OdometerContract.OdometerEntry.COLUMN_ODOMETER));
                lastOdometerDate = cursor.getString(cursor.getColumnIndex(OdometerContract.OdometerEntry.COLUMN_DATETIME));
                lastRecordID = cursor.getInt(cursor.getColumnIndex(OdometerContract.OdometerEntry._ID));
            } else{
                numberOfReadings = 0;
                lastOdometerValue = 0;
                lastOdometerDate = "";
                lastRecordID = 0;
                readings.add(0);
            }

        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }

//        return readings;
    }



    private void updateLeaseDetails (int vehicle_ID) {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = { OdometerContract.LeaseDetails.COLUMN_START_DATE,
                OdometerContract.LeaseDetails.COLUMN_START_MILEAGE,
                OdometerContract.LeaseDetails.COLUMN_DURATION,
                OdometerContract.LeaseDetails.COLUMN_MAX_MILES,
                OdometerContract.LeaseDetails.COLUMN_OVERAGE_COST};

        // Perform a query on the mileage table
        cursor = db.query(
                OdometerContract.LeaseDetails.TABLE_NAME,   // The table to query
                projection,            // The columns to return
                OdometerContract.LeaseDetails.COLUMN_VEHICLE_ID + "=" + vehicle_ID,  // The WHERE clause
                null,
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                 // The sort order

        try {
            if(cursor.getCount() == 1) {
                leaseStartDate = StringToDate(cursor.getString(cursor.getColumnIndex(OdometerContract.LeaseDetails.COLUMN_START_DATE)));
                leaseStartMileage = cursor.getInt(cursor.getColumnIndex(OdometerContract.LeaseDetails.COLUMN_START_MILEAGE));
                leaseDuration = cursor.getInt(cursor.getColumnIndex(OdometerContract.LeaseDetails.COLUMN_DURATION));
                leaseMileage = cursor.getInt(cursor.getColumnIndex(OdometerContract.LeaseDetails.COLUMN_MAX_MILES));
                leaseOverageCost = cursor.getDouble(cursor.getColumnIndex(OdometerContract.LeaseDetails.COLUMN_OVERAGE_COST));
                if (leaseDuration > 0) {
                    dailyDistance = leaseMileage / (leaseDuration / 12) / 365.25 ;
                } else {
                    dailyDistance = 0.0;
                }
            } else {
                leaseStartDate = StringToDate("2016-01-01 00:00:00");
                leaseStartMileage = 0;
                leaseDuration = 36;
                leaseMileage = 30000;
                leaseOverageCost = 0.0;
                dailyDistance = 0.0;
            }

        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }

    }

    private Date StringToDate(String strDate) {
        String exampleDate = "2017-01-01";
        Date d;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            d = sdf.parse(exampleDate);
        } catch (ParseException e) {
//            Logger.getLogger(Prime.class.getName()).log(Level.SEVERE, null, e);
            Calendar calendar = Calendar.getInstance();
            calendar.set(2016, 0, 1, 0, 0, 0); // or GregorianCalendar(year + 1900, month, date, hrs, min, sec)
            d = calendar.getTime();
            e.printStackTrace();
        }
        return d;
    }
}
