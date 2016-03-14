package com.gelato.gelato.models.services;

import com.gelato.gelato.models.Channel;
import com.gelato.gelato.models.ChannelAdmin;
import com.gelato.gelato.models.ChannelInfo;
import com.gelato.gelato.models.Manito;
import com.gelato.gelato.models.User;

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
public interface ChannelService {
    @GET("/u_channel/{channel_id}/members/")
    Call<List<User>> getChannelUsers(@Path("channel_id")int channel_id);

    @GET("/u_channel/{channel_id}/admin_user/")
    Call<List<Manito>> getChannelAdminUsers(@Path("channel_id")int channel_id);

    @GET("/u_channel/")
    Call<List<Channel>> getChannels();

    @GET("/u_channel/{channel_id}/status/")
    Call<ChannelInfo> getChannelInfo(@Path("channel_id")int channel_id);

    @POST("/u_channel/{channel_id}/start/")
    Call<Boolean> ChannelStart(@Path("channel_id")int channel_id);

    @GET("/u_channel/{channel_id}/admin/")
    Call<ChannelAdmin> isAdmin(@Path("channel_id")int channel_id);

    @FormUrlEncoded
    @POST("/u_channel/code/")
    Call<Channel> joinChannel(@Field("code") String code);

    @FormUrlEncoded
    @POST("/u_channel/")
    Call<Integer> makeChannel(@Field("name") String name, @Field("expiry_date")DateTime expiry_date);
    //채널 id를 얻는다.

}