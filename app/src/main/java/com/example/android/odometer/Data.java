package com.example.android.odometer;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android.odometer.database.OdometerContract;
import com.example.android.odometer.database.OdometerDbHelper;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;

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
    public ArrayList <BarEntry> entries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("__Debug: Data.onCreate");
        updateLeaseDetails(1);
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("__Debug: Data.onStart");
//        updateLeaseDetails(1);
    }

    public void setDBConnection(OdometerDbHelper mDbHelper) {
        this.mDbHelper = mDbHelper;
    }

    public void refresh() {
        System.out.println("......... Data.refresh()");
        updateReadings(1);
        refreshLeaseDetatils();
//        updateLeaseDetails(1);
    }

    public void refreshLeaseDetatils() {
        System.out.println("......... Data.refreshLeaseDetails()");
        updateLeaseDetails(1);
    }

    //    private ArrayList getAllOdometerReadings(int vehicle_ID) {
    private void updateReadings(int vehicle_ID) {

        System.out.println("--------  Data.updateReadings...");

        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
//        ArrayList <Integer> readings = new ArrayList<>();

        readings = new ArrayList<>();
        entries = new ArrayList<>();

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
//                Long millis = 0L;
                Long seconds = 0L;
                numberOfReadings = cursor.getCount();
                cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount(); i++) {
                    readings.add(cursor.getInt(cursor.getColumnIndex(OdometerContract.OdometerEntry.COLUMN_ODOMETER)));
                    String dt = cursor.getString(cursor.getColumnIndex(OdometerContract.OdometerEntry.COLUMN_DATETIME));

                    try {
                        seconds = new SimpleDateFormat("MM/dd/yyyy").parse(dt).getTime() / 1000000;
                    } catch (ParseException e) {
                        try {
                            seconds = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dt).getTime() / 1000000;
                        } catch (ParseException e2) {
//                            e2.printStackTrace();
                            System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX  " + dt);
                            continue;
                        }
                    }

                    Long xVal = seconds; //cursor.getLong(cursor.getColumnIndex(OdometerContract.OdometerEntry.COLUMN_DATETIME));
                    Integer yVal = cursor.getInt(cursor.getColumnIndex(OdometerContract.OdometerEntry.COLUMN_ODOMETER));

                    System.out.println("entries = " + xVal + yVal + "   " + dt);

                    entries.add(new BarEntry(xVal, yVal));
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

        // TODO: Fix this Null check
//        if (dailyDistance.isNaN()) {
//            System.out.println("___Debug: trapped null");
//            updateLeaseDetails(1);
//        }

//        updateLeaseDetails(1);
//        return readings;
    }



    private void updateLeaseDetails (int vehicle_ID) {

        System.out.println("--------- Data.updateLeaseDetails...");
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
            cursor.moveToFirst();
            if(cursor.getCount() == 1) {
                leaseStartMileage = cursor.getInt(cursor.getColumnIndex(OdometerContract.LeaseDetails.COLUMN_START_MILEAGE));
                leaseDuration = cursor.getInt(cursor.getColumnIndex(OdometerContract.LeaseDetails.COLUMN_DURATION));
                leaseMileage = cursor.getInt(cursor.getColumnIndex(OdometerContract.LeaseDetails.COLUMN_MAX_MILES));
                leaseOverageCost = cursor.getDouble(cursor.getColumnIndex(OdometerContract.LeaseDetails.COLUMN_OVERAGE_COST));
                leaseStartDate = stringToDate(cursor.getString(cursor.getColumnIndex(OdometerContract.LeaseDetails.COLUMN_START_DATE)));
                System.out.println("");
                String debugDate = "Date = " + new SimpleDateFormat("MM/dd/yyyy");
                System.out.println(debugDate);
                System.out.println("");
                if (leaseDuration > 0) {
                    dailyDistance = leaseMileage / (leaseDuration / 12) / 365.25 ;
                } else {
                    dailyDistance = 0.0;
                }
            } else {
                if (cursor.getCount() == 0) {
                    System.out.println("********** No data found in Data.updateLeaseDetails");
                    System.out.println("********** No data found in Data.updateLeaseDetails");
                    System.out.println("********** No data found in Data.updateLeaseDetails");
                }
                if (cursor.getCount() > 1) {
                    System.out.println("********** Multiple rows found in Data.updateLeaseDetails (only 1 expected)");
                    System.out.println("********** Multiple rows found in Data.updateLeaseDetails (only 1 expected)");
                    System.out.println("********** Multiple rows found in Data.updateLeaseDetails (only 1 expected)");
                }
                leaseStartDate = stringToDate("2016-01-04 00:00:00");
                leaseStartMileage = 0;
                leaseDuration = 12;
                leaseMileage = 12345;
                leaseOverageCost = 0.0;
                dailyDistance = 0.0;
            }

        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }

    }

    public Date stringToDate(String strDate) {
        System.out.println("--------- Data.stringToDate");
//        String exampleDate = "2017-01-01";
        Date d;
//        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        try {
//            d = sdf.parse(exampleDate);
            d = sdf.parse(strDate);
        } catch (ParseException e) {
//            Logger.getLogger(Prime.class.getName()).log(Level.SEVERE, null, e);
            System.out.println(" ******** Exception parsing this date: " + strDate);
            Calendar calendar = Calendar.getInstance();
            calendar.set(2016, 0, 3, 0, 0, 0); // or GregorianCalendar(year + 1900, month, date, hrs, min, sec)
            d = calendar.getTime();
            e.printStackTrace();
        }
        return d;
    }

    public Date stringToDateTime(String strDate) {
//        String exampleDate = "2017-01-01 00:00:00";
        Date d;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        try {
//            d = sdf.parse(exampleDate);
            d = sdf.parse(strDate);
        } catch (ParseException e) {
//            Logger.getLogger(Prime.class.getName()).log(Level.SEVERE, null, e);
            Calendar calendar = Calendar.getInstance();
            calendar.set(2016, 0, 2, 0, 0, 0); // or GregorianCalendar(year + 1900, month, date, hrs, min, sec)
            d = calendar.getTime();
            e.printStackTrace();
        }
        return d;
    }
}
