package com.fitwebappclient.adminmain.ui.allusers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fitwebappclient.adminmain.models.UserDaoShort;
import com.fitwebappclient.adminmain.services.AdminApiService;

import java.util.ArrayList;

public class AllUserViewModel extends ViewModel {
    private AdminApiService adminApiService;
    private MutableLiveData<ArrayList<UserDaoShort>> allUsers;
    private MutableLiveData<Boolean> status;
    private MutableLiveData<Boolean> deleteStatus;

    public LiveData<Boolean> getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(boolean deleteStatus) {
        this.deleteStatus.setValue(deleteStatus);
    }

    public AllUserViewModel() {
        this.status = new MutableLiveData<>();
        this.allUsers = new MutableLiveData<>();
        this.deleteStatus = new MutableLiveData<>();

        this.adminApiService = new AdminApiService(this,null);
    }

    public void setAllUsers(ArrayList<UserDaoShort> users) {
        this.allUsers.setValue(users);
    }

    public LiveData<ArrayList<UserDaoShort>> getAllUsers() {
        return this.allUsers;
    }


    public LiveData<Boolean> getStatus() {
        return this.status;
    }


    public void setUpStatus(boolean status) {
        this.status.setValue(status);
    }

    public void getAllUsersFromServer() {
        this.adminApiService.getAllUsersFromServer(this.allUsers);
    }

    public void deleteUser(int id) {
        this.adminApiService.deleteUserByID(id);
    }
}