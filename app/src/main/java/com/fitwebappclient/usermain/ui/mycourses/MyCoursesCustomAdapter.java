package com.fitwebappclient.usermain.ui.mycourses;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.fitwebappclient.R;
import com.fitwebappclient.adminmain.ui.allcourses.managecourse.ManageCourseActivity;
import com.fitwebappclient.interfaces.BitmapHandler;
import com.fitwebappclient.interfaces.DialogsHandling;
import com.fitwebappclient.loginandregistration.models.ServerResponse;
import com.fitwebappclient.usermain.models.CourseWithStepsDao;
import com.fitwebappclient.usermain.ui.allavailablecourses.AllAvailableCoursesCustomAdapter;
import com.fitwebappclient.usermain.ui.allavailablecourses.AllAvailableCoursesFragment;
import com.fitwebappclient.usermain.ui.allavailablecourses.AllAvailableCoursesViewModel;
import com.fitwebappclient.usermain.ui.mycourses.openmycourse.OpenMyCourseActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;

public class MyCoursesCustomAdapter extends RecyclerView.Adapter<MyCoursesCustomAdapter.ViewHolder> implements BitmapHandler {
    private MyCoursesFragment myCoursesFragment;
    private ArrayList<CourseWithStepsDao> dataSet;
    private MyCoursesViewModel viewModel;
    private DialogsHandling dialogsHandling;
    private View root;
    private TextView textViewNotifyNoCoursess;

    public MyCoursesCustomAdapter(MyCoursesViewModel viewModel, DialogsHandling dialogsHandling, MyCoursesFragment myCoursesFragment, ConstraintLayout root, RecyclerView recyclerView, TextView textViewNotifyNoCoursess) {
        this.viewModel = viewModel;
        this.dialogsHandling = dialogsHandling;
        this.myCoursesFragment = myCoursesFragment;
        this.root = root;
        this.textViewNotifyNoCoursess = textViewNotifyNoCoursess;
        prepareData();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView courseName;

        private final TextView courseDesciption;

        private final TextView courseStepsNumber;

        private final ImageView courseImage;

        private final View root;

        private final ConstraintLayout row;

        public ConstraintLayout getRow() {
            return row;
        }

        public TextView getCourseName() {
            return courseName;
        }

        public TextView getCourseDesciption() {
            return courseDesciption;
        }

        public TextView getCourseStepsNumber() {
            return courseStepsNumber;
        }

        public ImageView getCourseImage() {
            return courseImage;
        }

        public View getRoot() {
            return root;
        }

        public ViewHolder(View view) {
            super(view);
            courseName = view.findViewById(R.id.allcourses_row_name);
            courseDesciption = view.findViewById(R.id.allcourses_row_description);
            courseStepsNumber = view.findViewById(R.id.allcourses_row_steps);
            courseImage = view.findViewById(R.id.all_courses_image);
            row = (ConstraintLayout) view.findViewById(R.id.course_row);

            this.root = view;
        }

    }


    private void prepareData() {
        LiveData<ArrayList<CourseWithStepsDao>> response = viewModel.getMyCourses();
        dialogsHandling.enableLoadingBar();
        if (!response.hasActiveObservers()) {
            response.observe(myCoursesFragment, new Observer<ArrayList<CourseWithStepsDao>>() {
                @Override
                public void onChanged(ArrayList<CourseWithStepsDao> serverResponse) {
                    dialogsHandling.disableLoadingBar();
                    if (serverResponse != null) {
                        try {
                            dataSet = serverResponse;
                            notifyDataSetChanged();
                        } catch (Exception exception) {
                            System.out.println(exception.getMessage());
                            dialogsHandling.handleErrorWhileLoadingData();
                        }
                    } else {
                        dialogsHandling.handleErrorWhileLoadingData();
                    }
                }
            });
        }
    }

    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
            int position = viewHolder.getAdapterPosition();
            dialogsHandling.enableLoadingBar();
            LiveData<ServerResponse> response = viewModel.unassignUserToCourse(dataSet.get(position));
            if (!response.hasActiveObservers()) {
                response.observe(myCoursesFragment, new Observer<ServerResponse>() {
                    @Override
                    public void onChanged(ServerResponse hasChange) {
                        if (hasChange != null) {
                            handleCourseRemoved(position);
                        } else {
                            dialogsHandling.handleErrorWhileLoadingData();
                        }
                    }
                });
            }
        }
    };

    private void handleCourseRemoved(int position) {
        notifyItemRemoved(position);
        Toast.makeText(root.getContext(), root.getResources().getText(R.string.unassign_user_from_course), Toast.LENGTH_SHORT).show();
        prepareData();
        dialogsHandling.disableLoadingBar();
    }

    @Override
    public MyCoursesCustomAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.all_courses_recyclerview_row, viewGroup, false);

        return new MyCoursesCustomAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyCoursesCustomAdapter.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {
        viewHolder.getCourseName().setText(dataSet.get(position).getName());
        viewHolder.getCourseDesciption().setText(dataSet.get(position).getDescription());
        viewHolder.getCourseImage().setImageBitmap(getImageFromBase64(dataSet.get(position).getImage()));
        viewHolder.getCourseStepsNumber().setText(viewHolder.root.getResources().getText(R.string.steps_number) +
                " " +
                String.valueOf(dataSet.get(position).getSteps() == null ? 0 : dataSet.get(position).getSteps().length));

        viewHolder.getRow().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleClickOnCourse(dataSet.get(position));
            }
        });
    }

    private void handleClickOnCourse(CourseWithStepsDao courseWithStepsDao) {
        if (courseWithStepsDao != null) {
            Intent intent = new Intent(root.getContext(), OpenMyCourseActivity.class);
            intent.putExtra("courseId", courseWithStepsDao.getId());
            root.getContext().startActivity(intent);
        }

    }

    @Override
    public int getItemCount() {
        if (this.dataSet != null) {
            if (dataSet.size() == 0) {
                this.textViewNotifyNoCoursess.setVisibility(View.VISIBLE);
            }
            return this.dataSet.size();
        } else {
            this.textViewNotifyNoCoursess.setVisibility(View.INVISIBLE);
            return 0;
        }
    }
}

