package com.gelato.gelato;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gelato.gelato.cores.AppController;
import com.gelato.gelato.cores.CustomDrawerActivity;
import com.gelato.gelato.cores.gcm.RegistrationIntentService;
import com.gelato.gelato.models.Channel;
import com.gelato.gelato.models.ChannelAdmin;
import com.gelato.gelato.models.User;
import com.gelato.gelato.tools.RegisteredFragmentStatePagerAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChannelActivity extends CustomDrawerActivity {

    //Defining Variables
    private ViewPager mPager;
    FragmentManager mFragmentManager;
    UserListAdapter mUserListAdapter;
    TabLayout tabs;
    ListView list_view;
    LinearLayout right_drawer;
    TextView txtvCountRight, txtvCrack;
    private int channelId;
    Toolbar toolbar;
    DrawerLayout drawer_layout;
    Channel mChannel;

    public int getChannelId() {
        return channelId;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.group, menu);
        menu.findItem(R.id.action_group).setOnMenuItemClickListener(
                new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_group:
                                drawer_layout.openDrawer(Gravity.RIGHT);
                                break;
                        }
                        return true;
                    }
                }
        );
        return true;
    }

    public Channel getChannel() {
        return mChannel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        channelId = getIntent().getIntExtra("channel_id", AppController.getInstance().getLocalStore().getChannels().get(0).getChannelId());
        //initializeChannelLayout();
        if(AppController.getInstance().getLocalStore().getChannelCreated(channelId)){
            initializeChannelLayout();
        }else{
            Intent intent = new Intent(ChannelActivity.this, WaitingRoomActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("channel_id", channelId);
            startActivity(intent);
            finish();
        }
    }

    int a = 0;

    private void initializeChannelLayout() {
        setContentView(R.layout.activity_channel);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        initializeToolbar(toolbar);
        registrationInstanceId();
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        txtvCountRight = (TextView) findViewById(R.id.txtvCountRight);
        list_view = (ListView) findViewById(R.id.list_view);
        txtvCrack = (TextView) findViewById(R.id.txtvCrack);
        txtvCrack.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (a > 5) {
                    setAdminFragments();
                }
                a++;
                return false;
            }
        });
        list_view.setDivider(null);
        right_drawer = (LinearLayout) findViewById(R.id.right_drawer);
        mPager = (ViewPager) findViewById(R.id.pager);

        tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setTabTextColors(getResources().getColorStateList(R.color.selector));
        for (Channel channel : AppController.getInstance().getLocalStore().getChannels()) {
            if (channel.getChannelId() == channelId) {
                mChannel = channel;
            }
        }
        if(mChannel!=null){
            if(mChannel.getExpiryAt().isAfterNow()) {
                toolbar.setTitle(mChannel.getChannelName());
                toolbar.setTitleTextColor(getResources().getColor(R.color.white));
            }else{
                toolbar.setTitle("만료된 "+ mChannel.getChannelName());
                toolbar.setTitleTextColor(getResources().getColor(R.color.grey_200));
            }
        }
        mFragmentManager = getSupportFragmentManager();

        mUserListAdapter = new UserListAdapter();
        AppController.getInstance().getDataManager().getChannelService().getChannelUsers(channelId).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccess()) {
                    for (User user : response.body()) {
                        mUserListAdapter.add(user);

                    }
                    txtvCountRight.setText(String.format("%d명", response.body().size()));
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });
        list_view.setAdapter(mUserListAdapter);
        // Initialize pager

        // for GA
        ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int arg0) {

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case ALL:
                        break;
                    case TO_ME:
                        break;
                    case TO_YOU:
                    default:
                        break;
                }
            }
        };

        mPager.setOnPageChangeListener(pageChangeListener);
        pageChangeListener.onPageSelected(0);

        // Sliding Tab Layout

        // setup
        setFragment();

        registrationInstanceId();
    }

    private void setFragment() {
        if(mChannel.getExpiryAt().isAfterNow()) {

            AppController.getInstance().getDataManager().getChannelService().isAdmin(channelId).enqueue(new Callback<ChannelAdmin>() {
                @Override
                public void onResponse(Call<ChannelAdmin> call, Response<ChannelAdmin> response) {
                    if (response.isSuccess()) {
                        if (response.body().getIsAdmin()) {
                            setAdminFragments();
                        } else {
                            setUserFragments();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ChannelAdmin> call, Throwable t) {

                }
            });
        }else{
            setAdminFragments();
        }
    }

    private void setUserFragments() {
        UserPagerAdapter mAdapter = new UserPagerAdapter(mFragmentManager);
        mPager.setAdapter(mAdapter);
        mPager.setOffscreenPageLimit(mPager.getAdapter().getCount());
        tabs.setupWithViewPager(mPager);
        tabs.setTabMode(TabLayout.MODE_FIXED);
    }

    private void setAdminFragments() {
        AdminAdatper mAdapter = new AdminAdatper(mFragmentManager);
        mPager.setAdapter(mAdapter);
        mPager.setOffscreenPageLimit(mPager.getAdapter().getCount());
        tabs.setupWithViewPager(mPager);
        tabs.setTabMode(TabLayout.MODE_FIXED);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {

        }
    }


    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
//                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
//                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                finish();
            }
            return false;
        }
        return true;
    }

    public void registrationInstanceId() {
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
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

    public static final int UPLOAD_MISSION = 1123;
    public static final int MAKE_MISSION = 1124;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK){
            switch (requestCode){
                case UPLOAD_MISSION:
                    refresh();
                    break;
                case MAKE_MISSION:
                    refresh();
                    break;
                default:
                    break;
            }
        }
    }

    private void refresh() {
        if(mPager.getAdapter() instanceof UserPagerAdapter) {
            ((UserPagerAdapter) mPager.getAdapter()).getTimeLineFragment().refresh();
            ((UserPagerAdapter) mPager.getAdapter()).getToMeFragment().refresh();
            ((UserPagerAdapter) mPager.getAdapter()).getToYouFragment().refresh();
        }else if(mPager.getAdapter() instanceof AdminAdatper){
            ((AdminAdatper) mPager.getAdapter()).getAdminTimeLineFragment().refresh();
            ((AdminAdatper) mPager.getAdapter()).getManitoFragment().refresh();
            ((AdminAdatper) mPager.getAdapter()).getStateFragment().refresh();
        }
    }

    /*
         * Fragments
         */
    public final int ALL = 0, TO_ME = 1, TO_YOU = 2;

    public class UserPagerAdapter extends RegisteredFragmentStatePagerAdapter {
        private final int NUM_ITEMS = 3;

        public UserPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case ALL:
                    return new TimeLineFragment();
                case TO_ME:
                    return new ToMeFragment();
                case TO_YOU:
                    return new ToYouFragment();
                default:
                    throw new RuntimeException("Out of bounds.");
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position % NUM_ITEMS) {
                case ALL:
                    return "전체보기";
                case TO_ME:
                    return "받은쪽지";
                case TO_YOU:
                    return "보낸쪽지";
                default:
                    return "";
            }
        }

        public TimeLineFragment getTimeLineFragment() {
            return (TimeLineFragment) getRegisteredFragment(ALL);
        }

        public ToMeFragment getToMeFragment() {
            return (ToMeFragment) getRegisteredFragment(TO_ME);
        }

        public ToYouFragment getToYouFragment() {
            return (ToYouFragment) getRegisteredFragment(TO_YOU);
        }
    }

    public class AdminAdatper extends RegisteredFragmentStatePagerAdapter {
        private final int NUM_ITEMS = 3;

        public AdminAdatper(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case ALL:
                    return new AdminTimeLineFragment();
                case TO_ME:
                    return new AdminMissionFragment();
                case TO_YOU:
                    return new ManitoFragment();
                default:
                    throw new RuntimeException("Out of bounds.");
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position % NUM_ITEMS) {
                case ALL:
                    return "전체보기";
                case TO_ME:
                    return "미션현황";
                case TO_YOU:
                    return "참여현황";
                default:
                    return "";
            }
        }

        public AdminTimeLineFragment getAdminTimeLineFragment() {
            return (AdminTimeLineFragment) getRegisteredFragment(ALL);
        }

        public AdminMissionFragment getStateFragment() {
            return (AdminMissionFragment) getRegisteredFragment(TO_ME);
        }

        public ManitoFragment getManitoFragment() {
            return (ManitoFragment) getRegisteredFragment(TO_YOU);
        }
    }
}
