package com.gelato.gelato;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.gelato.gelato.cores.CustomAppCompatActivity;

public class WelcomeActivity extends CustomAppCompatActivity {

    LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        container = (LinearLayout) findViewById(R.id.container);
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, CreateChannelorInputCodeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}