package com.gelato.gelato.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by YJLaptop on 2016-02-17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FacebookPicture {
    FacebookPictureData data;

    public FacebookPictureData getData() {
        return data;
    }

    public void setData(FacebookPictureData data) {
        this.data = data;
    }
}
