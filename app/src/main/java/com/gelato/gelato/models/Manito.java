package com.gelato.gelato.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by YongJae on 2016-02-21.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Manito implements Serializable {
    public String getGiverUserName() {
        return giverUserName;
    }

    public void setGiverUserName(String giverUserName) {
        this.giverUserName = giverUserName;
    }

    public String getGiverUserProfileUrl() {
        return giverUserProfileUrl;
    }

    public void setGiverUserProfileUrl(String giverUserProfileUrl) {
        this.giverUserProfileUrl = giverUserProfileUrl;
    }

    public String getReceiverUserName() {
        return receiverUserName;
    }

    public void setReceiverUserName(String receiverUserName) {
        this.receiverUserName = receiverUserName;
    }

    public String getReceiverUserProfileUrl() {
        return receiverUserProfileUrl;
    }

    public void setReceiverUserProfileUrl(String receiverUserProfileUrl) {
        this.receiverUserProfileUrl = receiverUserProfileUrl;
    }

    public String getFinishStatus() {
        return finishStatus;
    }

    public void setFinishStatus(String finishStatus) {
        this.finishStatus = finishStatus;
    }

    // TODO: implement parent_profile
    String giverUserName, giverUserProfileUrl, receiverUserName, receiverUserProfileUrl, finishStatus;



}
