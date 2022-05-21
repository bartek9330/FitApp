package com.fitwebappclient.adminmain.ui.allcourses.managecourse;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

import com.fitwebappclient.R;
import com.fitwebappclient.adminmain.MainAdminActivity;
import com.fitwebappclient.adminmain.models.CourseDao;
import com.fitwebappclient.adminmain.ui.allcourses.managecourse.ui.ManageCourseFragment;

public class ManageCourseActivity extends AppCompatActivity {


    public static CourseDao courseDao;

    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            if (getIntent().getSerializableExtra("courseData") != null) {
                courseDao = ((CourseDao) getIntent().getSerializableExtra("courseData"));
            }
        }catch (Exception exception){
            System.out.println(exception.getMessage());
        }


        setContentView(R.layout.manage_course_activity);

        NavController navController = Navigation.findNavController(this, R.id.activity_manage_course);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        if(courseDao != null){
            ((AppCompatActivity) this).getSupportActionBar().setTitle(courseDao.getName());
        }

//        if (savedInstanceState == null) {
//            ManageCourseFragment manageCourseFragment = new ManageCourseFragment();
//            manageCourseFragment.setArguments(getIntent().getExtras());
//
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.container, manageCourseFragment)
//                    .commitNow();
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        courseDao = null;
    }
}