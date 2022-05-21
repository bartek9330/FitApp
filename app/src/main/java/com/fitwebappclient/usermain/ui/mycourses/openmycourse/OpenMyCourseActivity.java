package com.fitwebappclient.usermain.ui.mycourses.openmycourse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fitwebappclient.R;
import com.fitwebappclient.adminmain.models.CourseDao;
import com.fitwebappclient.interfaces.BitmapHandler;
import com.fitwebappclient.interfaces.DialogsHandling;
import com.fitwebappclient.loginandregistration.models.ServerResponse;
import com.fitwebappclient.usermain.models.CourseWithStepsDao;

public class OpenMyCourseActivity extends AppCompatActivity implements BitmapHandler {
    private CourseWithStepsDao courseWithStepsDao;
    private DialogsHandling dialogsHandling;
    private OpenMyCourseViewModel viewModel;
    private RecyclerView recyclerView;
    private OpenMyCourseAdapter viewAdapter;
    private TextView courseName;
    private TextView courseDescription;
    private ImageView imageView;

    private View root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_my_course);
        viewModel = new OpenMyCourseViewModel();
        root = findViewById(android.R.id.content).getRootView();
        dialogsHandling = new DialogsHandling(root);

        if (getIntent().getSerializableExtra("courseId") != null) {
            getCourseDataFromServer((Integer) getIntent().getSerializableExtra("courseId"));
        }

    }

    private void getCourseDataFromServer(int courseId) {
        LiveData<CourseWithStepsDao> response = viewModel.getCourse(courseId);
        dialogsHandling.enableLoadingBar();
        if (!response.hasActiveObservers()) {
            response.observe(this, new Observer<CourseWithStepsDao>() {
                @Override
                public void onChanged(CourseWithStepsDao serverResponse) {
                    dialogsHandling.disableLoadingBar();
                    if (serverResponse != null) {
                        courseWithStepsDao = serverResponse;
                        setUpViews();
                        setUpRecyclerView();
                    } else {
                        dialogsHandling.handleErrorWhileLoadingData();
                    }
                }
            });
        }
    }

    private void setUpViews() {
        this.courseName = root.findViewById(R.id.open_my_course_name);
        this.courseDescription = root.findViewById(R.id.open_my_course_description);
        this.recyclerView = root.findViewById(R.id.open_my_course_recyclerview);
        this.imageView = root.findViewById(R.id.open_my_course_image);

        setTitle(courseWithStepsDao.getName());
        courseName.setText(courseWithStepsDao.getName());
        courseDescription.setText(courseWithStepsDao.getDescription());
        imageView.setImageBitmap(getImageFromBase64(courseWithStepsDao.getImage()));

    }

    private void setUpRecyclerView() {


        viewAdapter = new OpenMyCourseAdapter(courseWithStepsDao);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setItemViewCacheSize(512);
        this.recyclerView.setLayoutManager(linearLayoutManager);
        this.recyclerView.setAdapter(viewAdapter);
    }
}