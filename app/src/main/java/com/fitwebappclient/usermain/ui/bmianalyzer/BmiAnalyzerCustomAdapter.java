package com.fitwebappclient.usermain.ui.bmianalyzer;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.fitwebappclient.R;
import com.fitwebappclient.interfaces.DialogsHandling;
import com.fitwebappclient.usermain.models.UserBMI;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BmiAnalyzerCustomAdapter extends RecyclerView.Adapter<BmiAnalyzerCustomAdapter.ViewHolder> {
    private ArrayList<UserBMI> localDataSet = new ArrayList<>();
    private BmiAnalyzerFragment bmiAnalyzerFragment;
    private BmiAnalyzerViewModel viewModel;
    private TextView noDataTextView;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewDate;
        private TextView textViewWeight;

        public ViewHolder(View view) {
            super(view);
            textViewDate = view.findViewById(R.id.bmi_analyzer_row_date);
            textViewWeight = view.findViewById(R.id.bmi_analyzer_row_weight);
        }

        public TextView getTextViewDate() {
            return textViewDate;
        }

        public TextView getTextViewWeight() {
            return textViewWeight;
        }
    }

    public BmiAnalyzerCustomAdapter(ArrayList<UserBMI> dataset ,View view, BmiAnalyzerFragment bmiAnalyzerFragment) {
        this.bmiAnalyzerFragment = bmiAnalyzerFragment;
        this.viewModel = bmiAnalyzerFragment.getViewModel();
        this.noDataTextView = view.findViewById(R.id.bmi_analyzer_textview_notify_no_data);
        this.noDataTextView.setVisibility(View.INVISIBLE);
        this.localDataSet = dataset;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.bmi_analyzer_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "SimpleDateFormat"})
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Date date = new Date();
        date.setTime(Long.parseLong(localDataSet.get(position).getDate()));

        viewHolder.getTextViewDate().setText(new SimpleDateFormat("dd-MM-yyyy").format(date));
        viewHolder.getTextViewWeight().setText(localDataSet.get(position).getWeight() / 1000 + "kg #" + (position + 1));
    }

    @Override
    public int getItemCount() {
        if (this.localDataSet != null) {
            if (localDataSet.size() == 0) {
                this.noDataTextView.setVisibility(View.VISIBLE);
            }else{
                this.noDataTextView.setVisibility(View.INVISIBLE);
            }
            return this.localDataSet.size();
        } else {
            this.noDataTextView.setVisibility(View.INVISIBLE);
            return 0;
        }
    }


}
