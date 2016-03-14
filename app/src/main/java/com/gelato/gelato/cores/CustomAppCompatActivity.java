package com.gelato.gelato.cores;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by YongJae on 2016-01-27.
 */
public class CustomAppCompatActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        try {
            String label = getResources().getResourceEntryName(v.getId());
            AppController.getInstance().sendEvent(this.getClass().getSimpleName(),"click" ,label);
        }catch (Exception e){

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppController.getInstance().sendScreenChange(this.getClass().getSimpleName());
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}