package com.gelato.gelato;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.gelato.gelato.cores.AppController;
import com.gelato.gelato.cores.CustomAppCompatActivity;
import com.gelato.gelato.models.MemberMission;
import com.gelato.gelato.models.User;
import com.gelato.gelato.tools.ImageFilePath;
import com.gelato.gelato.tools.Utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadMissionItemActivity extends CustomAppCompatActivity {

    TextView txtvTo, txtvFrom, txtvContent, txtvMission;
    EditText etxtMessage;
    ImageView imgvCamera, imgvPortrait;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && result != null && result.getData() != null) {
            Uri uri = result.getData();
            String filePath = getFilePathFromUri(this, uri);
            if (filePath != null) {
                imageFile = new File(filePath);
                Glide.with(this)
                        .load(imageFile)
                        .placeholder(R.drawable.timeline_placeholder)
                        .into(imgvCamera);
            } else {
                Toast.makeText(this, "사진을 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_upload:
                UploadMissionItemActivity.this.upload();
                return true;
            default:
                return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.upload, menu);
        return true;
    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }

    private void upload() {
        if(etxtMessage.getText().toString().isEmpty()){
            Toast.makeText(this, "글을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        RequestBody requestBody2 = RequestBody.create(MediaType.parse("text/plain"), etxtMessage.getText().toString());
        Call<Boolean> uploadMissionItems;
        if(imageFile!=null) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
            uploadMissionItems = AppController.getInstance().getDataManager().getMissionItemService().postMissionItem(memberMission.getMemberMissionId(), requestBody, requestBody2);
        }else{
            uploadMissionItems = AppController.getInstance().getDataManager().getMissionItemService().postMissionItem(memberMission.getMemberMissionId(), requestBody2);
        }
        uploadMissionItems.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.isSuccess()) {
                    showDoneDialog();
                }else{

                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.d("asd", "asd");

            }
        });
    }

    private void showDoneDialog() {
        new MaterialDialog.Builder(this)
                .title("미션 수행 완료")
                .content("내 마니또에게 미션을 수행하였습니다.")
                .positiveText("확인")
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        setResult(RESULT_OK);
                        finish();
                    }
                })
                .show();
    }

    int channel_id;
    MemberMission memberMission;
    Toolbar toolbar;
    RelativeLayout containerCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_item);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitle("미션 수행하기");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        memberMission = (MemberMission) getIntent().getSerializableExtra("memberMission");
        channel_id = getIntent().getIntExtra("channel_id", 0);
        if(channel_id == 0){
            finish();
        }
        txtvTo = (TextView) findViewById(R.id.txtvTo);
        txtvContent = (TextView) findViewById(R.id.txtvContent);
        txtvMission = (TextView) findViewById(R.id.txtvMission);
        imgvPortrait = (ImageView) findViewById(R.id.imgvPortrait);
        etxtMessage = (EditText) findViewById(R.id.etxtMessage);
        imgvCamera = (ImageView) findViewById(R.id.imgvCamera);
        containerCamera = (RelativeLayout) findViewById(R.id.containerCamera);

        txtvMission.setText(memberMission.getMissionName());
        txtvContent.setText(memberMission.getMissionInfo());
        AppController.getInstance().getDataManager().getUserService().getManito(channel_id).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccess()) {
                    Glide.with(UploadMissionItemActivity.this)
                            .load(response.body().getUserProfileUrl())
                            .centerCrop()
                            .bitmapTransform(new CropCircleTransformation(UploadMissionItemActivity.this))
                            .placeholder(R.drawable.ic_account_circle_grey600_36dp)
                            .into(imgvPortrait);
                    txtvTo.setText(response.body().getUserName());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("asd", "asd");
            }
        });

        imgvCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasPermission()) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                } else {
                    requestPermission();
                }
            }
        });
        containerCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasPermission()) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                } else {
                    requestPermission();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (hasPermission()) {

        }
        else {
            requestPermission();
        }
    }

    File imageFile;
    int PICK_IMAGE_REQUEST = 1;


    private boolean hasPermission() {
        int storage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (storage == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        else {
            return false;
        }
    }
    public void requestPermission() {
        // CAMERA, WRITE_EXTERNAL_STORAGE 권한 요청
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
    }

    static final int  REQUEST_CAMERA_PERMISSION = 33;


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION:
                if (grantResults.length >= 1
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // do nothing (camera will be initialized at onResume)
                } else {
                    Toast.makeText(AppController.getInstance().getApplicationContext(), "겔러리를 실행할 수 있는 권한이 없습니다.", Toast.LENGTH_LONG).show();
                    finish();
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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        imgvCamera.setImageBitmap(null);
    }
}
