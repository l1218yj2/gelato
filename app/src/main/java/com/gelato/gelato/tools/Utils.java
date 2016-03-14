package com.gelato.gelato.tools;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.view.View;

import com.gelato.gelato.cores.AppController;

import org.joda.time.DateTime;
import org.joda.time.Period;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Mathpresso2 on 2015-08-21.
 */
public class Utils {

    public static String getHtmlData(String bodyHTML) {
        String head = "<head><style>img{max-width: 100%; width:auto; height: auto;}</style></head>";
        String new_string = "<html>" + head + "<body>" + bodyHTML + "</body></html>";
        return new_string;
    }

    public static Uri takeScreenshot(Activity activity) {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            // create bitmap screen capture
            View v1 = activity.getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            Uri uri = Uri.fromFile(imageFile);
            return uri;
        } catch (Throwable e) {
            // Several error may come out with file handling or OOM
            e.printStackTrace();
        }
        return null;
    }

    /*
     * Delete old folder
     */
    private static void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();
    }

    /*
     * Camera utils
     */
    public static File getDCIMDirectory() {
        return AppController.getInstance().getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DCIM);
    }

    public static File saveCanvasImage(View view) {

        view.setDrawingCacheEnabled(true);
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache(true);
        Bitmap b = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        try {
//            File sdCard = Environment.getExternalStorageDirectory();
//            File file = new File(sdCard, "image.jpg");
            File file = new File(getDCIMDirectory(), "image.jpg");
            FileOutputStream fos = new FileOutputStream(file);
            b.compress(Bitmap.CompressFormat.JPEG, 95, fos);
            return file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    //    public static void setBadgeCount(Context context, LayerDrawable icon, int count) {
//
//        BadgeDrawable badge;
//
//        // Reuse drawable if possible
//        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
//        if (reuse != null && reuse instanceof BadgeDrawable) {
//            badge = (BadgeDrawable) reuse;
//        } else {
//            badge = new BadgeDrawable(context);
//        }
//
//        badge.setCount(count);
//        icon.mutate();
//        icon.setDrawableByLayerId(R.id.ic_badge, badge);
//    }

    public static String getAlphabat(String args) throws Exception {

        String result;
        result = "";

        for (int i = 0; i < args.length(); i++) {
            char comVal = (char) (args.charAt(i)-0xAC00);

            if (comVal >= 0 && comVal <= 11172){
                // 한글일경우

                // 초성만 입력 했을 시엔 초성은 무시해서 List에 추가합니다.
                char uniVal = (char)comVal;

                // 유니코드 표에 맞추어 초성 중성 종성을 분리합니다..
                char cho = (char) ((((uniVal - (uniVal % 28)) / 28) / 21) + 0x1100);
                char jung = (char) ((((uniVal - (uniVal % 28)) / 28) % 21) + 0x1161);
                char jong = (char) ((uniVal % 28) + 0x11a7);

                if(cho!=4519){
                    System.out.print(cho+" ");
                    result += cho;
                }
                if(jung!=4519){
                    System.out.print(jung+" ");
                }
                if(jong!=4519){
                    System.out.print(jong+" ");
                }

            } else {
                // 한글이 아닐경우
                comVal = (char) (comVal+0xAC00);
                result += comVal;
                System.out.print(comVal+" ");
            }
        }
        return result;
    }


    // https://gist.github.com/YoungjaeKim/5563920
    /**
     * 친숙한 시간 표기법.
     * @param date 과거시간데이터.
     * @return 한국어로 친숙한 시간 설명 출력.
     * @exception IllegalArgumentException 미래값을 넣으면 {@link IllegalArgumentException} 발생.
     */
    public static String toFriendlyDateTimeString(DateTime date) {
        if (date == null) {
            return "";
        }
        DateTime now = new DateTime();
        if (date.isAfterNow())
            return "몇 초전";
        //throw new IllegalArgumentException("Future DateTime cannot be not handled.");
        Period period = new Period(date, now);

        if(date.isBefore(new DateTime().minusYears(1))) return period.getYears() + "년 전";
        if(date.isBefore(new DateTime().minusMonths(1))) return period.getMonths() + "달 전";
        if(date.isBefore(new DateTime().minusWeeks(1))) return period.getWeeks() + "주 전";
        if(date.isBefore(new DateTime().minusDays(1))){
            if(period.getDays() == 1)
                return "하루 전";
            if(period.getDays() == 2)
                return "이틀 전";
            return period.getDays() + "일 전";
        }
        if(date.isBefore(new DateTime().minusHours(1))) return period.getHours() + "시간 전";
        if(date.isBefore(new DateTime().minusMinutes(1))) return period.getMinutes() + "분 전";
        if(date.isBefore(new DateTime().minusSeconds(1))) return period.getSeconds() + "초 전";
        return "몇 초전";
    }

    public static String toFriendlyDateString(Date date) {
        Date now = new Date();
        long days = TimeUnit.DAYS.convert(now.getTime() - date.getTime(), TimeUnit.MILLISECONDS);
        if(days < 0)
            return "";
        if(days == 0)
            return "오늘";
        if(days == 1)
            return "어제";

        int years = now.getYear() - date.getYear();
        DateFormat format;
        if(years > 0)
            format = new SimpleDateFormat("yyyy년 M월 d일");
        else
            format = new SimpleDateFormat("M월 d일");
        return format.format(date);
    }
    public static String toNormalDateTimeString(DateTime dateTime) {
        return dateTime.toString("yyyy년 M월 d일 hh:mm a");
    }

    public static String getDeviceIdString() {
        return android.provider.Settings.Secure.getString(
                AppController.getInstance().getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);
    }

    public static String joinString(String[] strings, String separator) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0, il = strings.length; i < il; i++) {
            if (i > 0)
                sb.append(separator);
            sb.append(strings[i]);
        }
        return sb.toString();
    }
}
