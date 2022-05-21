package com.fitwebappclient.usermain.services;

import com.fitwebappclient.loginandregistration.models.ServerResponse;
import com.fitwebappclient.usermain.models.CourseWithStepsDao;
import com.fitwebappclient.usermain.models.UserBMI;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface UserApiServer {

    @POST("getAvailableCoursesForUser")
    Call<List<CourseWithStepsDao>> getAllAvailableCourses(@Body HashMap<String,Integer> body, @Header("Authorization") String jwtToken);

    @POST("getMyCourses")
    Call<List<CourseWithStepsDao>> getMyCourses(@Body HashMap<String,Integer> body, @Header("Authorization") String jwtToken);

    @POST("assignUserToCourse")
    Call<ServerResponse> assignUserToCourse(@Body HashMap<String,Integer> body, @Header("Authorization") String jwtToken);

    @POST("unassignUserToCourse")
    Call<ServerResponse> unassignUserToCourse(@Body HashMap<String,Integer> body, @Header("Authorization") String jwtToken);

    @POST("getCourses")
    Call<CourseWithStepsDao> getCourseData(@Body HashMap<String,Integer> body, @Header("Authorization") String jwtToken);

    @POST("saveBMI")
    Call<ServerResponse> saveBMI(@Body HashMap<String,String> body, @Header("Authorization") String jwtToken);

    @POST("getMyBMI")
    Call<List<UserBMI>> getMyBMI(@Body HashMap<String,Integer> body, @Header("Authorization") String jwtToken);
}
