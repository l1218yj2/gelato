package com.gelato.gelato;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import com.gelato.gelato.cores.CustomAppCompatActivity;
import com.gelato.gelato.models.AdminMission;
import com.gelato.gelato.tools.circleprogress.CircleProgressView;
import com.gelato.gelato.tools.progresspercent.CircularProgress;

import static com.gelato.gelato.tools.DisplayUtils.dp2px;

public class ViewMissionAdminActivity extends CustomAppCompatActivity{
    public TextView txtvTitle, txtvState, txtvDate;
    CircleProgressView circularProgress;
    RecyclerView recyclerView;
    UserMissionAdapter userMissionAdapter;
    android.support.v7.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_mission_admin);
        AdminMission item = (AdminMission) getIntent().getSerializableExtra("mission_item");
        toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitle(item.getMissionName());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(item==null){
            finish();
        }else {
            txtvTitle = (TextView) findViewById(R.id.txtvTitle);
            txtvState = (TextView) findViewById(R.id.txtvState);
            txtvDate = (TextView) findViewById(R.id.txtvDate);
            circularProgress = (CircleProgressView) findViewById(R.id.circleView);
            txtvTitle.setText(item.getMissionName());
            txtvTitle.setTextColor(item.getColor(this));
            txtvState.setText(item.getState());
            txtvState.setTextColor(item.getColor(this));
            txtvDate.setTextColor(item.getColor(this));
            circularProgress.setUnitVisible(false);
            txtvDate.setText(item.getDateState());
            circularProgress.setTextSize(dp2px(this, 26));
            circularProgress.setUnitSize(dp2px(this, 22));
            circularProgress.setValue(item.getPercent());
            circularProgress.setText(item.getPercent()+"%");
            circularProgress.setRimColor(getResources().getColor(R.color.grey_200));
            //spin 아니고
            circularProgress.setUnitColor(item.getColor(this));
            circularProgress.setBarColor(item.getColor(this));
            circularProgress.setUnit("%");
            circularProgress.setTextColor(item.getColor(this));
            recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            userMissionAdapter = new UserMissionAdapter(this, item.getUserMission());
            recyclerView.setAdapter(userMissionAdapter);
        }
    }
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
}
