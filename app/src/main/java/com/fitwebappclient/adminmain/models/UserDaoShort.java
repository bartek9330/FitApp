package com.fitwebappclient.adminmain.models;

import com.google.gson.annotations.SerializedName;

public class UserDaoShort {
    @SerializedName("id")
    private int id;

    @SerializedName("roleId")
    private int roleId;

    @SerializedName("login")
    private String login;

    @SerializedName("email")
    private String email;

    private String password;

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
