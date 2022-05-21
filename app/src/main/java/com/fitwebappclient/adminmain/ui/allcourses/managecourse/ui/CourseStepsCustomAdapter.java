package com.fitwebappclient.adminmain.ui.allcourses.managecourse.ui;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.fitwebappclient.interfaces.BitmapHandler;
import com.fitwebappclient.R;
import com.fitwebappclient.adminmain.models.StepDao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class CourseStepsCustomAdapter extends RecyclerView.Adapter<CourseStepsCustomAdapter.ViewHolder> implements BitmapHandler {
    private ArrayList<StepDao> dataSet;
    private ManageCourseViewModel viewModel;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView stepName;
        private View root;

        public TextView getStepDesciption() {
            return stepDesciption;
        }

        private final TextView stepDesciption;

        private final TextView stepNumber;

        private final ImageView stepImage;

        public ImageView getStepImage() {
            return stepImage;
        }

        public TextView getStepNumber() {
            return stepNumber;
        }

        public TextView getStepName() {
            return stepName;
        }

        public ViewHolder(View view) {
            super(view);
            stepName = view.findViewById(R.id.all_steps_name);
            stepDesciption = view.findViewById(R.id.all_steps_description);
            stepNumber = view.findViewById(R.id.all_steps_number);
            stepImage = view.findViewById(R.id.all_steps_image);
            root = view;
            //TODO handle image
        }
    }

    public CourseStepsCustomAdapter(ManageCourseViewModel viewModel) {
        ArrayList<StepDao> steps = new ArrayList<StepDao>(Arrays.asList(viewModel.getCourseData().getValue().getSteps()));
        Collections.sort(steps, new Comparator<StepDao>() {
            @Override
            public int compare(StepDao st1, StepDao st2) {
                return st1.getStepNumber() - st2.getStepNumber();
            }
        });

        this.dataSet = steps;
        this.viewModel = viewModel;
    }

    @Override
    public CourseStepsCustomAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.step_row, viewGroup, false);
        return new CourseStepsCustomAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(CourseStepsCustomAdapter.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {
        viewHolder.getStepName().setText(dataSet.get(position).getName());
        viewHolder.getStepDesciption().setText(dataSet.get(position).getDescription());
        viewHolder.getStepNumber().setText("#" + String.valueOf(dataSet.get(position).getStepNumber()));
        viewHolder.getStepImage().setImageBitmap(getImageFromBase64(dataSet.get(position).getImage()));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.openStepActivity(dataSet.get(position), null);
            }
        });
    }

    @Override
    public int getItemCount() {
        try {
            this.dataSet = new ArrayList<StepDao>(Arrays.asList(viewModel.getCourseData().getValue().getSteps()));
            return this.dataSet.size();
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return 0;
        }
    }
}