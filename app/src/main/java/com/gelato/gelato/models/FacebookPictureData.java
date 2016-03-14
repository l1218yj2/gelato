package com.gelato.gelato.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by YJLaptop on 2016-02-17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FacebookPictureData {

    String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
