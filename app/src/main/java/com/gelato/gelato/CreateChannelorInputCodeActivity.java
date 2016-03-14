package com.gelato.gelato;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gelato.gelato.cores.AppController;
import com.gelato.gelato.cores.CustomAppCompatActivity;
import com.gelato.gelato.models.Channel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by taebinkim on 2016. 2. 20..
 */
public class CreateChannelorInputCodeActivity extends CustomAppCompatActivity {
    Toolbar toolbar;

    EditText etxtCode;
    TextView txtvCreate;
    AppCompatButton btnCode;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_or_code);
        etxtCode = (EditText) findViewById(R.id.etxtCode);
        btnCode = (AppCompatButton) findViewById(R.id.btnCode);
        txtvCreate = (TextView) findViewById( R.id.txtvCreate);
        btnCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             if(etxtCode.getText()!=null){
                 AppController.getInstance().getDataManager().getChannelService().joinChannel(etxtCode.getText().toString()).enqueue(new Callback<Channel>() {
                     @Override
                     public void onResponse(Call<Channel> call, Response<Channel> response) {
                         if(response.isSuccess()){
                             ArrayList<Channel> channels =  AppController.getInstance().getLocalStore().getChannels();
                             channels.add(response.body());
                             AppController.getInstance().getLocalStore().storeChannel(channels);
                             Intent intent = new Intent(CreateChannelorInputCodeActivity.this, ChannelActivity.class);
                             intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                             intent.putExtra("channel_id",response.body().getChannelId());
                             startActivity(intent);
                         }else{
                             Toast.makeText(CreateChannelorInputCodeActivity.this, "잘못 입력하셨거나 해당 채널이 이미 시작하였습니다.", Toast.LENGTH_LONG).show();
                         }
                     }

                     @Override
                     public void onFailure(Call<Channel> call, Throwable t) {

                     }
                 });
             }
            }
        });
        txtvCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateChannelorInputCodeActivity.this, CreateChannelActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
}
