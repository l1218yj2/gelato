package com.gelato.gelato.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by YJLaptop on 2016-02-17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChannelAdmin {
   Boolean isAdmin;

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
}
