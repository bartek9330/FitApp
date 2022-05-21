package com.fitwebappclient.loginandregistration.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.HashMap;

public class UserData  implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("rolesEntity")
    @Expose
    private HashMap<String,String> rolesEntity;


    @SerializedName("email")
    private String email;

    private RolesEntity userRoles = new RolesEntity();


    public HashMap<String, String> getRolesEntity() {
        return rolesEntity;
    }

    public RolesEntity getUserRoles() {
        return userRoles;
    }

    public void setRolesEntity(HashMap<String, String> rolesEntity) {
         this.rolesEntity = rolesEntity;
    }

    public void setUserRoles(){
        userRoles.setId(Integer.parseInt(rolesEntity.get("id")));
        userRoles.setName(rolesEntity.get("name"));
        userRoles.setDescription(rolesEntity.get("description"));
    }



    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @SerializedName("login")
    private String login;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
