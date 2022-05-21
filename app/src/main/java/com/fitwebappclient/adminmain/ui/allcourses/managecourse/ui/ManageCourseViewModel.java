package com.fitwebappclient.adminmain.ui.allcourses.managecourse.ui;

import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.fragment.NavHostFragment;

import com.fitwebappclient.R;
import com.fitwebappclient.adminmain.models.CourseDao;
import com.fitwebappclient.adminmain.models.StepDao;
import com.fitwebappclient.adminmain.services.AdminApiService;
import com.fitwebappclient.loginandregistration.models.ServerResponse;

public class ManageCourseViewModel extends ViewModel {
    private AdminApiService adminApiService;
    private MutableLiveData<CourseDao> courseData;
    private MutableLiveData<ServerResponse> serverResponse;

    public ManageCourseFragment getManageCourseFragment() {
        return manageCourseFragment;
    }

    public void setManageCourseFragment(ManageCourseFragment manageCourseFragment) {
        this.manageCourseFragment = manageCourseFragment;
    }
//xx
    private ManageCourseFragment manageCourseFragment;

    public ManageCourseViewModel() {
        this.adminApiService = new AdminApiService();
        this.courseData = new MutableLiveData<>();
        this.serverResponse = new MutableLiveData<>();
    }

    public void getDataFromServer(CourseDao courseDao) {
        adminApiService.getCourseDetails(courseDao,this.courseData);
    }

    public void setCourseData(CourseDao course) {
        this.courseData.setValue(course);
    }

    public LiveData<CourseDao> getCourseData() {
        return this.courseData;
    }

    public void openStepActivity(StepDao stepDao,CourseDao courseDao) {
        if(stepDao != null){
            try{
                Bundle args = new Bundle();
                args.putSerializable("stepDao",stepDao);
                NavHostFragment.findNavController(getManageCourseFragment())
                        .navigate(R.id.action_manageCourseFragment_to_manageStep,args);
            }catch (Exception exception){
                System.out.println(exception.getMessage());
            }
        }else if(courseDao != null){
            try{
                Bundle args = new Bundle();
                args.putSerializable("courseDao",courseDao);
                NavHostFragment.findNavController(getManageCourseFragment())
                        .navigate(R.id.action_manageCourseFragment_to_manageStep,args);
            }catch (Exception exception){
                System.out.println(exception.getMessage());
            }
        }
    }

    public void setResponseStatus(ServerResponse step) {
        this.serverResponse.setValue(step);
    }

    private LiveData<ServerResponse> getServerResponseStatus() {
        return this.serverResponse;
    }

    public LiveData<ServerResponse> deleteStepFromServer(StepDao stepDao) {
        adminApiService.deleteStepFromServer(stepDao,this.serverResponse);
        return this.getServerResponseStatus();
    }

    public  LiveData<ServerResponse> createNewCourseOnServer(CourseDao courseDao){
        adminApiService.createNewCourseOnServer(courseDao,this.serverResponse);
        return this.getServerResponseStatus();
    }

    public LiveData<ServerResponse> updateCourseOnServer(CourseDao courseDao) {
        adminApiService.updateCourseOnServer(courseDao,this.serverResponse);
        return this.getServerResponseStatus();
    }
}