package com.gelato.gelato.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by Mathpresso2 on 2015-07-22.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserMission implements Serializable {
    // TODO: implement parent_profile
    public String userName;
    public String userProfileUrl;
    boolean done;

    public boolean isDone() {
        return done;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserProfileUrl() {
        return userProfileUrl;
    }

    public void setUserProfileUrl(String userProfileUrl) {
        this.userProfileUrl = userProfileUrl;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
