package com.gelato.gelato.models;

import android.content.Context;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gelato.gelato.R;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by YongJae on 2016-01-27.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdminMission implements Serializable {
    // TODO: implement parent_profile
    int channelMissionId;
    ArrayList<UserMission> userMission;
    String missionName;
    String missionInfo;
    DateTime expiryAt, createdAt;

    public int getChannelMissionId() {
        return channelMissionId;
    }

    public void setChannelMissionId(int channelMissionId) {
        this.channelMissionId = channelMissionId;
    }

    public int getColor(Context mContext){
            if(expiryAt.isBefore(DateTime.now())){
                return mContext.getResources().getColor(R.color.grey_500);
            }
        else{
                switch (channelMissionId%3){
                    case 0:
                        return mContext.getResources().getColor(R.color.mission_1);
                    case 1:
                        return mContext.getResources().getColor(R.color.mission_2);
                    case 3:
                        return mContext.getResources().getColor(R.color.mission_3);
                    default:
                        return mContext.getResources().getColor(R.color.mission_1);
                }
            }
    }

    public DateTime getCreatedAt() {
        return createdAt;
    }

    public DateTime getExpiryAt() {
        return expiryAt;
    }

    public void setCreatedAt(DateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setExpiryAt(DateTime expiryAt) {
        this.expiryAt = expiryAt;
    }

    public ArrayList<UserMission> getUserMission() {
        return userMission;
    }

    public void setUserMission(ArrayList<UserMission> userMission) {
        this.userMission = userMission;
    }

    public int getPercent() {
        if (userMission != null) {
            int i = 0;
            for (UserMission mUserMission : userMission) {
                if (mUserMission.done) {
                    i++;
                }
            }
            return i*100 / userMission.size();
        } else {
            return 0;
        }
    }

    public void setMissionInfo(String missionInfo) {
        this.missionInfo = missionInfo;
    }

    public void setMissionName(String missionName) {
        this.missionName = missionName;
    }

    public String getMissionInfo() {
        return missionInfo;
    }

    public String getMissionName() {
        return missionName;
    }

    public String getDateState() {
        return createdAt.toString("MM-dd") + " ~ " + expiryAt.toString("MM-dd");
    }

    public String getState() {
        if(expiryAt.isAfter(DateTime.now())){
            return "진행중인 미션";
        }
        else{
            return "과거의 미션";
        }
    }
}
