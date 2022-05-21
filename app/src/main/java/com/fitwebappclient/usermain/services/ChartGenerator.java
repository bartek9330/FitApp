package com.fitwebappclient.usermain.services;

import android.graphics.Color;
import android.view.View;

import com.fitwebappclient.usermain.models.UserBMI;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class ChartGenerator implements BMI {
    public void setUpCharts(CombinedChart combinedChart, ArrayList<UserBMI> serverResponse) {
        if (serverResponse.size() > 0) {
            combinedChart.setVisibility(View.VISIBLE);
        }
        combinedChart.setScaleEnabled(true);
        combinedChart.setDragEnabled(true);

        CombinedData data = new CombinedData();
        data.setData(generateLineData(serverResponse));
        data.setData(generateBarData(serverResponse));

        combinedChart.setData(data);
        XAxis xAxis = combinedChart.getXAxis();
        YAxis leftAxis = combinedChart.getAxisLeft();
        leftAxis.setEnabled(false);
        configureChartYAxisLabels(xAxis, serverResponse.size());

        combinedChart.getDescription().setEnabled(false);
        combinedChart.invalidate();
    }

    private LineData generateLineData(ArrayList<UserBMI> serverResponse) {
        try {
            LineData lineData = new LineData();
            ArrayList<Entry> entries = new ArrayList<>();
            for (UserBMI userBMI : serverResponse) {
                entries.add(new Entry(serverResponse.indexOf(userBMI) + 1, userBMI.getWeight() / 1000));
            }
            int color = Color.rgb(0, 0, 204);

            LineDataSet set = new LineDataSet(entries, "Weight");
            set.setColor(color);
            set.setLineWidth(2.5f);
            set.setCircleColor(color);
            set.setCircleRadius(5f);
            set.setFillColor(color);
            set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set.setDrawValues(true);
            set.setValueTextSize(10f);
            set.setValueTextColor(color);

            set.setAxisDependency(YAxis.AxisDependency.LEFT);
            lineData.addDataSet(set);

            return lineData;
        } catch (Exception exception) {
            System.out.println(exception);
            return null;
        }
    }

    private BarData generateBarData(ArrayList<UserBMI> serverResponse) {
        try {
            ArrayList<BarEntry> entries = new ArrayList<>();
            for (UserBMI userBMI : serverResponse) {
                double bmi = calcBMI((double) userBMI.getWeight() / 1000, (double) userBMI.getHeight() / 100);
                entries.add(new BarEntry(serverResponse.indexOf(userBMI) + 1, (float) bmi));
            }
            BarDataSet set = new BarDataSet(entries, "BMI");
            set.setColor(Color.rgb(60, 220, 78));
            set.setValueTextColor(Color.rgb(60, 220, 78));
            set.setValueTextSize(10f);
            set.setAxisDependency(YAxis.AxisDependency.LEFT);
            float barWidth = 0.1f;
            BarData barData = new BarData(set);
            barData.setBarWidth(barWidth);
            return barData;
        } catch (Exception exception) {
            System.out.println(exception);
            return null;
        }
    }

    private void configureChartYAxisLabels(XAxis xAxis, int count) {
        xAxis.setAxisMaxValue(count + 1);
        xAxis.setLabelCount(count + 1, true);
    }
}
