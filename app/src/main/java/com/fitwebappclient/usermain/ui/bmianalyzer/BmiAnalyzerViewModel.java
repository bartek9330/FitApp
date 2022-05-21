package com.fitwebappclient.usermain.ui.bmianalyzer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.fitwebappclient.usermain.models.CourseWithStepsDao;
import com.fitwebappclient.usermain.models.UserBMI;
import com.fitwebappclient.usermain.services.UserService;

import java.util.ArrayList;

public class BmiAnalyzerViewModel extends ViewModel {
    private final UserService userService;
    private final MutableLiveData<ArrayList<UserBMI>> allBMIs;

    public BmiAnalyzerViewModel() {
        this.allBMIs = new MutableLiveData<>();
        this.userService = new UserService();
    }

    public void setAllBMI(ArrayList<UserBMI> userBMIS) {
        this.allBMIs.setValue(userBMIS);
    }

    public LiveData<ArrayList<UserBMI>> getAllBMIs() {
        this.userService.getAllBMIs(this.allBMIs);
        return this.allBMIs;
    }

}