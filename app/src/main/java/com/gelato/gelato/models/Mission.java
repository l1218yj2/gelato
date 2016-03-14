package com.gelato.gelato.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by YongJae on 2016-01-27.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Mission implements Serializable {
    // TODO: implement parent_profile
    int MissionId;
    String missionName;

    public String getMissionName() {
        return missionName;
    }

    @Override
    public String toString() {
        return missionName;
    }

    public int getMissionId() {
        return MissionId;
    }

    public void setMissionId(int missionId) {
        MissionId = missionId;
    }
}
