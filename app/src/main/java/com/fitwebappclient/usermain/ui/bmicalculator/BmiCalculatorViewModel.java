package com.fitwebappclient.usermain.ui.bmicalculator;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fitwebappclient.loginandregistration.models.ServerResponse;
import com.fitwebappclient.usermain.models.CourseWithStepsDao;
import com.fitwebappclient.usermain.models.UserBMI;
import com.fitwebappclient.usermain.services.UserService;

public class BmiCalculatorViewModel extends ViewModel {
    private final MutableLiveData<ServerResponse> serverResponse;
    private final UserService userService;


    public BmiCalculatorViewModel(){
        this.serverResponse = new MutableLiveData<>();
        this.userService = new UserService();
    }

    public void setServerResponse(ServerResponse response) {
        this.serverResponse.setValue(response);
    }

    public LiveData<ServerResponse> getServerResponse() {
        return this.serverResponse;
    }

    public LiveData<ServerResponse> saveUserBMI(UserBMI userBMI) {
        this.userService.saveUserBMI(userBMI,this.serverResponse);
        return this.getServerResponse();
    }



}