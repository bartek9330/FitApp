package com.fitwebappclient.adminmain.models;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class StepDao  implements Serializable {
    @SerializedName("name")
    private String name;

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    @SerializedName("courseId")
    private int courseId;


    @SerializedName("description")
    private String description;

    @SerializedName("image")
    private String image;

    public int getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;
    }

    @SerializedName("stepNumber")
    private int stepNumber;


    public int getStepId() {
        return stepId;
    }

    public void setStepId(int stepId) {
        this.stepId = stepId;
    }

    @SerializedName("id")
    private int stepId;

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
