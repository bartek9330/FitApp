package com.fitwebappclient.loginandregistration.registerpage;

import android.app.Activity;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fitwebappclient.R;
import com.fitwebappclient.loginandregistration.MainLoginActivity;
import com.fitwebappclient.loginandregistration.loginservice.LogApiService;

public class RegistrationPageViewModel extends ViewModel {
    private LogApiService apiService = new LogApiService();

    private String registerStatusMessage;
    private Activity activity;

    public RegistrationPageViewModel(Activity activity) {
        this.activity = activity;
    }

    public String getRegisterStatusMessage() {
        return registerStatusMessage;
    }

    public void setRegisterStatusMessage(String registerStatusMessage) {
        this.registerStatusMessage = registerStatusMessage;
    }

    private MutableLiveData<Boolean> registrationStatus = new MutableLiveData<>();


    public LiveData<Boolean> getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(Boolean registrationStatus) {
        this.registrationStatus.setValue(registrationStatus);
    }

    public void registerUser(String login, String email, String password){
        try{
            apiService.registerUser(login,email,password,this);
        }catch (Exception exception){
            throw exception;
        }
    }

    public void setConnectionErrorAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(activity.getResources().getString(R.string.server_connection_error))
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                       activity.finish();
                    }
                });
        builder.show();
    }
}
