package com.gelato.gelato;
/**
 * Created by YJLaptop on 2016-02-05.
 */
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gelato.gelato.cores.AppController;
import com.gelato.gelato.cores.CustomFragment;
import com.gelato.gelato.models.MemberMission;
import com.gelato.gelato.models.MissionItem;
import com.gelato.gelato.tools.pullToLoadView.PullCallback;
import com.gelato.gelato.tools.pullToLoadView.PullToLoadView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mathpresso3 on 2015-08-04.
 */

public class AdminTimeLineFragment extends CustomFragment {
    private final String TAG = this.getClass().getName();

    Boolean isLoading, isHasLoadedAll;
    RecyclerView recvTimeLine;
    TimeLineAdapter mAdapter;
    LinearLayoutManager mLayoutManager;
    PullToLoadView mPullToLoadView;

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

//            RecyclerView.ItemDecoration itemDecoration = new HorizontalDividerItemDecoration.Builder(getActivity())
//                    .color(getResources().getColor(R.color.indigo_050))
//                    .build();
//            recvTimeLine.addItemDecoration(itemDecoration);

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
                    refresh();
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

    public void refresh(){
        isHasLoadedAll = false;
        clearAdapter();
        loadTImeLine();
    }

    private void loadTImeLine() {
        isLoading = true;
        Call<List<MissionItem>> CallMissionItems = AppController.getInstance().getDataManager().getMissionItemService().getAdminTimeLineMissionItems(((ChannelActivity) getActivity()).getChannelId());
        CallMissionItems.enqueue(new Callback<List<MissionItem>>() {
            @Override
            public void onResponse(Call<List<MissionItem>> call, Response<List<MissionItem>> response) {
                if (response.isSuccess()) {
                    for (MissionItem mission : response.body()) {
                        mAdapter.add(mAdapter.initMissionItem(mission));
                    }
                    isLoading = false;
                    mPullToLoadView.setComplete();
                } else {
                    isLoading = false;
                    mPullToLoadView.setComplete();
                }
            }

            @Override
            public void onFailure(Call<List<MissionItem>> call, Throwable t) {
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
        mAdapter = new TimeLineAdapter(
                getActivity(), null, new TimeLineAdapter.OnTimeLineItemSelectedListener() {
            @Override
            public void onSelect(MissionItem mission) {

            }
            @Override
            public void onSelect(MemberMission memberMission) {

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