package com.gelato.gelato;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gelato.gelato.cores.CustomAdapter;
import com.gelato.gelato.models.AdminMission;
import com.gelato.gelato.tools.circleprogress.CircleProgressView;

import java.util.List;

import static com.gelato.gelato.tools.DisplayUtils.dp2px;

/**
 * Created by Mathpresso2 on 2015-09-10.
 */
public class AdminMissionAdapter extends CustomAdapter<AdminMissionAdapter.AdminMissionItem, RecyclerView.ViewHolder> {
    onMissionAdminSelectedListener mListener;

    public static final int
            PLUS_MISSION = 100, LIST_MISSION = 200;

    public interface onMissionAdminSelectedListener {
        void onSelect(AdminMission mission);

        void onSelect();
    }

    public class AdminMissionItem {
        int type;
        AdminMission mission;

        private AdminMissionItem(int type) {
            this.type = type;
        }

        public void bindMission(AdminMission mission) {
            this.mission = mission;
        }

        public int getObjectType() {
            return type;
        }
    }

    public AdminMissionItem initMission(AdminMission missionItem) {
        AdminMissionItem item = new AdminMissionItem(LIST_MISSION);
        item.bindMission(missionItem);
        return item;
    }

    public AdminMissionItem initPlusMission() {
        AdminMissionItem item = new AdminMissionItem(PLUS_MISSION);
        return item;
    }

    public AdminMissionAdapter(Context context, List<AdminMissionItem> data, onMissionAdminSelectedListener listener) {
        super(context, data);
        mListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return mItems.get(position).getObjectType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case PLUS_MISSION:
                viewHolder = new PlusMissionViewHolder(
                        inflater.inflate(R.layout.item_admin_mission_plus, parent, false)
                );
                break;
            case LIST_MISSION:
                viewHolder = new MissionViewHolder(
                        inflater.inflate(R.layout.item_admin_mission, parent, false)
                );
                break;
            default:
                return null;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // bind data
        if (holder instanceof PlusMissionViewHolder) {
            configurPlusMissionViewHolder((PlusMissionViewHolder) holder, position);
        } else if (holder instanceof MissionViewHolder) {
            configureMissionViewHolder((MissionViewHolder) holder, position);
        }
    }

    private void configurPlusMissionViewHolder(final PlusMissionViewHolder holder, int position) {

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onSelect();
            }
        });
    }

    private void configureMissionViewHolder(final MissionViewHolder holder, int position) {
        final AdminMission item = mItems.get(position).mission;
        holder.txtvTitle.setText(item.getMissionName());
        holder.txtvTitle.setTextColor(item.getColor(mContext));
        holder.txtvState.setText(item.getState());
        holder.txtvState.setTextColor(item.getColor(mContext));
        holder.txtvDate.setTextColor(item.getColor(mContext));
        //holder.circularProgress.setProgress(item.getPercent());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onSelect(item);
            }
        });
        holder.txtvDate.setText(item.getDateState());
        holder.circularProgress.setValue(item.getPercent());
        holder.circularProgress.setText(item.getPercent()+"%");
        holder.circularProgress.setTextSize(dp2px(mContext, 9));
        holder.circularProgress.setUnitSize(dp2px(mContext, 9));
        holder.circularProgress.setRimColor(mContext.getResources().getColor(R.color.grey_200));
        holder.circularProgress.setUnitVisible(false);
        //spin 아니고
        holder.circularProgress.setUnitColor(item.getColor(mContext));
        holder.circularProgress.setBarColor(item.getColor(mContext));
        holder.circularProgress.setUnit("%");
        holder.circularProgress.setTextColor(item.getColor(mContext));

    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    public static class MissionViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public TextView txtvTitle;
        public TextView txtvState, txtvDate;
        CircleProgressView circularProgress;

        public MissionViewHolder(View v) {
            super(v);
            mView = itemView;
            txtvTitle = (TextView) itemView.findViewById(R.id.txtvTitle);
            txtvState = (TextView) itemView.findViewById(R.id.txtvState);
            txtvDate = (TextView) itemView.findViewById(R.id.txtvDate);
            circularProgress = (CircleProgressView) itemView.findViewById(R.id.circleView);
        }
    }

    public static class PlusMissionViewHolder extends RecyclerView.ViewHolder {
        public View mView;

        public PlusMissionViewHolder(View v) {
            super(v);
            mView = itemView;
        }
    }
}
