package com.fitwebappclient.adminmain.services;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.fitwebappclient.adminmain.models.CourseDao;
import com.fitwebappclient.adminmain.models.StepDao;
import com.fitwebappclient.adminmain.models.UserDaoShort;
import com.fitwebappclient.adminmain.ui.allcourses.AllCoursesViewModel;
import com.fitwebappclient.adminmain.ui.allcourses.managecourse.ui.managestep.ManageStepViewModel;
import com.fitwebappclient.adminmain.ui.allusers.AllUserViewModel;
import com.fitwebappclient.adminmain.ui.createuser.AdminActivityCreateUserViewModel;
import com.fitwebappclient.loginandregistration.MainLoginActivity;
import com.fitwebappclient.loginandregistration.loginservice.LogApiService;
import com.fitwebappclient.loginandregistration.models.ServerResponse;
import com.fitwebappclient.loginandregistration.models.TokenJWN;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdminApiService {
    private final Retrofit retrofit = new Retrofit.Builder().baseUrl(MainLoginActivity.serverIp).addConverterFactory(GsonConverterFactory.create()).build();
    private final AdminApiServer adminApiServer = retrofit.create(AdminApiServer.class);
    private AllUserViewModel allUserViewModel;
    private AllCoursesViewModel allCoursesViewModel;
    private AdminActivityCreateUserViewModel adminActivityCreateUserViewModel;
    private TokenJWN tokenJWN;

    public AdminApiService(AllUserViewModel allUserViewModel,AdminActivityCreateUserViewModel adminActivityCreateUserViewModel) {
        this.tokenJWN = LogApiService.getTokenJWN();
        if(adminActivityCreateUserViewModel != null){
            this.adminActivityCreateUserViewModel = adminActivityCreateUserViewModel;
        }

        if(allUserViewModel != null){
            this.allUserViewModel = allUserViewModel;
        }
    }

    public AdminApiService(AllCoursesViewModel allCoursesViewModel) {
        this.tokenJWN = LogApiService.getTokenJWN();
        this.allCoursesViewModel = allCoursesViewModel;
    }

    public AdminApiService() {
        this.tokenJWN = LogApiService.getTokenJWN();
    }

    public void getAllUsersFromServer(MutableLiveData<ArrayList<UserDaoShort>> allUsers) {
        Call<List<UserDaoShort>> call = adminApiServer.getAllUsers("Bearer " + tokenJWN.getToken());

        call.enqueue(new Callback<List<UserDaoShort>>() {
            @Override
            public void onResponse(Call<List<UserDaoShort>> call, retrofit2.Response<List<UserDaoShort>> response) {
                if (response.code() == 200) {
                    allUserViewModel.setAllUsers((ArrayList<UserDaoShort>) response.body());
                    allUserViewModel.setUpStatus(true);
                } else {
                    allUserViewModel.setUpStatus(false);
                }
            }

            @Override
            public void onFailure(Call<List<UserDaoShort>> call, Throwable t) {
                System.out.println("fail" + t.getMessage());
                allUserViewModel.setUpStatus(false);

            }
        });
    }

    public void deleteUserByID(int id) {
        HashMap<String,Integer> jsonId = new HashMap<>();
        jsonId.put("userId",id);

        Call<ServerResponse> call = adminApiServer.deleteUser(jsonId,"Bearer " + tokenJWN.getToken());

        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                if (response.code() == 200) {
                    allUserViewModel.setDeleteStatus(true);
                } else {
                    allUserViewModel.setDeleteStatus(false);
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                System.out.println("fail" + t.getMessage());
                allUserViewModel.setDeleteStatus(false);
            }
        });
    }

    public void addUserToServer(UserDaoShort userDaoShort) {
        Call<ServerResponse> call = adminApiServer.registerUser(userDaoShort);

        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                if (response.code() == 201) {
                    adminActivityCreateUserViewModel.setUpStatus(true);
                } else {
                    adminActivityCreateUserViewModel.setUpStatus(false);
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                System.out.println("fail" + t.getMessage());
                adminActivityCreateUserViewModel.setUpStatus(false);
            }
        });
    }

    public void getAllCourses(MutableLiveData<ArrayList<CourseDao>> allCourses) {
        Call<List<CourseDao>> call = adminApiServer.getAllCourses("Bearer " + tokenJWN.getToken());

        call.enqueue(new Callback<List<CourseDao>>() {
            @Override
            public void onResponse(Call<List<CourseDao>> call, retrofit2.Response<List<CourseDao>> response) {
                if (response.code() == 200) {
                    allCoursesViewModel.setAllCourses((ArrayList<CourseDao>) response.body());
                    allCoursesViewModel.setUpStatus(true);
                } else {
                    allCoursesViewModel.setUpStatus(false);
                }
            }

            @Override
            public void onFailure(Call<List<CourseDao>> call, Throwable t) {
                System.out.println("fail" + t.getMessage());
                allCoursesViewModel.setUpStatus(false);
            }
        });
    }

    public void getCourseDetails(CourseDao courseDao, MutableLiveData<CourseDao> courseData) {
        HashMap<String,Integer> serverRequest = new HashMap<>();
        serverRequest.put("courseId",courseDao.getId());
        Call<CourseDao> call = adminApiServer.getCourseDetails(serverRequest,"Bearer " + tokenJWN.getToken());

        call.enqueue(new Callback<CourseDao>() {
            @Override
            public void onResponse(Call<CourseDao> call, retrofit2.Response<CourseDao> response) {
                if (response.code() == 200) {
                    courseData.setValue(response.body());
                } else {
                    courseData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<CourseDao> call, Throwable t) {
                System.out.println("fail" + t.getMessage());
                courseData.setValue(null);
            }
        });
    }

    public void handleStepOnServer(boolean isUpdate,StepDao stepDao, ManageStepViewModel serverResponse) {
        Call<ServerResponse> call = null;
        if(isUpdate) {
            call = adminApiServer.editStepOnServer(stepDao, "Bearer " + tokenJWN.getToken());
        }else{
            call = adminApiServer.createStepOnServer(stepDao, "Bearer " + tokenJWN.getToken());
        }

        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                if (response.code() == 200) {
                    serverResponse.setResponseStatus(response.body());
                } else {
                    serverResponse.setResponseStatus(null);
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                System.out.println("fail" + t.getMessage());
                serverResponse.setResponseStatus(null);
            }
        });
    }

    public void deleteStepFromServer(StepDao stepDao, MutableLiveData<ServerResponse> serverResponse) {
        HashMap<String,Integer> requestData = new HashMap<>();
        requestData.put("stepId",stepDao.getStepId());
        Call<ServerResponse> call = adminApiServer.deleteStepOnServer(requestData, "Bearer " + tokenJWN.getToken());
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


    public void createNewCourseOnServer(CourseDao courseDao, MutableLiveData<ServerResponse> serverResponseStatus) {
        Call<ServerResponse> call = adminApiServer.createCourseOnServer(courseDao, "Bearer " + tokenJWN.getToken());
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                if (response.code() == 200) {
                    serverResponseStatus.setValue(response.body());
                } else {
                    serverResponseStatus.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                System.out.println("fail" + t.getMessage());
                serverResponseStatus.setValue(null);
            }
        });
    }

    public void deleteCourseFromServer(CourseDao courseDao, MutableLiveData<ServerResponse> responseStatus) {
        HashMap<String,Integer> requestData = new HashMap<>();
        requestData.put("courseId",courseDao.getId());
        Call<ServerResponse> call = adminApiServer.deleteCourseOnServer(requestData, "Bearer " + tokenJWN.getToken());
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                if (response.code() == 200) {
                    responseStatus.setValue(response.body());
                } else {
                    responseStatus.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                System.out.println("fail" + t.getMessage());
                responseStatus.setValue(null);
            }
        });
    }

    public void updateCourseOnServer(CourseDao courseDao, MutableLiveData<ServerResponse> serverResponse) {
        Call<ServerResponse> call = adminApiServer.updateCourseOnServer(courseDao, "Bearer " + tokenJWN.getToken());
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
}
