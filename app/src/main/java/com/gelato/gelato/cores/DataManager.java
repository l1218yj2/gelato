package com.gelato.gelato.cores;

import com.gelato.gelato.models.services.ChannelService;
import com.gelato.gelato.models.services.MissionItemService;
import com.gelato.gelato.models.services.MissionService;
import com.gelato.gelato.models.services.UserService;

/**
 * Created by YongJae on 2016-02-10.
 */
public class DataManager {
    UserService userService = AppController.getInstance().getRetrofit().create(UserService.class);
    ChannelService channelService = AppController.getInstance().getRetrofit().create(ChannelService.class);
    MissionItemService missionItemService = AppController.getInstance().getRetrofit().create(MissionItemService.class);
    MissionService missionService = AppController.getInstance().getRetrofit().create(MissionService.class);

    public UserService getUserService() {
        return userService;
    }

    public ChannelService getChannelService() {
        return channelService;
    }

    public MissionItemService getMissionItemService() {
        return missionItemService;
    }

    public MissionService getMissionService() {
        return missionService;
    }
}
