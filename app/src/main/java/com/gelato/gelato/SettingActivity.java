package com.gelato.gelato;

import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.widget.CompoundButton;

import com.gelato.gelato.cores.AppController;
import com.gelato.gelato.cores.CustomDrawerActivity;

public class SettingActivity extends CustomDrawerActivity{
    SwitchCompat switSound, switVibrate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        initializeToolbar(toolbar);
        switSound = (SwitchCompat) findViewById(R.id.switSound);
        switVibrate = (SwitchCompat) findViewById(R.id.switVibrate);
        switSound.setChecked(AppController.getInstance().getLocalStore().getSound());
        switVibrate.setChecked(AppController.getInstance().getLocalStore().getVibration());
        switSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AppController.getInstance().getLocalStore().setSound(isChecked);
            }
        });
        switVibrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AppController.getInstance().getLocalStore().setVibration(isChecked);
            }
        });

    }
}
