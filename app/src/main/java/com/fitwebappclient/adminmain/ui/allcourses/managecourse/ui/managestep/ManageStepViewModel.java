package com.fitwebappclient.adminmain.ui.allcourses.managecourse.ui.managestep;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fitwebappclient.adminmain.models.StepDao;
import com.fitwebappclient.adminmain.services.AdminApiService;
import com.fitwebappclient.loginandregistration.models.ServerResponse;

public class ManageStepViewModel extends ViewModel {
    private AdminApiService adminApiService;
    private MutableLiveData<ServerResponse> responseStatus;

    public ManageStepViewModel() {
        this.adminApiService = new AdminApiService();
        this.responseStatus = new MutableLiveData<>();
    }

    public LiveData<ServerResponse> updateStepOnServer(StepDao stepDao) {
        adminApiService.handleStepOnServer(true, stepDao, this);
        return this.getServerResponseStatus();
    }

    public void setResponseStatus(ServerResponse step) {
        this.responseStatus.setValue(step);
    }

    private LiveData<ServerResponse> getServerResponseStatus() {
        return this.responseStatus;
    }

    public LiveData<ServerResponse> createStepOnServer(StepDao stepDao) {
        adminApiService.handleStepOnServer(false, stepDao, this);
        return this.getServerResponseStatus();
    }

    public LiveData<ServerResponse> deleteStepFromServer(StepDao stepDao) {
        adminApiService.deleteStepFromServer(stepDao, this.responseStatus);
        return this.getServerResponseStatus();
    }
}
