package com.gelato.gelato.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by YongJae on 2016-01-27.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginToken {
    String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
