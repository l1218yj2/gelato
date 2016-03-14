package com.gelato.gelato;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gelato.gelato.cores.CustomAppCompatActivity;
import com.gelato.gelato.cores.CustomDrawerActivity;
import com.gelato.gelato.models.MissionItem;
import com.gelato.gelato.tools.Utils;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ViewMissionItemActivity extends CustomAppCompatActivity{
    public View mView;
    public TextView txtvFrom, txtvTo, txtvTime, txtvMission;
    public TextView txtvContent;
    public ImageView imgvContent, imgvPortrait;
    Toolbar toolbar;
    PhotoViewAttacher mAttacher;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_mission_item);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitle("세부정보");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txtvFrom = (TextView) findViewById(R.id.txtvFrom);
        txtvTo = (TextView) findViewById(R.id.txtvTo);
        txtvTime = (TextView) findViewById(R.id.txtvTime);
        txtvMission = (TextView) findViewById(R.id.txtvMission);
        txtvContent = (TextView) findViewById(R.id.txtvContent);
        imgvContent = (ImageView) findViewById(R.id.imgvContent);
        imgvPortrait = (ImageView) findViewById(R.id.imgvPortrait);
        MissionItem item = (MissionItem) getIntent().getSerializableExtra("mission_item");
        if(item==null){
            finish();
        }else {
            txtvContent.setText(item.getTimelineContent());
            txtvFrom.setText(item.getGiverNickname());
            txtvMission.setText(item.getTimelineContentTitle());
            txtvTime.setText(Utils.toFriendlyDateTimeString(item.getRegTime()));
            txtvTo.setText(item.getReceiverName());
            if (!item.getTimelinePictureUrl().equals("0")) {
                imgvContent.setVisibility(View.VISIBLE);
                Glide.with(this)
                        .load(item.getTimelinePictureUrl())
                        .placeholder(R.drawable.timeline_placeholder)
                        .into(imgvContent);
            } else {
                imgvContent.setVisibility(View.GONE);
                Glide.clear(imgvContent);
                imgvContent.setImageDrawable(null);
            }
            if (!item.getGiverImgUrl().contentEquals("0")) {
                Glide.with(this)
                        .load(item.getGiverImgUrl())
                        .bitmapTransform(new CropCircleTransformation(this))
                        .placeholder(R.drawable.ic_account_circle_grey600_36dp)
                        .into(imgvPortrait);
            } else {
                Glide.clear(imgvPortrait);
                imgvPortrait.setImageDrawable(null);
            }
        }
    }
}
