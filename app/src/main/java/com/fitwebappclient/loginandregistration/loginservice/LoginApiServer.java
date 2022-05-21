package com.fitwebappclient.loginandregistration.loginservice;

import com.fitwebappclient.loginandregistration.models.ServerResponse;
import com.fitwebappclient.loginandregistration.models.TokenJWN;
import com.fitwebappclient.loginandregistration.models.UserData;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface LoginApiServer {
    @POST("authenticate")
    Call<TokenJWN> getToken(@Body HashMap<String,String> body);

    @POST("getUserByLogin")
    Call<UserData> getUserByLogin(@Body HashMap<String,String> body, @Header("Authorization") String jwtToken);

    @POST("registerUser")
    Call<ServerResponse> registerUser(@Body HashMap<String,String> body);
}
