package com.example.android.odometer;

/**
 * Created by ACornelius on 6/2/2017.
 */

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android.odometer.database.OdometerDbHelper;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class LineChartObj extends AppCompatActivity {

    private LineChart mChart;

    /** Database helper that will provide us access to the database */
    private OdometerDbHelper mDbHelper;

    private Data data;

    protected Typeface mTfRegular;
    protected Typeface mTfLight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("......... LineChartData.onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        mTfRegular = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        mTfLight = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");

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
        xAxis.setTypeface(mTfLight);
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
        leftAxis.setTypeface(mTfLight);
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

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void setDBConnection(OdometerDbHelper mDbHelper) {
        this.mDbHelper = mDbHelper;
    }

//    public void setChartObj(LineChart chart) {
//        this.mChart = chart;
//    }

    public void setDataObj(Data data) {
        this.data = data;
    }

    public void updateGraph() {
        System.out.println("--------  LineChartData.updateGraph()");

//        LineDataSet setActual = new LineDataSet(data.lineDataEntries, "Odometer Readings");

        List<Entry> valsComp1 = new ArrayList<Entry>();
        List<Entry> valsComp2 = new ArrayList<Entry>();
        Entry c1e1 = new Entry(0f, 100000f); // 0 == quarter 1
        valsComp1.add(c1e1);
        Entry c1e2 = new Entry(1f, 140000f); // 1 == quarter 2 ...
        valsComp1.add(c1e2);
        // and so on ...

        Entry c2e1 = new Entry(0f, 130000f); // 0 == quarter 1
        valsComp2.add(c2e1);
        Entry c2e2 = new Entry(1f, 115000f); // 1 == quarter 2 ...
        valsComp2.add(c2e2);

        LineDataSet setComp1 = new LineDataSet(valsComp1, "Company 1");
        setComp1.setAxisDependency(AxisDependency.LEFT);
        LineDataSet setComp2 = new LineDataSet(valsComp2, "Company 2");
        setComp2.setAxisDependency(AxisDependency.LEFT);
        // use the interface ILineDataSet
        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(setComp1);
        dataSets.add(setComp2);

        LineData data = new LineData(dataSets);
        mChart.setData(data);
        mChart.invalidate(); // refresh

//        setActual.setAxisDependency(AxisDependency.LEFT);
//        setActual.setColor(ColorTemplate.getHoloBlue());
//        setActual.setValueTextColor(ColorTemplate.getHoloBlue());
//        setActual.setLineWidth(1.5f);
//        setActual.setDrawCircles(false);
//        setActual.setDrawValues(false);
//        setActual.setFillAlpha(65);
//        setActual.setFillColor(ColorTemplate.getHoloBlue());
//        setActual.setHighLightColor(Color.rgb(244, 117, 117));
//        setActual.setDrawCircleHole(false);


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

    }

//    private LineData setData(int count) {
//// adding months in X-axis
//        ArrayList<String> xVals = new ArrayList<String>();
//        xVals.add("January");
//        xVals.add("February");
//        xVals.add("March");
//        xVals.add("April");
//        xVals.add("May");
//        xVals.add("June");
//        xVals.add("July");
//        xVals.add("August");
//        xVals.add("September");
//        xVals.add("October");
//        xVals.add("November");
//        xVals.add("December");
//
//        ArrayList<Entry> vals1 = new ArrayList<Entry>();
//// Sample data in trafficDataJSON:
////{"1":0,"2":0,"3":11,"4":0,"5":0,"6":0,"7":0,"8":0,"9":0,"10":0,"11":0,"12":0}
//// Adding data values for Y-Axis
//        for (int i = 1; i <= count; i++) {
////            try {
//                vals1.add(new Entry(i, i));
////                vals1.add(new Entry(trafficDataJSON.getInt("" + i), i));
////            } catch (JSONException e) {
////                e.printStackTrace();
////            }
//        }
//
//        // create a dataset and give it a type
//        LineDataSet set1 = new LineDataSet(vals1, "DataSet 1");
////        set1.setDrawCubic(true);
//        set1.setCubicIntensity(0.2f);
//        //set1.setDrawFilled(true);
//        set1.setDrawCircles(false);
//        set1.setLineWidth(1.8f);
//        set1.setCircleRadius(4f);
//        set1.setCircleColor(Color.BLACK);
//        set1.setHighLightColor(Color.rgb(244, 117, 117));
//        set1.setColor(Color.BLACK);
//        set1.setFillColor(Color.BLACK);
//        set1.setFillAlpha(100);
//        set1.setDrawHorizontalHighlightIndicator(false);
////        set1.setFillFormatter(new FillFormatter() {
////            @Override
////            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
////                return -10;
////            }
////        });
//
//        // create a data object with the datasets
//        LineData data = new LineData(xVals, set1);
////            data.setValueTypeface(tf);
//        data.setValueTextSize(9f);
//        data.setDrawValues(false);
//        return data;
//
//    }

//    private void setData(int count, float range) {
//        System.out.println("......... LineChartData.setData()");
//        // now in hours
//        long now = TimeUnit.MILLISECONDS.toHours(System.currentTimeMillis());
//
//        ArrayList<Entry> values = new ArrayList<Entry>();
//
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
//
//        // create a dataset and give it a type
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
//
//    }


    public void deleteGraphData() {
        System.out.println("--------  LineChartData.deleteGraphData()");
//        graph.removeAllSeries();
        mChart.clear();
    }

//    private Entry actualData() {
//        System.out.println("--------  Chart.actualData()");
//
//        ArrayList<Entry> entries = new ArrayList<>();
//        BarDataSet dataSet = new BarDataSet(data.entries, "Odometer Readings");
//    }

//    private DataPoint[] actualData_OLD() {
////        System.out.println("--------  Graph.actualData()");
//
//        ArrayList<Integer> readings = data.readings;
//        int count = readings.size();
//
//        DataPoint[] values = new DataPoint[count];
//        for (int i=0; i < count; i++) {
//            DataPoint v = new DataPoint(i, readings.get(i));
//            values[i] = v;
//        }
//        return values;
//    }

//    private DataPoint[] idealData_OLD() {
////        System.out.println("--------  Graph.idealData()");
//
//        int count = data.numberOfReadings;
//        double idealOdometer = 0;
//
//        DataPoint[] values = new DataPoint[count];
//        for (int i=0; i < count; i++) {
//            idealOdometer += data.dailyDistance;
//            DataPoint v = new DataPoint(i, idealOdometer);
//            values[i] = v;
//        }
//        System.out.println("......... Ideal Odometer = " + idealOdometer + "   @ " + data.dailyDistance + " per day");
//        return values;
//    }


}
