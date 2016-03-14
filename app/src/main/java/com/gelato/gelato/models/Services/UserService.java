package com.gelato.gelato.models.services;

import com.gelato.gelato.models.LoginToken;
import com.gelato.gelato.models.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by YongJae on 2016-02-10.
 */
public interface UserService {
    @GET("/user/me/")
    Call<User> getUser();

    @GET("/user/{channel_id}/manito/")
    Call<User> getManito(@Path("channel_id")int channel_id);

    @FormUrlEncoded
    @POST("/user/register/")
    Call<Boolean> register(@Field("facebook_id") String id, @Field("facebook_token") String token, @Field("name") String name, @Field("picture")String picture, @Field("gender")String gender);

    @FormUrlEncoded
    @POST("/user/device/")
    Call<Boolean> register_device(@Field("device_id") String device_id);

    @FormUrlEncoded
    @POST("/user/login/")
    Call<LoginToken> login(@Field("facebook_id") String id);
}