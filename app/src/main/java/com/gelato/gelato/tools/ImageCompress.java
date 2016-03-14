package com.gelato.gelato.tools;

/**
 * Created by YongJae on 2016-02-28.
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

public class ImageCompress {

    public static File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        );
        return image;
    }

    protected static String getFilePathFromUri(Context context, Uri uri) {
        if (Build.VERSION.SDK_INT < 19) {
            return ImageFilePath.getPath(context, uri);
        } else {
            return getFilePathFromKitkatUri(context, uri);
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

    public static File scal(Uri fileUri, Context context) {
        String path = getFilePathFromUri(context, fileUri);
        File outputFile = new File(path);
        long fileSize = outputFile.length();
        //500kb
        final long fileMaxSize = 500 * 1024;
        if (fileSize >= fileMaxSize) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);
            int height = options.outHeight;
            int width = options.outWidth;

            double scale = Math.sqrt((float) fileSize / fileMaxSize);
            options.outHeight = (int) (height / scale);
            options.outWidth = (int) (width / scale);
            options.inSampleSize = (int) (scale + 0.5);
            options.inJustDecodeBounds = false;

            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            outputFile = new File(PhotoUtil.createImageFile().getPath());
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(outputFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
                fos.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Log.d("", "sss ok " + outputFile.length());
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            } else {
                File tempFile = outputFile;
                outputFile = new File(PhotoUtil.createImageFile().getPath());
                PhotoUtil.copyFileUsingFileChannels(tempFile, outputFile);
            }

        }
        return outputFile;

    }
}