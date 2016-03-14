package com.gelato.gelato.models.services;

import com.gelato.gelato.models.AdminMission;
import com.gelato.gelato.models.MemberMission;
import com.gelato.gelato.models.Mission;

import org.joda.time.DateTime;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by YongJae on 2016-02-10.
 */
public interface MissionService {
    @GET("/u_channel/{id}/mission/")
    Call<List<MemberMission>> getMission(@Path("id") int channel_id);

    @GET("/u_channel/{id}/admin_mission/")
    Call<List<AdminMission>> getAdminMission(@Path("id") int channel_id);

    @GET("/u_channel/mission_list/")
    Call<List<Mission>> getAllMission();

    @FormUrlEncoded
    @POST("/u_channel/{id}/admin_mission/")
    Call<Boolean> postMission(@Path("id") int channel_id, @Field("mission_id") int id, @Field("description")String description, @Field("expiry_date")DateTime dateTime);
}