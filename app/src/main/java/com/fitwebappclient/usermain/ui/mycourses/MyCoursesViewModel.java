package com.fitwebappclient.usermain.ui.mycourses;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fitwebappclient.loginandregistration.models.ServerResponse;
import com.fitwebappclient.usermain.models.CourseWithStepsDao;
import com.fitwebappclient.usermain.services.UserService;

import java.util.ArrayList;

public class MyCoursesViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<CourseWithStepsDao>> allCourses;
    private final MutableLiveData<ServerResponse> serverResponse;

    private final UserService userService;

    public MyCoursesViewModel() {
        this.serverResponse = new MutableLiveData<>();
        this.allCourses = new MutableLiveData<>();
        this.userService = new UserService();
    }

    public void setAllCourses(ArrayList<CourseWithStepsDao> courses) {
        this.allCourses.setValue(courses);
    }

    public void setServerResponse(ServerResponse response) {
        this.serverResponse.setValue(response);
    }

    public LiveData<ServerResponse> getServerResponse() {
        return this.serverResponse;
    }


    public LiveData<ArrayList<CourseWithStepsDao>> getMyCourses() {
        this.userService.getMyCourses(this.allCourses);
        return this.allCourses;
    }

    public LiveData<ServerResponse> unassignUserToCourse(CourseWithStepsDao courseWithStepsDao) {
        this.userService.unassignUserToCourse(courseWithStepsDao,this.serverResponse);
        return this.getServerResponse();
    }



}