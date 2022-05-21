package com.fitwebappclient.adminmain.models;

public enum ServerResponseMessages {
    OK("Course has been added"),
    EXIST("Course exist");
    private String response;

    ServerResponseMessages(String responseMessage){
        this.response = responseMessage;
    }

    public String getResponse() {
        return response;
    }
}
