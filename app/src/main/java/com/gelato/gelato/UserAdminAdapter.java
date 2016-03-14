package com.gelato.gelato;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gelato.gelato.cores.CustomAdapter;
import com.gelato.gelato.models.Manito;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Mathpresso2 on 2015-09-10.
 */
public class UserAdminAdapter extends CustomAdapter<Manito, UserAdminAdapter.ManitoViewHolder> {


    public UserAdminAdapter(Context context, List<Manito> data) {
        super(context, data);
    }

    @Override
    public ManitoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ManitoViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        viewHolder = new ManitoViewHolder(
                inflater.inflate(R.layout.item_user_admin, parent, false)
        );

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ManitoViewHolder holder, int position) {

        Manito userMission = mItems.get(position);
        holder.txtvName.setText(userMission.getReceiverUserName()+"님");
        holder.txtvName2.setText(userMission.getGiverUserName()+"님");
        holder.txtvState.setText(userMission.getFinishStatus());

        Glide.with(mContext)
                .load(userMission.getReceiverUserProfileUrl())
                .bitmapTransform(new CropCircleTransformation(mContext))
                .placeholder(R.drawable.ic_account_circle_grey600_36dp)
                .into(holder.imageView);

        // bind data
    }


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ManitoViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public TextView txtvName, txtvName2, txtvState;
        ImageView imageView;

        public ManitoViewHolder(View v) {
            super(v);
            mView = itemView;
            txtvName = (TextView) itemView.findViewById(R.id.txtvName);
            txtvName2 = (TextView) itemView.findViewById(R.id.txtvName2);
            txtvState = (TextView) itemView.findViewById(R.id.txtvState);
            imageView = (ImageView) itemView.findViewById(R.id.imgvPortrait);
        }
    }
}
