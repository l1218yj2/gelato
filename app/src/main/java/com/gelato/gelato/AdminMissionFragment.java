package com.gelato.gelato;
/**
 * Created by YJLaptop on 2016-02-05.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gelato.gelato.cores.AppController;
import com.gelato.gelato.cores.CustomFragment;
import com.gelato.gelato.models.AdminMission;
import com.gelato.gelato.tools.pullToLoadView.PullCallback;
import com.gelato.gelato.tools.pullToLoadView.PullToLoadView;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mathpresso3 on 2015-08-04.
 */

public class AdminMissionFragment extends CustomFragment {
    private final String TAG = this.getClass().getName();
    Boolean isLoading, isHasLoadedAll;
    PullToLoadView mPullToLoadView;
    RecyclerView recvTimeLine;
    AdminMissionAdapter mAdapter;
    LinearLayoutManager mLayoutManager;

    public void refresh() {
        isHasLoadedAll = false;
        clearAdapter();
        loadTImeLine();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_timeline, container, false);

        if (v != null) {
            mPullToLoadView = (PullToLoadView) v.findViewById(R.id.pullToLoadView);
            recvTimeLine = mPullToLoadView.getRecyclerView();

            RecyclerView.ItemDecoration itemDecoration = new HorizontalDividerItemDecoration.Builder(getActivity())
                    .color(getResources().getColor(R.color.indigo_050))
                    .build();
            recvTimeLine.addItemDecoration(itemDecoration);

            isHasLoadedAll = false;
            isLoading = false;

            initializerAdapter();

            mPullToLoadView.isLoadMoreEnabled(false);
            mPullToLoadView.setPullCallback(new PullCallback() {
                @Override
                public void onLoadMore() {


                }

                @Override
                public void onRefresh() {
                    isHasLoadedAll = false;
                    clearAdapter();
                    loadTImeLine();
                }


                @Override
                public boolean isLoading() {
                    //Log.e("main activity", "main isLoading:" + isLoading);
                    return isLoading;
                }

                @Override
                public boolean hasLoadedAllItems() {
                    return isHasLoadedAll;
                }
            });
            mPullToLoadView.initLoad();
        }
        return v;
    }


    @Override
    public void onResume() {
        super.onResume();
        mPullToLoadView.setComplete();
    }

    private void loadTImeLine() {
        isLoading = true;
        AppController.getInstance().getDataManager().getMissionService().getAdminMission(((ChannelActivity) getActivity()).getChannelId()).enqueue(new Callback<List<AdminMission>>() {
            @Override
            public void onResponse(Call<List<AdminMission>> call, Response<List<AdminMission>> response) {
                if (response.isSuccess()) {
                    if (((ChannelActivity) getActivity()).getChannel().getExpiryAt().isAfterNow()) {
                        mAdapter.add(mAdapter.initPlusMission());
                    }
                    for (AdminMission mission : response.body()) {
                        mAdapter.add(mAdapter.initMission(mission));
                    }
                    isLoading = false;
                    mPullToLoadView.setComplete();
                } else {
                    isLoading = false;
                    mPullToLoadView.setComplete();
                }
            }

            @Override
            public void onFailure(Call<List<AdminMission>> call, Throwable t) {
                isLoading = false;
                mPullToLoadView.setComplete();
            }
        });
    }

    private void clearAdapter() {
        mAdapter.clear();
    }

    private void initializerAdapter() {
        // initialize question adapter
        mAdapter = new AdminMissionAdapter(getActivity(), null, new AdminMissionAdapter.onMissionAdminSelectedListener() {
            @Override
            public void onSelect(AdminMission mission) {
                Intent intent = new Intent(getActivity(), ViewMissionAdminActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("mission_item", mission);
                startActivity(intent);
            }

            @Override
            public void onSelect() {
                if (((ChannelActivity) getActivity()).getChannel().getExpiryAt().isAfterNow()) {
                    Intent intent = new Intent(getActivity(), CreateMissionActivity.class);
                    intent.putExtra("channel_id", ((ChannelActivity) getActivity()).getChannelId());
                    getActivity().startActivityForResult(intent, ChannelActivity.MAKE_MISSION);
                } else {
                    Toast.makeText(getActivity(), "만료된 채널 입니다.", Toast.LENGTH_LONG).show();
                }
            }
        });

        recvTimeLine.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recvTimeLine.setLayoutManager(mLayoutManager);
    }

    @Override
    public void onStop() {
        super.onStop();
        isLoading = false;
        mPullToLoadView.setComplete();
        //AppController.getInstance().setQuestionListValidation(false);
    }

    @Override
    public void onClick(View v) {
    }


}