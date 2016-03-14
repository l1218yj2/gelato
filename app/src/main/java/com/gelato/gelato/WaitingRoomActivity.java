package com.gelato.gelato;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gelato.gelato.cores.AppController;
import com.gelato.gelato.cores.CustomDrawerActivity;
import com.gelato.gelato.models.Channel;
import com.gelato.gelato.models.ChannelAdmin;
import com.gelato.gelato.models.ChannelInfo;
import com.gelato.gelato.models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.util.KakaoParameterException;

/**
 * Created by taebinkim on 2016. 2. 20..
 */
public class WaitingRoomActivity extends CustomDrawerActivity {

    UserListAdapter mUserListAdapter;
    Boolean admin = false;
    ListView list_view;
    LinearLayout right_drawer;
    TextView txtvCount, txtvCode, txtvTitle, txtvCountRight, txtvState;
    private int channelId;
    Toolbar toolbar;
    DrawerLayout drawer_layout;
    Channel mChannel;
    //kakao
    private KakaoLink kakaoLink;
    private KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder;
    MenuItem daum;
    Context m_context;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.daum, menu);
        daum = menu.findItem(R.id.daum);
        daum.setOnMenuItemClickListener(
                new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.daum:
                                makeChannel();
                                break;
                        }
                        return true;
                    }
                }
        );
        if(isadmin){
            daum.setVisible(true);
        }
        return true;
    }

    private void makeChannel() {
        if(admin) {
            AppController.getInstance().getDataManager().getChannelService().ChannelStart(channelId).enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if (response.isSuccess()) {
                        AppController.getInstance().getLocalStore().setChannelCreated(channelId, true);
                        Intent intent = new Intent(WaitingRoomActivity.this, ChannelActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("channel_id", channelId);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(WaitingRoomActivity.this,  "인원이 4명 이상일 때 시작할수 있습니다.",  Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {

                }
            });
        }else{
            Toast.makeText(WaitingRoomActivity.this,  "관리자가 아닙니다.",  Toast.LENGTH_LONG).show();
        }

    }

    Boolean isadmin=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        txtvCode = (TextView) findViewById(R.id.txtvCode);
        txtvState = (TextView) findViewById(R.id.txtvState);
        txtvTitle = (TextView) findViewById(R.id.txtvTitle);

        m_context = this;
        //Kakao
        try {
            kakaoLink = KakaoLink.getKakaoLink(m_context);
            kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
        } catch (KakaoParameterException e) {
            //alert(e.getMessage());
            Log.e("arctic",e.getMessage().toString());

        }

        //kakaoTalkLinkMessageBuilder.addAppLink("자세히 보기", new AppActionBuilder().setUrl("your-website url").build());

        channelId = getIntent().getIntExtra("channel_id", AppController.getInstance().getLocalStore().getChannels().get(0).getChannelId());
        initializeToolbar(toolbar);

        Log.d("WaitingChannelId", channelId + "");

        AppController.getInstance().getDataManager().getChannelService().isAdmin(channelId).enqueue(new Callback<ChannelAdmin>() {
            @Override
            public void onResponse(Call<ChannelAdmin> call, Response<ChannelAdmin> response) {
                if (response.isSuccess()) {
                    if (response.body().getIsAdmin()) {
                        admin = true;
                        if(daum!=null) {
                            daum.setVisible(true);
                        }else{
                            isadmin=true;
                        }
                    } else {
                        admin = false;
                    }
                }
            }

            @Override
            public void onFailure(Call<ChannelAdmin> call, Throwable t) {

            }
        });

        mUserListAdapter = new UserListAdapter();
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        txtvCount = (TextView) findViewById(R.id.txtvCount);
        txtvCountRight = (TextView) findViewById(R.id.txtvCountRight);
        list_view = (ListView) findViewById(R.id.list_view);
        list_view.setDivider(null);
        right_drawer = (LinearLayout) findViewById(R.id.right_drawer);
        for (Channel channel : AppController.getInstance().getLocalStore().getChannels()) {
            if (channel.getChannelId() == channelId) {
                mChannel = channel;
            }
        }
        if (mChannel != null) {
            toolbar.setTitle(mChannel.getChannelName());
            toolbar.setTitleTextColor(getResources().getColor(R.color.white));
            txtvTitle.setText(mChannel.getChannelName()+" 채널을\n개설하셨습니다.");
        }
        AppController.getInstance().getDataManager().getChannelService().getChannelInfo(channelId).enqueue(new Callback<ChannelInfo>() {
            @Override
            public void onResponse(Call<ChannelInfo> call, final Response<ChannelInfo> response) {
                if(response.isSuccess()) {
                    if(response.body().getIsCreated()){
                        Intent intent = new Intent(WaitingRoomActivity.this, ChannelActivity.class);
                        AppController.getInstance().getLocalStore().setChannelCreated(channelId, true);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("channel_id", channelId);
                        startActivity(intent);
                        finish();
                    }
                    txtvState.setText(String.format("*초청코드는 %s 전까지(24시간) 유효하며,\n이후에는 채널개설을 다시 진행해야 합니다.", response.body().getExpiryAt().toString("MM월 dd일 HH시")));
                    txtvCode.setText(response.body().getCode());
                    for (User user : response.body().getUserList()) {
                        mUserListAdapter.add(user);
                    }
                    txtvCount.setText(String.format("%d명", response.body().getUserList().size()));
                    txtvCountRight.setText(String.format("%d명", response.body().getUserList().size()));
                    txtvCount.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openRight();
                        }
                    });
                    txtvCode.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try{
                                kakaoTalkLinkMessageBuilder.addText("너와 나, 우리들의 소중한 이야기 마니또 어플리케이션 Arctic 으로 당신을 초대합니다.\n" +
                                        "\n" +
                                        "채널 초청코드: "+response.body().getCode())
                                        .addImage("http://d39er8gh0y1xkv.cloudfront.net/kakao/invite_img.png", 300, 200)
                                        .addAppButton("앱으로 이동")
                                        .build();
                                kakaoLink.sendMessage(kakaoTalkLinkMessageBuilder, m_context);
                            } catch (KakaoParameterException e){
                                Log.e("arctic",e.getMessage().toString());
                            }

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<ChannelInfo> call, Throwable t) {
                Log.d("asd","asdsa");
            }
        });


        list_view.setAdapter(mUserListAdapter);
    }

    @Override
    protected boolean isDrawerOpen() {
        return (getDrawerLayout().isDrawerOpen(Gravity.LEFT)||getDrawerLayout().isDrawerOpen(Gravity.RIGHT));
    }

    @Override
    protected void closeDrawer() {
        if(getDrawerLayout().isDrawerOpen(Gravity.RIGHT)){
            getDrawerLayout().closeDrawer(Gravity.RIGHT);
        }
        if(getDrawerLayout().isDrawerOpen(Gravity.LEFT)){
            getDrawerLayout().closeDrawer(Gravity.LEFT);
        }
    }

    @Override
    public void onBackPressed() {
        if (isDrawerOpen()) {
            closeDrawer();
        } else if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("종료")
                    .setMessage("정말로 Arctic을 종료할까요?")
                    .setNegativeButton("아니요", null)
                    .setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            finish();
                        }
                    })
                    .show();
        } else {
            super.onBackPressed();
        }
    }
}
