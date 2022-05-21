package com.fitwebappclient.usermain.ui.mycourses.openmycourse;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.fitwebappclient.usermain.models.CourseWithStepsDao;
import com.fitwebappclient.usermain.services.UserService;

public class OpenMyCourseViewModel {
    private final UserService userService;
    private final MutableLiveData<CourseWithStepsDao> courseData;

    public OpenMyCourseViewModel() {
        this.courseData = new MutableLiveData<>();
        this.userService = new UserService();
    }


    public void setCourse(CourseWithStepsDao course) {
        this.courseData.setValue(course);
    }

    public LiveData<CourseWithStepsDao> getCourse(int courseId) {
        this.userService.getCourseData(this.courseData,courseId);
        return this.courseData;
    }
}
