package com.gelato.gelato.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by Mathpresso2 on 2015-07-22.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SampleUser implements Serializable {
    // TODO: implement parent_profile
    public int userId;
    public String name;
    public String address;

    public int getUserId() {
        return userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

}
