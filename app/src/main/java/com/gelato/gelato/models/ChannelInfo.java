package com.gelato.gelato.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by YongJae on 2016-01-27.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChannelInfo implements Serializable {

    //사실 isStarted가 더 적당함
    Boolean isCreated;

    ArrayList<User> userList;

    String code;

    public ArrayList<User> getUserList() {
        return userList;
    }

    public Boolean getIsCreated() {
        return isCreated;
    }

    DateTime expiryAt;

    public DateTime getExpiryAt() {
        return expiryAt;
    }

    public void setExpiryAt(DateTime expiryAt) {
        this.expiryAt = expiryAt;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setIsCreated(Boolean isCreated) {
        this.isCreated = isCreated;
    }

    public void setUserList(ArrayList<User> userList) {
        this.userList = userList;
    }
}
