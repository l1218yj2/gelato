package com.gelato.gelato.tools.pullToLoadView;

/**
 * @author bian.xd
 */
public interface PullCallback {

    void onLoadMore();

    void onRefresh();

    boolean isLoading();

    boolean hasLoadedAllItems();

}
