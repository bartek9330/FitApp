package com.fitwebappclient.loginandregistration.loginservice;

import com.fitwebappclient.loginandregistration.MainLoginActivity;
import com.fitwebappclient.loginandregistration.loginpage.LoginPageViewModel;
import com.fitwebappclient.loginandregistration.models.ServerResponse;
import com.fitwebappclient.loginandregistration.models.TokenJWN;
import com.fitwebappclient.loginandregistration.models.UserData;
import com.fitwebappclient.loginandregistration.registerpage.RegistrationPageViewModel;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LogApiService {
    private Retrofit retrofit = new Retrofit.Builder().baseUrl(MainLoginActivity.serverIp).addConverterFactory(GsonConverterFactory.create()).build();
    private LoginApiServer loginApiServer = retrofit.create(LoginApiServer.class);

    public static TokenJWN getTokenJWN() {
        return tokenJWN;
    }

    public static TokenJWN tokenJWN = new TokenJWN();

    public void getToken(LoginPageViewModel loginPageViewModel, String userLogin, String userPassword) {
        HashMap<String, String> jsonObject = new HashMap<String, String>();
        jsonObject.put("username", userLogin);
        jsonObject.put("password", userPassword);

        Call<TokenJWN> call = loginApiServer.getToken(jsonObject);

        call.enqueue(new Callback<TokenJWN>() {
            @Override
            public void onResponse(Call<TokenJWN> call, retrofit2.Response<TokenJWN> response) {
                System.out.println("token " + response.code());
                if (response.code() == 200) {
                    tokenJWN.setToken(response.body().getToken());
                    getUserData(userLogin, loginPageViewModel);
                    System.out.println(tokenJWN.getToken());
                } else {
                    loginPageViewModel.setLoginStatus(false);
                }
            }

            @Override
            public void onFailure(Call<TokenJWN> call, Throwable t) {
                System.out.println("fail" + t.getMessage());
                loginPageViewModel.setLoginStatus(false);
                loginPageViewModel.setConnectionErrorAlert();
            }
        });
    }

    private void getUserData(String userLogin, LoginPageViewModel loginPageViewModel) {
        HashMap<String, String> jsonObject = new HashMap<String, String>();
        jsonObject.put("login", userLogin);

        Call<UserData> call = loginApiServer.getUserByLogin(jsonObject, "Bearer " + tokenJWN.getToken());
        try {
            call.enqueue(new Callback<UserData>() {
                @Override
                public void onResponse(Call<UserData> call, retrofit2.Response<UserData> response) {
                    System.out.println("UserData " + response.code());
                    if (response.code() == 200) {
                        loginPageViewModel.setUserData(response.body());
                        loginPageViewModel.setLoginStatus(true);
                    } else {
                        loginPageViewModel.setLoginStatus(false);
                    }
                }

                @Override
                public void onFailure(Call<UserData> call, Throwable t) {
                    System.out.println("fail" + t.getMessage());
                    loginPageViewModel.setLoginStatus(false);
                    loginPageViewModel.setConnectionErrorAlert();
                }
            });
        } catch (Exception exception) {
            throw exception;
        }
    }

    public void registerUser(String login, String email, String password, RegistrationPageViewModel registrationPageViewModel){
        HashMap<String, String> jsonObject = new HashMap<String, String>();
        jsonObject.put("login", login);
        jsonObject.put("email", email);
        jsonObject.put("password", password);
        jsonObject.put("roleId", "2");

        Call<ServerResponse> call = loginApiServer.registerUser(jsonObject);
        try {
            call.enqueue(new Callback<ServerResponse>() {
                @Override
                public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                    System.out.println("registerUser " + response.code());
                    if (response.code() == 201) {
                        registrationPageViewModel.setRegisterStatusMessage(response.body().getMessage());
                        registrationPageViewModel.setRegistrationStatus(true);
                    } else {
                        registrationPageViewModel.setRegisterStatusMessage(response.body().getMessage());
                        registrationPageViewModel.setRegistrationStatus(false);
                    }
                }
                @Override
                public void onFailure(Call<ServerResponse> call, Throwable t) {
                    registrationPageViewModel.setRegistrationStatus(false);
                    System.out.println("registerUser fail" + t.getMessage());
                    registrationPageViewModel.setConnectionErrorAlert();
                }
            });
        } catch (Exception exception) {
            throw exception;
        }
    }
}
