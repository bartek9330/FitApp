package com.fitwebappclient.loginandregistration.loginpage;

import android.app.Activity;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fitwebappclient.R;
import com.fitwebappclient.loginandregistration.loginservice.LogApiService;
import com.fitwebappclient.loginandregistration.models.UserData;

public class LoginPageViewModel extends ViewModel   {
    private LogApiService logApiService = new LogApiService();
    private UserData userData = new UserData();
    private Activity activity;

    private MutableLiveData<Boolean> loginStatus;

    public LoginPageViewModel(Activity activity) {
        this.activity = activity;
        this.loginStatus = new MutableLiveData<>();
    }

    public void setLoginStatus(Boolean loginStatus) {
        this.loginStatus.setValue(loginStatus);
    }

    public LiveData<Boolean> getLoginStatus() {
        return loginStatus;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
        userData.setUserRoles();
    }

    public void logIntoAccount(String userLogin, String userPassword){
        logApiService.getToken(this,userLogin,userPassword);
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