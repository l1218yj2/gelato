package com.gelato.gelato.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by Mathpresso2 on 2015-07-22.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements Serializable {
    // TODO: implement parent_profile

    public String userName;
    public String userProfileUrl;

    public String getUserName() {
        return userName;
    }

    public String getUserProfileUrl() {
        return userProfileUrl;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserProfileUrl(String userProfileUrl) {
        this.userProfileUrl = userProfileUrl;
    }
}
