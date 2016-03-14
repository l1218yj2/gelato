package com.gelato.gelato;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gelato.gelato.cores.AppController;
import com.gelato.gelato.cores.CustomAdapter;
import com.gelato.gelato.models.MemberMission;
import com.gelato.gelato.models.MissionItem;
import com.gelato.gelato.tools.ImageFilePath;
import com.gelato.gelato.tools.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Mathpresso2 on 2015-09-10.
 */
public class TimeLineAdapter extends CustomAdapter<TimeLineAdapter.TimeLineItem, RecyclerView.ViewHolder> {
    OnTimeLineItemSelectedListener mListener;

    public static final int
            MISSION_ITEM = 100, TODO_MISSION = 200;

    public interface OnTimeLineItemSelectedListener {
        void onSelect(MissionItem timeLineItem);

        void onSelect(MemberMission memberMission);
    }

    public class TimeLineItem {
        int type;
        MissionItem missionItem;
        MemberMission memberMission;

        private TimeLineItem(int type) {
            this.type = type;
        }

        public void bindMissionItem(MissionItem missionItem) {
            this.missionItem = missionItem;
        }

        public void bindMission(MemberMission memberMission) {
            this.memberMission = memberMission;
        }

        public int getObjectType() {
            return type;
        }
    }


    public TimeLineItem initMissionItem(MissionItem missionItem) {
        TimeLineItem item = new TimeLineItem(MISSION_ITEM);
        item.bindMissionItem(missionItem);
        return item;
    }

    public TimeLineItem initMission(MemberMission memberMissionItem) {
        TimeLineItem item = new TimeLineItem(TODO_MISSION);
        item.bindMission(memberMissionItem);
        return item;
    }

    public TimeLineAdapter(Context context, List<TimeLineItem> data, OnTimeLineItemSelectedListener listener) {
        super(context, data);
        requestPermission();
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
            case MISSION_ITEM:
                viewHolder = new MissionItemViewHolder(
                        inflater.inflate(R.layout.item_time_line, parent, false)
                );
                break;
            case TODO_MISSION:
                viewHolder = new MissionViewHolder(
                        inflater.inflate(R.layout.item_mission, parent, false)
                );
                break;
            default:
                return null;
        }
        return viewHolder;
    }


    private boolean hasPermission() {
        int storage = ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (storage == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        else {
            return false;
        }
    }
    public void requestPermission() {
        // CAMERA, WRITE_EXTERNAL_STORAGE 권한 요청
        ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION:
                if (grantResults.length >= 1
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // do nothing (camera will be initialized at onResume)
                } else {
                    Toast.makeText(AppController.getInstance().getApplicationContext(), "저장할 수 있는 권한이 없습니다.", Toast.LENGTH_LONG).show();
                }
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static Uri handleImageUri(Uri uri) {
        Pattern pattern = Pattern.compile("(content://media/.*\\d)");
        if (uri.getPath().contains("content")) {
            Matcher matcher = pattern.matcher(uri.getPath());
            if (matcher.find())
                return Uri.parse(matcher.group(1));
            else
                throw new IllegalArgumentException("Cannot handle this URI");
        } else
            return uri;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static String getFilePathFromKitkatUri(Context context, Uri uri) {
        Cursor cursor = null;
        try {
            // (갤러리에서 선택) content://media/external/images/media/xxx
            Uri newUri = handleImageUri(uri);
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(newUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            if (cursor.moveToFirst()) {
                String filePath = cursor.getString(column_index);
                if (filePath != null) {
                    return filePath;
                }
            }
        } catch (Exception ignored) {
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        try {
            // (그냥선택) content://com.android.providers.media.documents/document/image%3Axxx
            String wholeId = DocumentsContract.getDocumentId(uri);
            String id = wholeId.split(":")[1];
            String[] column = {MediaStore.Images.Media.DATA};

            String sel = MediaStore.Images.Media._ID + "=?";
            cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    column, sel, new String[]{id}, null);
            int columnIndex = cursor.getColumnIndex(column[0]);
            if (cursor.moveToFirst()) {
                String filePath = cursor.getString(columnIndex);
                if (filePath != null) {
                    return filePath;
                }
            }
        } catch (Exception ignored) {
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    protected static String getFilePathFromUri(Context context, Uri uri) {
        if (Build.VERSION.SDK_INT < 19) {
            return ImageFilePath.getPath(context, uri);
        } else {
            return getFilePathFromKitkatUri(context, uri);
        }
    }

    // make picture and save to a folder
    private static File getOutputMediaFile() {
        File mediaStorageDir = Utils.getDCIMDirectory();

        if (mediaStorageDir == null) {
            return null;
        }
        //take the current timeStamp
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        //and make a media file:
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");

        return mediaFile;
    }

    /*
     * open & release camera
     */
    static final int  REQUEST_CAMERA_PERMISSION = 33;

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // bind data
        if (holder instanceof MissionItemViewHolder) {
            configurMissionItemViewHolder((MissionItemViewHolder) holder, position);
        } else if (holder instanceof MissionViewHolder) {
            configureMissionViewHolder((MissionViewHolder) holder, position);
        }
    }

    private void configurMissionItemViewHolder(final MissionItemViewHolder holder, int position) {
        final MissionItem item = mItems.get(position).missionItem;
        holder.txtvContent.setText(item.getTimelineContent());
        holder.txtvFrom.setText(item.getGiverNickname());
        holder.txtvMission.setText(item.getTimelineContentTitle());
        holder.txtvTime.setText(Utils.toFriendlyDateTimeString(item.getRegTime()));
        holder.txtvTo.setText(item.getReceiverName());
        holder.imgvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hasPermission()) {
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, saveImageToFile(holder.itemView));
                    shareIntent.setType("image/jpeg");
                    mContext.startActivity(Intent.createChooser(shareIntent, "카드를 공유합니다."));
                }
            }
        });
        holder.imgvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hasPermission()) {
                    saveImageToFile(holder.itemView);
                    Toast.makeText(mContext,"저장되었습니다.", Toast.LENGTH_LONG).show();
                }
            }
        });
        if (!item.getTimelinePictureUrl().equals("0")) {
            holder.imgvContent.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(item.getTimelinePictureUrl())
                    .centerCrop()
                    .placeholder(R.drawable.timeline_placeholder)
                    .into(holder.imgvContent);
        }else{
            holder.imgvContent.setVisibility(View.GONE);
            Glide.clear(holder.imgvContent);
            holder.imgvContent.setImageDrawable(null);
        }
        if (!item.getGiverImgUrl().contentEquals("0")) {
            Glide.with(mContext)
                    .load(item.getGiverImgUrl())
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .placeholder(R.drawable.ic_account_circle_grey600_36dp)
                    .into(holder.imgvPortrait);
        }else{
            Glide.clear(holder.imgvPortrait);
            holder.imgvPortrait.setImageDrawable(null);
        }
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ViewMissionItemActivity.class);
                intent.putExtra("mission_item", item);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
                mListener.onSelect(item);
            }
        });
    }

    private android.net.Uri saveImageToFile(View itemView) {
        try {
            itemView.setDrawingCacheEnabled(true);
            Bitmap bitmap = itemView.getDrawingCache();
            File file, f = null;
            if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
                file = new File(android.os.Environment.getExternalStorageDirectory(), "TTImages_cache");
                if (!file.exists()) {
                    file.mkdirs();
                }
                f = new File(file.getAbsolutePath() + File.separator + "filename" + ".png");
            }
            FileOutputStream ostream = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 10, ostream);
            ostream.close();
            return Uri.fromFile(f);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void configureMissionViewHolder(final MissionViewHolder holder, int position) {
        final MemberMission item = mItems.get(position).memberMission;
        holder.txtvTitle.setText(item.getMissionName());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onSelect(item);
            }
        });
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MissionViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public TextView txtvTitle;

        public MissionViewHolder(View v) {
            super(v);
            mView = itemView;
            txtvTitle = (TextView) itemView.findViewById(R.id.txtvTitle);
        }
    }

    public static class MissionItemViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public TextView txtvFrom, txtvTo, txtvTime, txtvMission;
        public TextView txtvContent;
        public ImageView imgvContent, imgvPortrait, imgvSave, imgvShare;

        public MissionItemViewHolder(View v) {
            super(v);
            mView = itemView;
            imgvSave = (ImageView) itemView.findViewById(R.id.imgvSave);
            imgvShare = (ImageView) itemView.findViewById(R.id.imgvShare);
            txtvFrom = (TextView) itemView.findViewById(R.id.txtvFrom);
            txtvTo = (TextView) itemView.findViewById(R.id.txtvTo);
            txtvTime = (TextView) itemView.findViewById(R.id.txtvTime);
            txtvMission = (TextView) itemView.findViewById(R.id.txtvMission);
            txtvContent = (TextView) itemView.findViewById(R.id.txtvContent);
            imgvContent = (ImageView) itemView.findViewById(R.id.imgvContent);
            imgvPortrait = (ImageView) itemView.findViewById(R.id.imgvPortrait);
        }
    }
}
