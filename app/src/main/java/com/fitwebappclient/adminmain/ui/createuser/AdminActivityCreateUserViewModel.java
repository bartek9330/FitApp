package com.fitwebappclient.adminmain.ui.createuser;

import android.app.ProgressDialog;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fitwebappclient.adminmain.models.UserDaoShort;
import com.fitwebappclient.adminmain.services.AdminApiService;

public class AdminActivityCreateUserViewModel extends ViewModel {
    private AdminApiService adminApiService;
    private MutableLiveData<Boolean> status;


    public AdminActivityCreateUserViewModel() {
        this.status = new MutableLiveData<>();
        this.adminApiService = new AdminApiService(null,this);
    }


    public LiveData<Boolean> getStatus() {
        return this.status;
    }


    public void setUpStatus(boolean status) {
        this.status.setValue(status);
    }

    public void registerUser(UserDaoShort userDaoShort){
        try{
            this.adminApiService.addUserToServer(userDaoShort);
        }catch (Exception exception){
            System.out.println(exception);
        }
    }
}
