package com.fitwebappclient.adminmain.ui.allcourses;

import android.content.Intent;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fitwebappclient.adminmain.models.CourseDao;
import com.fitwebappclient.adminmain.models.StepDao;
import com.fitwebappclient.adminmain.services.AdminApiService;
import com.fitwebappclient.adminmain.ui.allcourses.managecourse.ManageCourseActivity;
import com.fitwebappclient.loginandregistration.models.ServerResponse;

import java.util.ArrayList;

public class AllCoursesViewModel extends ViewModel {
    private AdminApiService adminApiService;
    private MutableLiveData<ArrayList<CourseDao>> allCourses;
    private MutableLiveData<Boolean> status;

    private MutableLiveData<ServerResponse> responseStatus;

    public void setResponseStatus(ServerResponse step) {
        this.responseStatus.setValue(step);
    }

    private LiveData<ServerResponse> getServerResponseStatus() {
        return this.responseStatus;
    }

    public AllCoursesViewModel() {
        this.adminApiService = new AdminApiService(this);
        this.allCourses = new MutableLiveData<>();
        this.status = new MutableLiveData<>();
        this.responseStatus = new MutableLiveData<>();
    }

    public LiveData<Boolean> getStatus() {
        return this.status;
    }

    public void setUpStatus(boolean status) {
        this.status.setValue(status);
    }

    public void setAllCourses(ArrayList<CourseDao> courses) {
        this.allCourses.setValue(courses);
    }

    public LiveData<ArrayList<CourseDao>> getAllCourses() {
        return this.allCourses;
    }

    public void getAllCoursesFromServer() {
        this.adminApiService.getAllCourses(this.allCourses);
    }

    public void openCourseActivity(CourseDao courseDao, View view) {
        Intent intent = new Intent(view.getContext(), ManageCourseActivity.class);
        if (courseDao != null) {
            intent.putExtra("courseData", courseDao);
        }
        view.getContext().startActivity(intent);
    }

    public LiveData<ServerResponse> deleteCourseFromServer(CourseDao courseDao) {
        adminApiService.deleteCourseFromServer(courseDao,this.responseStatus);
        return this.getServerResponseStatus();
    }
}
