package com.gelato.gelato;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import com.gelato.gelato.cores.AppController;
import com.gelato.gelato.cores.network.DefaultListener;
import com.gelato.gelato.models.Channel;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_flash);


        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.gelato.gelato",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        if (AppController.getInstance().getLoginManager().isLoggedIn()) {
            AppController.getInstance().getLoginManager().loadUser(new DefaultListener() {
                @Override
                public void onSuccess() {
                    loadChannel();
                    activity.finish();
                }

                @Override
                public void onFailure() {
                    AppController.getInstance().getLoginManager().clear();
                    moveToLoginActivity();
                    activity.finish();
                }
            });
        } else {
            moveToLoginActivity();
        }
    }

    private void moveToLoginActivity() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        activity.finish();
    }

    private void loadChannel() {
        Call<List<Channel>> channelCall = AppController.getInstance().getDataManager().getChannelService().getChannels();
        channelCall.enqueue(new Callback<List<Channel>>() {
            @Override
            public void onResponse(Call<List<Channel>> call, Response<List<Channel>> response) {
                if (response.isSuccess()) {
                    if(response.body().size()==0)
                    {
                        Intent intent = new Intent(SplashActivity.this, CreateChannelorInputCodeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        activity.finish();
                    }
                    ArrayList<Channel> channels = new ArrayList<Channel>(response.body());
                    AppController.getInstance().getLocalStore().storeChannel(channels);
                    Intent intent = new Intent(getApplicationContext(), ChannelActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    activity.finish();
                } else {
                    Intent intent = new Intent(getApplicationContext(), ChannelActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    activity.finish();
                }
            }

            @Override
            public void onFailure(Call<List<Channel>> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
