package com.gelato.gelato.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by YongJae on 2016-01-27.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MemberMission implements Serializable {
    // TODO: implement parent_profile
    int memberMissionId;
    String missionName;
    String missionInfo;

    public void setMemberMissionId(int memberMissionId) {
        this.memberMissionId = memberMissionId;
    }

    public void setMissionInfo(String missionInfo) {
        this.missionInfo = missionInfo;
    }

    public void setMissionName(String missionName) {
        this.missionName = missionName;
    }

    public int getMemberMissionId() {
        return memberMissionId;
    }

    public String getMissionInfo() {
        return missionInfo;
    }

    public String getMissionName() {
        return missionName;
    }
}
