package com.fitwebappclient.loginandregistration.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RolesEntity implements Serializable {
    @SerializedName("id")
    private int id;

    @SerializedName("login")
    private String name;

    @SerializedName("description")
    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
