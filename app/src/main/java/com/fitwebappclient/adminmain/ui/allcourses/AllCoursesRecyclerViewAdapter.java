package com.fitwebappclient.adminmain.ui.allcourses;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.fitwebappclient.interfaces.BitmapHandler;
import com.fitwebappclient.R;
import com.fitwebappclient.adminmain.models.CourseDao;
import java.util.ArrayList;

public class AllCoursesRecyclerViewAdapter extends RecyclerView.Adapter<AllCoursesRecyclerViewAdapter.ViewHolder> implements BitmapHandler {
    private ArrayList<CourseDao> dataSet;
    private AllCoursesViewModel viewModel;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private View root;

        private final TextView courseName;

        public TextView getCourseDesciption() {
            return courseDesciption;
        }

        private final TextView courseDesciption;

        private final TextView courseStepsNumber;

        private final ImageView courseImage;

        public ImageView getCourseImage() {
            return courseImage;
        }

        public TextView getCourseStepsNumber() {
            return courseStepsNumber;
        }

        public TextView getCourseName() {
            return courseName;
        }

        public ViewHolder(View view) {
            super(view);
            courseName = view.findViewById(R.id.allcourses_row_name);
            courseDesciption = view.findViewById(R.id.allcourses_row_description);
            courseStepsNumber = view.findViewById(R.id.allcourses_row_steps);
            courseImage = view.findViewById(R.id.all_courses_image);
            root = view;
        }
    }

    public AllCoursesRecyclerViewAdapter(AllCoursesViewModel viewModel) {
        this.dataSet = viewModel.getAllCourses().getValue();
        this.viewModel = viewModel;
    }

    @Override
    public AllCoursesRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.all_courses_recyclerview_row, viewGroup, false);

        return new AllCoursesRecyclerViewAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(AllCoursesRecyclerViewAdapter.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {
        viewHolder.getCourseName().setText(dataSet.get(position).getName());
        viewHolder.getCourseDesciption().setText(dataSet.get(position).getDescription());
        viewHolder.getCourseImage().setImageBitmap(getImageFromBase64(dataSet.get(position).getImage()));
        viewHolder.getCourseStepsNumber().setText( viewHolder.root.getResources().getText(R.string.steps_number)+ " " + String.valueOf(dataSet.get(position).getNumberOfSteps()));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.openCourseActivity(dataSet.get(position),view);
            }
        });
    }

    @Override
    public int getItemCount() {
        this.dataSet = viewModel.getAllCourses().getValue();
        return viewModel.getAllCourses().getValue().size();
    }
}

