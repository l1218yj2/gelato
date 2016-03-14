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
import com.gelato.gelato.models.UserMission;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Mathpresso2 on 2015-09-10.
 */
public class UserMissionAdapter extends CustomAdapter<UserMission, UserMissionAdapter.UserMissionViewHolder> {


    public UserMissionAdapter(Context context, List<UserMission> data) {
        super(context, data);
    }

    @Override
    public UserMissionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        UserMissionViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        viewHolder = new UserMissionViewHolder(
                inflater.inflate(R.layout.item_user_mission, parent, false)
        );

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(UserMissionViewHolder holder, int position) {

        UserMission userMission = mItems.get(position);
        holder.txtvName.setText(userMission.getUserName());
        holder.txtvState.setText(userMission.isDone() ? "미션 완료" : "미션 안함");
        holder.txtvState.setTextColor(userMission.isDone() ? mContext.getResources().getColor(R.color.grey_800) : mContext.getResources().getColor(R.color.red_400));

        Glide.with(mContext)
                .load(userMission.getUserProfileUrl())
                .bitmapTransform(new CropCircleTransformation(mContext))
                .placeholder(R.drawable.ic_account_circle_grey600_36dp)
                .into(holder.imageView);

        // bind data
    }


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class UserMissionViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public TextView txtvName, txtvState;
        ImageView imageView;

        public UserMissionViewHolder(View v) {
            super(v);
            mView = itemView;
            txtvName = (TextView) itemView.findViewById(R.id.txtvName);
            txtvState = (TextView) itemView.findViewById(R.id.txtvState);
            imageView = (ImageView) itemView.findViewById(R.id.imgvPortrait);
        }
    }
}
