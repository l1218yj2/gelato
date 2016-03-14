package com.gelato.gelato.models.services;

import com.gelato.gelato.models.MissionItem;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by YongJae on 2016-02-10.
 */
public interface MissionItemService {
    @GET("/u_channel/{id}/all_mission/")
    Call<List<MissionItem>> getTimeLineMissionItems(@Path("id") int channel_id);

    @GET("/u_channel/{id}/all_mission/admin/")
    Call<List<MissionItem>> getAdminTimeLineMissionItems(@Path("id") int channel_id);

    @GET("/u_channel/{id}/from_mission/")
    Call<List<MissionItem>> getToMeMissionItems(@Path("id") int channel_id);

    @GET("/u_channel/{id}/to_mission/")
    Call<List<MissionItem>> getToYouMissionItems(@Path("id") int channel_id);

    @Multipart
    @POST("/u_channel/mission/{member_mission_id}/")
    Call<Boolean> postMissionItem(@Path("member_mission_id") int member_mission_id, @Part("image\"; filename=\"image.png\" ") RequestBody image, @Part("text") RequestBody text);

    @Multipart
    @POST("/u_channel/mission/{member_mission_id}/")
    Call<Boolean> postMissionItem(@Path("member_mission_id") int member_mission_id, @Part("text") RequestBody text);
}