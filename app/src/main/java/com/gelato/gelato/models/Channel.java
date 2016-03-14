package com.gelato.gelato.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.joda.time.DateTime;

import java.io.Serializable;

/**
 * Created by YongJae on 2016-01-27.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Channel implements Serializable {
    // TODO: implement parent_profile
    int channelId;
    String channelName;
    DateTime expiryAt;

    public DateTime getExpiryAt() {
        return expiryAt;
    }

    public void setExpiryAt(DateTime expiryAt) {
        this.expiryAt = expiryAt;
    }

    public void setChannelName(String title) {
        this.channelName = title;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public int getChannelId() {
        return channelId;
    }

    public String getChannelName() {
        return channelName;
    }
}
