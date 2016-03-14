package com.gelato.gelato;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.gelato.gelato.cores.AppController;
import com.gelato.gelato.cores.CustomAppCompatActivity;
import com.gelato.gelato.models.Mission;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by taebinkim on 2016. 2. 20..
 */
public class CreateMissionActivity extends CustomAppCompatActivity {

    com.gelato.gelato.tools.NoDefaultSpinner spinner;
    EditText editText;
    int channelId;
    MissionSpinnerAdapter missionSpinnerAdapter;

    TextView txtvDate;
    DateTime expiry_date;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_upload:
                this.upload();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_mission);
        toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitle("미션 부여하기");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        spinner = (com.gelato.gelato.tools.NoDefaultSpinner) findViewById(R.id.spinner);
        spinner.setPrompt("부여 할 미션을 선택해 주세요.");
        editText = (EditText) findViewById(R.id.etxtMission);
        channelId = getIntent().getIntExtra("channel_id", AppController.getInstance().getLocalStore().getChannels().get(0).getChannelId());
        txtvDate = (TextView) findViewById(R.id.txtvDate);
        txtvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarDatePickerDialogFragment.OnDateSetListener dateSetListener = new CalendarDatePickerDialogFragment.OnDateSetListener() {
                    @Override
                    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
                        // Set date from user input.
                        Calendar date = Calendar.getInstance();
                        date.set(Calendar.HOUR_OF_DAY, 9);
                        date.set(Calendar.MINUTE, 0);
                        date.set(Calendar.YEAR, year);
                        date.set(Calendar.MONTH, monthOfYear);
                        date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        expiry_date=new DateTime(date);
                        txtvDate.setText(expiry_date.toString("yyyy-MM-dd"));
                        // Do as you please with the date.
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

        AppController.getInstance().getDataManager().getMissionService().getAllMission().enqueue(new Callback<List<Mission>>() {
            @Override
            public void onResponse(Call<List<Mission>> call, Response<List<Mission>> response) {
                if (response.isSuccess()) {
                    missionSpinnerAdapter = new MissionSpinnerAdapter(CreateMissionActivity.this, R.layout.item_missions,
                             new ArrayList<Mission>(response.body()));
                    spinner.setAdapter(missionSpinnerAdapter);
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if(position>=0) {
                                mission = missionSpinnerAdapter.getItem(position);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Mission>> call, Throwable t) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.upload, menu);
        return true;
    }

    Mission mission;

    private void upload() {
        if(mission!=null && editText.getText()!=null&& expiry_date!=null) {
            AppController.getInstance().getDataManager().getMissionService().postMission(channelId, mission.getMissionId(), editText.getText().toString(), expiry_date).enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if(response.isSuccess()){
                        setResult(RESULT_OK);
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {

                }
            });
        }else{
            Toast.makeText(CreateMissionActivity.this, "세부내용과 만료 날짜를 확인해주세요.", Toast.LENGTH_LONG).show();
        }
    }
}
