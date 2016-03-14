package com.gelato.gelato.tools;

/**
 * Created by Mathpresso2 on 2015-12-21.
 */
public interface ItemTouchHelperAdapter {

    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}
