package com.fitwebappclient.usermain.ui.mycourses.openmycourse;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.fitwebappclient.R;
import com.fitwebappclient.adminmain.models.StepDao;
import com.fitwebappclient.adminmain.ui.allcourses.managecourse.ui.CourseStepsCustomAdapter;
import com.fitwebappclient.adminmain.ui.allcourses.managecourse.ui.ManageCourseViewModel;
import com.fitwebappclient.interfaces.BitmapHandler;
import com.fitwebappclient.usermain.models.CourseWithStepsDao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class OpenMyCourseAdapter extends RecyclerView.Adapter<OpenMyCourseAdapter.ViewHolder> implements BitmapHandler {
    private StepDao[] dataSet;
    private OpenMyCourseViewModel viewModel;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView stepName;
        private View root;

        public TextView getStepDesciption() {
            return stepDesciption;
        }

        private final TextView stepDesciption;


        private final ImageView stepImage;

        public ImageView getStepImage() {
            return stepImage;
        }


        public TextView getStepName() {
            return stepName;
        }

        public ViewHolder(View view) {
            super(view);
            stepName = view.findViewById(R.id.all_steps_name);
            stepDesciption = view.findViewById(R.id.all_steps_description);
            stepImage = view.findViewById(R.id.all_steps_image);
            root = view;
            //TODO handle image
        }
    }

    public OpenMyCourseAdapter(CourseWithStepsDao courseWithStepsDao) {
        this.dataSet = courseWithStepsDao.getSteps();
    }

    @Override
    public OpenMyCourseAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_open_my_course, viewGroup, false);
        return new OpenMyCourseAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(OpenMyCourseAdapter.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {
        viewHolder.getStepName().setText("#" + String.valueOf(dataSet[position].getStepNumber()) + " " + dataSet[position].getName());
        viewHolder.getStepDesciption().setText(dataSet[position].getDescription());
        viewHolder.getStepImage().setImageBitmap(getImageFromBase64(dataSet[position].getImage()));
    }

    @Override
    public int getItemCount() {
        try {
            return this.dataSet.length;
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return 0;
        }
    }
}