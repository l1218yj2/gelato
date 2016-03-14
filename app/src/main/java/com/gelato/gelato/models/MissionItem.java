package com.gelato.gelato.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.joda.time.DateTime;

import java.io.Serializable;

/**
 * Created by YongJae on 2016-01-27.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MissionItem implements Serializable {
    // TODO: implement parent_profile
    int timelineContentId;
    String timelineContent;
    String timelinePictureUrl;
    String giverNickname, receiverName;
    String timelineContentTitle;
    String giverImgUrl;
    DateTime regTime;

    public void setGiverImgUrl(String giverImgUrl) {
        this.giverImgUrl = giverImgUrl;
    }

    public String getGiverImgUrl() {
        return giverImgUrl;
    }

    public void setGiverNickname(String giverNickname) {
        this.giverNickname = giverNickname;
    }

    public void setTimelineContentTitle(String timelineContentTitle) {
        this.timelineContentTitle = timelineContentTitle;
    }

    public void setRegTime(DateTime regTime) {
        this.regTime = regTime;
    }

    public DateTime getRegTime() {
        return regTime;
    }

    public String getGiverNickname() {
        return giverNickname;
    }

    public String getTimelineContentTitle() {
        return timelineContentTitle;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public int getTimelineContentId() {
        return timelineContentId;
    }

    public String getTimelineContent() {
        return timelineContent;
    }

    public String getTimelinePictureUrl() {
        return timelinePictureUrl;
    }

    public void setTimelineContentId(int timelineContentId) {
        this.timelineContentId = timelineContentId;
    }

    public void setTimelineContent(String timelineContent) {
        this.timelineContent = timelineContent;
    }

    public void setTimelinePictureUrl(String timelinePictureUrl) {
        this.timelinePictureUrl = timelinePictureUrl;
    }

}
