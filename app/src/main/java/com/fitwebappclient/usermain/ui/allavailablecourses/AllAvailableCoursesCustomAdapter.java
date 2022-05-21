package com.fitwebappclient.usermain.ui.allavailablecourses;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.fitwebappclient.R;
import com.fitwebappclient.adminmain.MainAdminActivity;
import com.fitwebappclient.adminmain.models.UserDaoShort;
import com.fitwebappclient.adminmain.models.UserRoleDaoShort;
import com.fitwebappclient.adminmain.ui.allusers.AllUserViewModel;
import com.fitwebappclient.adminmain.ui.allusers.AllUsersRecyclerViewAdapter;
import com.fitwebappclient.interfaces.BitmapHandler;
import com.fitwebappclient.interfaces.DialogsHandling;
import com.fitwebappclient.usermain.models.CourseWithStepsDao;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AllAvailableCoursesCustomAdapter extends RecyclerView.Adapter<AllAvailableCoursesCustomAdapter.ViewHolder> implements BitmapHandler {
    private AllAvailableCoursesFragment allAvailableCoursesFragment;
    private ArrayList<CourseWithStepsDao> dataSet;
    private AllAvailableCoursesViewModel viewModel;
    private DialogsHandling dialogsHandling;
    private View root;
    private TextView textViewNotifyNoCoursess;


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

        public View getRoot(){
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

    public AllAvailableCoursesCustomAdapter(AllAvailableCoursesViewModel allAvailableCoursesViewModel, DialogsHandling dialogsHandling, AllAvailableCoursesFragment allAvailableCoursesFragment, View root, TextView textViewNotifyNoCoursess) {
        this.textViewNotifyNoCoursess = textViewNotifyNoCoursess;
        this.viewModel = allAvailableCoursesViewModel;
        this.dialogsHandling = dialogsHandling;
        this.allAvailableCoursesFragment = allAvailableCoursesFragment;
        this.root = root;
        prepareData();
    }

    void prepareData() {
        LiveData<ArrayList<CourseWithStepsDao>> response = viewModel.getAllCourses();
        dialogsHandling.enableLoadingBar();
        if (!response.hasActiveObservers()) {
            response.observe(allAvailableCoursesFragment, new Observer<ArrayList<CourseWithStepsDao>>() {
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

    @Override
    public AllAvailableCoursesCustomAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.all_courses_recyclerview_row, viewGroup, false);

        return new AllAvailableCoursesCustomAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(AllAvailableCoursesCustomAdapter.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {
        viewHolder.getCourseName().setText(dataSet.get(position).getName());
        viewHolder.getCourseDesciption().setText(dataSet.get(position).getDescription());
        viewHolder.getCourseImage().setImageBitmap(getImageFromBase64(dataSet.get(position).getImage()));
        viewHolder.getCourseStepsNumber().setText( viewHolder.root.getResources().getText(R.string.steps_number) +
                " " +
                String.valueOf(dataSet.get(position).getSteps() == null? 0 : dataSet.get(position).getSteps().length));

        viewHolder.getRow().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleClickOnCourse(dataSet.get(position),position);
            }
        });
    }

    private void handleClickOnCourse(CourseWithStepsDao courseWithStepsDao, int position) {
        openDialog(courseWithStepsDao,position);
    }

    private void openDialog(CourseWithStepsDao courseWithStepsDao, int position) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        allAvailableCoursesFragment.addUserToCourse(courseWithStepsDao);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(root.getContext());
        builder.setMessage(root.getResources().getString(R.string.user_ask_wanna_join_to_course)).setPositiveButton(root.getResources().getString(R.string.yes), dialogClickListener)
                .setNegativeButton(root.getResources().getString(R.string.no), dialogClickListener).show();
    }

    @Override
    public int getItemCount() {
        if(this.dataSet != null){
            if(this.dataSet.size() == 0){
                textViewNotifyNoCoursess.setVisibility(View.VISIBLE);
            }
            return this.dataSet.size();
        }else{
            this.textViewNotifyNoCoursess.setVisibility(View.INVISIBLE);
            return 0;
        }
    }
}

