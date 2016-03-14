package com.gelato.gelato.cores.gcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.NotificationCompat;

import com.gelato.gelato.R;
import com.gelato.gelato.cores.AppController;
import com.gelato.gelato.cores.LoginManager;
import com.gelato.gelato.cores.network.DefaultListener;
import com.gelato.gelato.models.CustomNotification;

public class NotificationReceiver extends BroadcastReceiver {
    public NotificationReceiver() {
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        final CustomNotification customNotification = (CustomNotification) intent.getSerializableExtra("notification");
        final LoginManager loginManager = AppController.getInstance().getLoginManager();
        if (loginManager.getUser() == null) {
            loginManager.loadUser(new DefaultListener() {
                @Override
                public void onSuccess() {
                    sendNotification(context, customNotification);
                }

                @Override
                public void onFailure() {
                }
            });
        }
    }


    private void sendNotification(Context context, CustomNotification notification) {

        Intent intent = notification.getIntent(context);
        intent.putExtra("channel_id", notification.getChannelId());

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(notification.getActivityClass());
        stackBuilder.addNextIntent(intent);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        Boolean vibration, sound;
        vibration = AppController.getInstance().getLocalStore().getVibration();
        sound = AppController.getInstance().getLocalStore().getSound();
        int defaultSetting = Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS;

        long[] vibarationTime = new long[]{100, 1000};

        if (!vibration) {
            vibarationTime = new long[]{0, 0};
        }
        if (!sound) {
            defaultSetting = Notification.DEFAULT_LIGHTS;
        }
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        int color = context.getResources().getColor(R.color.teal_500);
        if (Build.VERSION.SDK_INT < 16) {
            NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                    .setSmallIcon(notification.getSmallIcon())
                    .setContentTitle(notification.getTitle())
                    .setLargeIcon(notification.getIcon(context))
                    .setContentText(notification.getContent())
                    .setAutoCancel(true)
                    .setColor(notification.getColor(context))
                    .setSound(defaultSoundUri)
                    .setShowWhen(true)
                    .setContentIntent(pendingIntent)
                    .setDefaults(defaultSetting)
                    .setVibrate(vibarationTime);
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

        } else {

            Notification.Builder notificationBuilder = new Notification.Builder(context)
                    .setSmallIcon(notification.getSmallIcon())
                    .setLargeIcon(notification.getIcon(context))
                    .setContentTitle(notification.getTitle())
                    .setContentText(notification.getContent())
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)
                    .setDefaults(defaultSetting)
                    .setVibrate(vibarationTime);


            if (notification.getContent().length() > 19) {
                notificationBuilder
                        .setStyle(new Notification.BigTextStyle().bigText(notification.getContent()))
                        .setContentTitle(notification.getTitle());
            }
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                notificationBuilder.setColor(color);
            }


            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

        }
    }
}
