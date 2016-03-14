package com.gelato.gelato.cores;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gelato.gelato.ChannelActivity;
import com.gelato.gelato.CreateChannelorInputCodeActivity;
import com.gelato.gelato.R;
import com.gelato.gelato.SettingActivity;
import com.gelato.gelato.models.Channel;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Mathpresso2 on 2015-12-03.
 */
public class CustomDrawerActivity extends CustomAppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private TextView txtvName;
    private ImageView imgvPortrait;

    private static int NEW_CHANNEL = 1324322;

    public DrawerLayout getDrawerLayout() {
        return mDrawerLayout;
    }

    protected boolean isDrawerOpen() {
        if(getDrawerLayout()!=null) {
            return (getDrawerLayout().isDrawerOpen(Gravity.LEFT) || getDrawerLayout().isDrawerOpen(Gravity.RIGHT));
        }
        return false;
    }

    protected void closeDrawer() {
        if (getDrawerLayout().isDrawerOpen(Gravity.RIGHT)) {
            getDrawerLayout().closeDrawer(Gravity.RIGHT);
        }
        if (getDrawerLayout().isDrawerOpen(Gravity.LEFT)) {
            getDrawerLayout().closeDrawer(Gravity.LEFT);
        }
    }

    protected void openRight() {
        mDrawerLayout.openDrawer(Gravity.RIGHT);
    }

    @Override
    public void onBackPressed() {
        if (isDrawerOpen()) {
            closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    protected Toolbar getToolbar() {
        return mToolbar;
    }

    protected void onDrawerOpened() {
    }

    @Deprecated
    protected void scrollNavigationToTop() {
        // FIXME : not working
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mNavigationView.scrollTo(0, 0);
                mDrawerLayout.scrollTo(0, 0);
            }
        });
    }

    ArrayList<Channel> channels;

    protected void initializeToolbar(Toolbar toolbar) {
        mToolbar = toolbar;
        if (mToolbar != null) {
            mToolbar.setTitle("");
            setSupportActionBar(mToolbar);
            // Initializing Drawer Layout and ActionBarToggle
            mNavigationView = (NavigationView) findViewById(R.id.nav_view);
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            View headerView = mNavigationView.inflateHeaderView(R.layout.nav_header_main);
            txtvName = (TextView) headerView.findViewById(R.id.txtvName);
            imgvPortrait = (ImageView) headerView.findViewById(R.id.imgvPortrait);
            setDrawerColor(mNavigationView);

            if (AppController.getInstance().getLoginManager().getUser() != null) {
                txtvName.setText(AppController.getInstance().getLoginManager().getUser().getUserName());
                Glide.with(this)
                        .load(AppController.getInstance().getLoginManager().getUser().getUserProfileUrl())
                        .bitmapTransform(new CropCircleTransformation(this))
                        .placeholder(R.drawable.ic_account_circle_white_48dp)
                        .into(imgvPortrait);
            }


            ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    AppController.getInstance().sendScreenChange("DrawerOpen");
                    CustomDrawerActivity.this.onDrawerOpened();
                    super.onDrawerOpened(drawerView);
                }
            };
            // Setting the actionbarToggle to drawer layout
            mDrawerLayout.setDrawerListener(actionBarDrawerToggle);
            channels = new ArrayList<>();
            channels = AppController.getInstance().getLocalStore().getChannels();

            int i = 1;
            SubMenu subMenu, subMenu_end;
            MenuItem item = mNavigationView.getMenu().findItem(R.id.channels);
            MenuItem item_end = mNavigationView.getMenu().findItem(R.id.expired_channels);
            subMenu = item.getSubMenu();
            subMenu_end = item_end.getSubMenu();
            for (Channel channel : channels) {
                if(channel.getExpiryAt().isAfterNow()) {
                    subMenu.add(R.id.channels, channel.getChannelId(), Menu.NONE, channel.getChannelName()).setIcon(getResources().getDrawable(R.drawable.ic_nothing));
                }else{
                    subMenu_end.add(R.id.expired_channels, channel.getChannelId(), Menu.NONE, channel.getChannelName()).setIcon(getResources().getDrawable(R.drawable.ic_nothing));
                }
                i++;

//                if(channel.getExpiryAt().isAfterNow()) {
//                    subMenu.add(R.id.channels, channel.getChannelId(), Menu.NONE, channel.getChannelName());
//                }else{
//                    subMenu_end.add(R.id.expired_channels, channel.getChannelId(), Menu.NONE, channel.getChannelName());
//                }
//                i++;
            }
            subMenu.add(R.id.channels, NEW_CHANNEL, Menu.NONE, String.format("새로운 채널 생성하기", i)).setIcon(getResources().getDrawable(R.drawable.ic_add_black_24dp));
            subMenu.setHeaderIcon(R.drawable.ic_channel);
            subMenu.setHeaderIcon(getResources().getDrawable(R.drawable.ic_channel));
            //subMenu.add(R.id.channels, NEW_CHANNEL, Menu.NONE, String.format("새로운 채널", i));

            // Initializing NavigationView
            // Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
            mNavigationView.setNavigationItemSelectedListener(this);

            // calling sync state is necessay or else your hamburger icon wont show up
            actionBarDrawerToggle.syncState();
        }
    }

    private void setDrawerColor(NavigationView mNavigationView) {
        int[][] state = new int[][]{
                new int[]{-android.R.attr.state_enabled}, // disabled
                new int[]{android.R.attr.state_enabled}, // enabled
                new int[]{-android.R.attr.state_checked}, // unchecked
                new int[]{android.R.attr.state_pressed}  // pressed
        };

        int[] color = new int[]{
                Color.DKGRAY,
                Color.DKGRAY,
                Color.BLUE,
                Color.DKGRAY
        };

        ColorStateList csl = new ColorStateList(state, color);

        int[][] states = new int[][]{
                new int[]{-android.R.attr.state_enabled}, // disabled
                new int[]{android.R.attr.state_enabled}, // enabled
                new int[]{-android.R.attr.state_checked}, // unchecked
                new int[]{android.R.attr.state_pressed}  // pressed
        };

        int[] colors = new int[]{
                Color.GRAY,
                Color.GRAY,
                Color.BLUE,
                Color.DKGRAY
        };

        ColorStateList csl2 = new ColorStateList(states, colors);
        mNavigationView.setItemIconTintList(csl);
        mNavigationView.setItemIconTintList(csl2);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //loadProfileImage();
    }

    Intent intent;

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        //Checking if the item is in checked state or not, if not make it in checked state
        if (menuItem.isChecked()) menuItem.setChecked(false);
        else menuItem.setChecked(true);
        AppController.getInstance().sendEvent(menuItem.getTitle().toString(), "open", "NavigationDrawer");
        //Closing drawer on item click
        mDrawerLayout.closeDrawers();
        //Check to see which item was being clicked and perform appropriate action
        if (menuItem.getGroupId() == R.id.channels || menuItem.getGroupId() ==  R.id.expired_channels) {
            if (menuItem.getItemId() == NEW_CHANNEL) {
                intent = new Intent(getApplicationContext(), CreateChannelorInputCodeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);
                return true;
            } else {
                intent = new Intent(getApplicationContext(), ChannelActivity.class);
                intent.putExtra("channel_id", menuItem.getItemId());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);
                return true;
            }
        } else {
            switch (menuItem.getItemId()) {
                case R.id.nav_setting:
                    intent = new Intent(getApplicationContext(), SettingActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(0, 0);
                    return true;
                default:
                    Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                    return false;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getSupportFragmentManager().popBackStack();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.imgvPortrait:
                mDrawerLayout.closeDrawers();
                //Intent profile = new Intent(getApplicationContext(), ProfileActivity.class);
                //startActivityForResult(profile, 1);
                break;
        }
    }
}
