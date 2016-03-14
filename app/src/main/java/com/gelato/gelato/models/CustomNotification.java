package com.gelato.gelato.models;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gelato.gelato.ChannelActivity;
import com.gelato.gelato.R;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Mathpresso2 on 2015-08-25.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomNotification implements Serializable {

    public CustomNotification() {
    }

    public Class<?> getActivityClass() {
        return ChannelActivity.class;
    }

    public Bitmap getIcon(Context context) {
        Bitmap icon;

        icon = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.ic_launcher);

        return icon;
    }

    public Intent getIntent(Context context) {
        Intent intent;
        intent = new Intent(context, ChannelActivity.class);
        return intent;
    }

    public Bitmap getLogo(Context context) {
        return BitmapFactory.decodeResource(context.getResources(), R.drawable.notification_icon);
    }

    public int getSmallIcon() {

        return R.drawable.notification_icon;

    }

    public int getColor(Context context) {

        return context.getResources().getColor(R.color.light_blue_500);

    }

    public Integer channelId;

    public String title;

    public String content;
    public DateTime created_at;

    public DateTime getCreated_at() {
        return created_at;
    }


    public Integer getChannelId() {
        return channelId;
    }

    public void setCreated_at(DateTime created_at) {
        this.created_at = created_at;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public CustomNotification(Bundle data) {
        try {
            final JSONObject obj = new JSONObject(data.getString("param"));
            created_at = new DateTime(data.getString("created_at"));
            try {
                title = obj.getString("title");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                channelId = obj.getInt("channel_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                content = obj.getString("content");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public String getContent() {

        return this.content;
    }

    public String getTitle() {

        return title;
    }

}