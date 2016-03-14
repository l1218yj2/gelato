package com.gelato.gelato.cores;

import android.content.Context;
import android.content.SharedPreferences;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.gelato.gelato.models.Channel;
import com.gelato.gelato.models.User;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Mathpresso2 on 2015-07-22.
 */


public class LocalStore {

    public static final String SP_NAME = "LocalData";
    private SharedPreferences LocalDatabase;
    private int loadingDelay;
    private boolean didSubmitIDCard;

    public LocalStore(Context context) {
        LocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }

    public void storeLoginToken(String loginToken) {
        SharedPreferences.Editor spEditor = LocalDatabase.edit();
        spEditor.putString("login_token", loginToken);
        spEditor.commit();
    }

    public void storeUser(User user) {
        SharedPreferences.Editor spEditor = LocalDatabase.edit();
        ObjectMapper mapper = new ObjectMapper();
        try {
            spEditor.putString("user", mapper.writeValueAsString(user));
            spEditor.commit();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void setBaseUrl(String baseUrl) {
        SharedPreferences.Editor spEditor = LocalDatabase.edit();
        spEditor.putString("base_url", baseUrl);
        spEditor.commit();
    }

    public void setChannelCreated(int channel, Boolean created) {
        SharedPreferences.Editor spEditor = LocalDatabase.edit();
        spEditor.putBoolean(String.format("channel_%d", channel), created);
        spEditor.commit();
    }

    public Boolean getChannelCreated(int channel) {

        return LocalDatabase.getBoolean(String.format("channel_%d", channel), false);
    }


    public String getLoginToken() {
        return LocalDatabase.getString("login_token", null);
    }

    public User getUser() {
        ObjectMapper mapper = new ObjectMapper();

        User user = null;
        String json = LocalDatabase.getString("user", null);
        if (json != null && !json.isEmpty()) {
            try {
                mapper.registerModule(new JodaModule());
                user = mapper.readValue(json, User.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return user;
    }

    public void clearLoginData() {
        SharedPreferences.Editor spEditor = LocalDatabase.edit();
        spEditor.remove("login_token");
        spEditor.remove("user");
        spEditor.commit();
    }

    public String getBaseUrl() {
        return LocalDatabase.getString("base_url", "http://kohm.pe.kr");
    }

    public ArrayList<Channel> getChannels() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
        // http://stackoverflow.com/questions/10519265/jackson-overcoming-underscores-in-favor-of-camel-case
        mapper.setPropertyNamingStrategy(
                PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        ArrayList<Channel> channels = new ArrayList<>();
        String json = LocalDatabase.getString("channels", null);
        if (json != null && !json.isEmpty()) {
            try {
                channels = mapper.readValue(json,
                        new TypeReference<ArrayList<Channel>>() {
                        });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return channels;
    }

    public void storeChannel(ArrayList<Channel> channels) {
        SharedPreferences.Editor spEditor = LocalDatabase.edit();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
        // http://stackoverflow.com/questions/10519265/jackson-overcoming-underscores-in-favor-of-camel-case
        mapper.setPropertyNamingStrategy(
                PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        try {
            spEditor.putString("channels", mapper.writeValueAsString(channels));
            spEditor.commit();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void setVibration(boolean isChecked) {
        SharedPreferences.Editor spEditor = LocalDatabase.edit();
        spEditor.putBoolean("vibration", isChecked);
        spEditor.commit();

    }

    public void setSound(boolean isChecked) {
        SharedPreferences.Editor spEditor = LocalDatabase.edit();
        spEditor.putBoolean("sound", isChecked);
        spEditor.commit();

    }

    public boolean getVibration() {
        return LocalDatabase.getBoolean("sound", true);

    }


    public boolean getSound() {
        return LocalDatabase.getBoolean("vibration", true);

    }
}
