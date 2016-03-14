package com.gelato.gelato.cores;

import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by Mathpresso2 on 2015-12-22.
 */
public class CustomFragment extends Fragment implements View.OnClickListener {

    protected void sendClickEvent(View v) {

    }

    protected void sendClickEvent(String viewName, String extra) {

    }

    @Override
    public void onClick(View v) {
        sendClickEvent(v);
    }
}
