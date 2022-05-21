package com.fitwebappclient.usermain.services;

import androidx.lifecycle.MutableLiveData;

import com.fitwebappclient.adminmain.models.UserDaoShort;
import com.fitwebappclient.adminmain.services.AdminApiServer;
import com.fitwebappclient.loginandregistration.MainLoginActivity;
import com.fitwebappclient.loginandregistration.loginservice.LogApiService;
import com.fitwebappclient.loginandregistration.models.ServerResponse;
import com.fitwebappclient.loginandregistration.models.TokenJWN;
import com.fitwebappclient.loginandregistration.models.UserData;
import com.fitwebappclient.usermain.MainUserActivity;
import com.fitwebappclient.usermain.models.CourseWithStepsDao;
import com.fitwebappclient.usermain.models.UserBMI;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserService {
    private final Retrofit retrofit = new Retrofit.Builder().baseUrl(MainLoginActivity.serverIp).addConverterFactory(GsonConverterFactory.create()).build();
    private final UserApiServer api = retrofit.create(UserApiServer.class);
    private final TokenJWN tokenJWN = LogApiService.getTokenJWN();
    private final UserData userData = MainUserActivity.getUserData();



    public void getAllAvailableCourses(MutableLiveData<ArrayList<CourseWithStepsDao>> allCourses) {
        HashMap<String,Integer> user = new HashMap<>();
        user.put("userId",userData.getId());
        Call<List<CourseWithStepsDao>> call = api.getAllAvailableCourses(user,"Bearer " + tokenJWN.getToken());

        call.enqueue(new Callback<List<CourseWithStepsDao>>() {
            @Override
            public void onResponse(Call<List<CourseWithStepsDao>> call, retrofit2.Response<List<CourseWithStepsDao>> response) {
                if (response.code() == 200) {
                    allCourses.setValue((ArrayList<CourseWithStepsDao>) response.body());
                } else {
                    allCourses.setValue(null);
                }
            }
            @Override
            public void onFailure(Call<List<CourseWithStepsDao>> call, Throwable t) {
                System.out.println("fail" + t.getMessage());
                allCourses.setValue(null);
            }
        });
    }

    public void getMyCourses(MutableLiveData<ArrayList<CourseWithStepsDao>> allCourses) {
        HashMap<String,Integer> user = new HashMap<>();
        user.put("userId",userData.getId());
        Call<List<CourseWithStepsDao>> call = api.getMyCourses(user,"Bearer " + tokenJWN.getToken());

        call.enqueue(new Callback<List<CourseWithStepsDao>>() {
            @Override
            public void onResponse(Call<List<CourseWithStepsDao>> call, retrofit2.Response<List<CourseWithStepsDao>> response) {
                if (response.code() == 200) {
                    allCourses.setValue((ArrayList<CourseWithStepsDao>) response.body());
                } else {
                    allCourses.setValue(null);
                }
            }
            @Override
            public void onFailure(Call<List<CourseWithStepsDao>> call, Throwable t) {
                System.out.println("fail" + t.getMessage());
                allCourses.setValue(null);
            }
        });
    }

    public void addUserToCourse(CourseWithStepsDao courseWithStepsDao, MutableLiveData<ServerResponse> serverResponse) {
        HashMap<String,Integer> user = new HashMap<>();
        user.put("userId",userData.getId());
        user.put("courseId",courseWithStepsDao.getId());

        Call<ServerResponse> call = api.assignUserToCourse(user,"Bearer " + tokenJWN.getToken());

        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                if (response.code() == 200) {
                    serverResponse.setValue(response.body());
                } else {
                    serverResponse.setValue(null);
                }
            }
            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                System.out.println("fail" + t.getMessage());
                serverResponse.setValue(null);
            }
        });
    }

    public void unassignUserToCourse(CourseWithStepsDao courseWithStepsDao, MutableLiveData<ServerResponse> serverResponse) {
        HashMap<String,Integer> user = new HashMap<>();
        user.put("userId",userData.getId());
        user.put("courseId",courseWithStepsDao.getId());

        Call<ServerResponse> call = api.unassignUserToCourse(user,"Bearer " + tokenJWN.getToken());

        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                if (response.code() == 200) {
                    serverResponse.setValue(response.body());
                } else {
                    serverResponse.setValue(null);
                }
            }
            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                System.out.println("fail" + t.getMessage());
                serverResponse.setValue(null);
            }
        });
    }

    public void getCourseData(MutableLiveData<CourseWithStepsDao> courseData, int courseId) {
        HashMap<String,Integer> request = new HashMap<>();
        request.put("courseId",courseId);

        Call<CourseWithStepsDao> call = api.getCourseData(request,"Bearer " + tokenJWN.getToken());

        call.enqueue(new Callback<CourseWithStepsDao>() {
            @Override
            public void onResponse(Call<CourseWithStepsDao> call, retrofit2.Response<CourseWithStepsDao> response) {
                if (response.code() == 200) {
                    courseData.setValue(response.body());
                } else {
                    courseData.setValue(null);
                }
            }
            @Override
            public void onFailure(Call<CourseWithStepsDao> call, Throwable t) {
                System.out.println("fail" + t.getMessage());
                courseData.setValue(null);
            }
        });
    }

    public void saveUserBMI(UserBMI userBMI, MutableLiveData<ServerResponse> serverResponse) {
        HashMap<String,String> request = new HashMap<>();
        try{
            request.put("userId",String.valueOf(userData.getId()));
            request.put("height",String.valueOf(userBMI.getHeight()));
            request.put("weight",String.valueOf(userBMI.getWeight()));
            request.put("date",userBMI.getDate());
        }catch (Exception exception){
            System.out.println(exception.getMessage());
        }

        Call<ServerResponse> call = api.saveBMI(request,"Bearer " + tokenJWN.getToken());

        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                if (response.code() == 200) {
                    serverResponse.setValue(response.body());
                } else {
                    serverResponse.setValue(null);
                }
            }
            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                System.out.println("fail" + t.getMessage());
                serverResponse.setValue(null);
            }
        });
    }

    public void getAllBMIs(MutableLiveData<ArrayList<UserBMI>> allBMIs) {
        HashMap<String,Integer> user = new HashMap<>();
        user.put("userId",userData.getId());
        Call<List<UserBMI>> call = api.getMyBMI(user,"Bearer " + tokenJWN.getToken());

        call.enqueue(new Callback<List<UserBMI>>() {
            @Override
            public void onResponse(Call<List<UserBMI>> call, retrofit2.Response<List<UserBMI>> response) {
                if (response.code() == 200) {
                    allBMIs.setValue((ArrayList<UserBMI>) response.body());
                } else {
                    allBMIs.setValue(null);
                }
            }
            @Override
            public void onFailure(Call<List<UserBMI>> call, Throwable t) {
                System.out.println("fail" + t.getMessage());
                allBMIs.setValue(null);
            }
        });
    }
}
