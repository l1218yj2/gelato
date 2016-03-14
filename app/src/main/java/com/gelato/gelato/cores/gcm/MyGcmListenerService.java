package com.gelato.gelato.cores.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.gelato.gelato.models.CustomNotification;
import com.google.android.gms.gcm.GcmListenerService;

import org.joda.time.DateTime;

/**
 * Created by Mathpresso2 on 2015-08-24.
 */
public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";
    /**
     *
     * @param from SenderID 값을 받아온다.
     * @param data Set형태로 GCM으로 받은 데이터 payload이다.
     */
    @Override
    public void onMessageReceived(String from, Bundle data) {

        data.putString("created_at", DateTime.now().toString());

        CustomNotification customNotification = new CustomNotification(data);

//        Intent intent = new Intent("ACTION_NOTIFICATION");
//        intent.putExtra("notification", customNotification);

        sendNotification(customNotification);

//        sendOrderedBroadcast(intent, null, null, null, Activity.RESULT_OK, null, null);
    }

    private void sendNotification(CustomNotification customNotification) {
        Intent intent = new Intent(this, customNotification.getActivityClass());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("channel_id", customNotification.getChannelId());
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(customNotification.getSmallIcon())
                .setContentTitle(customNotification.getTitle())
                .setContentText(customNotification.getContent())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}