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
import com.gelato.gelato.models.Manito;
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

public class ManitoFragment extends CustomFragment {
    private final String TAG = this.getClass().getName();
    Boolean isLoading, isHasLoadedAll;
    PullToLoadView mPullToLoadView;
    RecyclerView recvTimeLine;
    UserAdminAdapter mAdapter;
    LinearLayoutManager mLayoutManager;

    public void refresh(){
        isHasLoadedAll = false;
        clearAdapter();
        loadTImeLine();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPullToLoadView.setComplete();
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
                    .color(getResources().getColor(R.color.grey_700))
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

    private void loadTImeLine() {
        isLoading = true;
        AppController.getInstance().getDataManager().getChannelService().getChannelAdminUsers(((ChannelActivity) getActivity()).getChannelId()).
        enqueue(new Callback<List<Manito>>() {
            @Override
            public void onResponse(Call<List<Manito>> call, Response<List<Manito>> response) {
                if (response.isSuccess()) {
                    for (Manito mission : response.body()) {
                        mAdapter.add(mission);
                    }
                    isLoading = false;
                    mPullToLoadView.setComplete();
                } else {
                    isLoading = false;
                    mPullToLoadView.setComplete();
                }
            }

            @Override
            public void onFailure(Call<List<Manito>> call, Throwable t) {
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
        mAdapter = new UserAdminAdapter(getActivity(), null);

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