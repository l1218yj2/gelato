package com.gelato.gelato;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.gelato.gelato.cores.AppController;
import com.gelato.gelato.cores.CustomAppCompatActivity;
import com.gelato.gelato.models.Channel;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by taebinkim on 2016. 2. 20..
 */
public class CreateChannelActivity extends CustomAppCompatActivity {

    MaterialEditText etxtName;
    MaterialEditText txtvDate;
    DateTime expiry_date;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.daum, menu);
        menu.findItem(R.id.daum).setVisible(true);
        menu.findItem(R.id.daum).setOnMenuItemClickListener(
                new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.daum:
                                if (etxtName.getText() != null && expiry_date != null) {

                                    AppController.getInstance().getDataManager().getChannelService().makeChannel(etxtName.getText().toString(), expiry_date).enqueue(new Callback<Integer>() {
                                        @Override
                                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                                            if (response.isSuccess()) {
                                                loadChannel(response.body());
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Integer> call, Throwable t) {
                                            Log.d("dhchannel", t.toString());
                                        }
                                    });
                                }

                                break;
                        }
                        return true;
                    }
                }
        );
        return true;
    }

    private void loadChannel(final int channel_id) {
        Call<List<Channel>> channelCall = AppController.getInstance().getDataManager().getChannelService().getChannels();
        channelCall.enqueue(new Callback<List<Channel>>() {
            @Override
            public void onResponse(Call<List<Channel>> call, Response<List<Channel>> response) {
                if (response.isSuccess()) {
                    ArrayList<Channel> channels = new ArrayList<Channel>(response.body());
                    AppController.getInstance().getLocalStore().storeChannel(channels);
                    Intent intent = new Intent(CreateChannelActivity.this, WaitingRoomActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("channel_id", channel_id);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<List<Channel>> call, Throwable t) {

            }
        });
    }
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_channel);
        toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitle("채널 만들기");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        etxtName = (MaterialEditText) findViewById(R.id.etxtName);
        txtvDate = (MaterialEditText) findViewById(R.id.etxtDate);
        txtvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarDatePickerDialogFragment.OnDateSetListener dateSetListener = new CalendarDatePickerDialogFragment.OnDateSetListener() {
                    @Override
                    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
                        // Set date from user input.
                        Calendar date = Calendar.getInstance();
                        date.set(Calendar.HOUR_OF_DAY, 23);
                        date.set(Calendar.MINUTE, 59);
                        date.set(Calendar.YEAR, year);
                        date.set(Calendar.MONTH, monthOfYear);
                        date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        txtvDate.setText(String.format("만료일: %d년 %d월 %d일", year, monthOfYear+1, dayOfMonth));
                        expiry_date=new DateTime(date);
                    }
                };

                // Create dismiss listener.
                CalendarDatePickerDialogFragment.OnDialogDismissListener dismissListener = new CalendarDatePickerDialogFragment.OnDialogDismissListener() {
                    @Override
                    public void onDialogDismiss(DialogInterface dialoginterface) {
                        // Do something when the user dismisses the dialog.
                    }
                };
                // Show date picker dialog.
                CalendarDatePickerDialogFragment dialog = new CalendarDatePickerDialogFragment();
                dialog.setOnDateSetListener(dateSetListener);
                dialog.setOnDismissListener(dismissListener);
                dialog.setThemeDark(false);
                dialog.show(getSupportFragmentManager(), "DATE_PICKER_TAG");
            }
        });

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "CreateChannel Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.gelato.gelato/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "CreateChannel Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.gelato.gelato/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
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
