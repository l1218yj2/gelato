package com.gelato.gelato;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.gelato.gelato.cores.CustomAppCompatActivity;

public class RegisterActivity extends CustomAppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    }
}
