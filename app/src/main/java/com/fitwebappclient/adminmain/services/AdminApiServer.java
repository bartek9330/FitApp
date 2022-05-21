package com.fitwebappclient.adminmain.services;

import com.fitwebappclient.adminmain.models.CourseDao;
import com.fitwebappclient.adminmain.models.StepDao;
import com.fitwebappclient.adminmain.models.UserDaoShort;
import com.fitwebappclient.loginandregistration.models.ServerResponse;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AdminApiServer {
    @POST("getAllUsers")
    Call<List<UserDaoShort>> getAllUsers(@Header("Authorization") String jwtToken);

    @POST("deleteUser")
    Call<ServerResponse> deleteUser(@Body HashMap<String,Integer> body, @Header("Authorization") String jwtToken);

    @POST("registerUser")
    Call<ServerResponse> registerUser(@Body UserDaoShort body);

    @POST("getAllCourses")
    Call<List<CourseDao>> getAllCourses(@Header("Authorization") String jwtToken);

    @POST("getCourses")
    Call<CourseDao> getCourseDetails(@Body HashMap<String,Integer> body, @Header("Authorization") String jwtToken);

    @POST("addCourse")
    Call<ServerResponse> createCourseOnServer(@Body CourseDao body, @Header("Authorization") String jwtToken);

    @POST("editCourse")
    Call<ServerResponse> updateCourseOnServer(@Body CourseDao body, @Header("Authorization") String jwtToken);

    @POST("deleteCourse")
    Call<ServerResponse> deleteCourseOnServer(@Body HashMap<String, Integer> body, @Header("Authorization") String jwtToken);

    @POST("editStep")
    Call<ServerResponse> editStepOnServer(@Body StepDao body, @Header("Authorization") String jwtToken);

    @POST("addStep")
    Call<ServerResponse> createStepOnServer(@Body StepDao body, @Header("Authorization") String jwtToken);

    @POST("deleteStep")
    Call<ServerResponse> deleteStepOnServer(@Body HashMap<String,Integer> body, @Header("Authorization") String jwtToken);




}
