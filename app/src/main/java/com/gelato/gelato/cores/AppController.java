package com.gelato.gelato.cores;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.gelato.gelato.cores.database.DatabaseHelper;
import com.gelato.gelato.cores.database.DatabaseManager;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by desmond on 13/7/14.
 */

//Local store, User singleton
public class AppController extends MultiDexApplication {
    // implements TrackerProvider {

    public static final String TAG = AppController.class.getSimpleName();
    private LoginManager mLoginManager;
    private DatabaseManager mDatabaseManager;
    private DatabaseHelper mDatabaseHelper;
    private DataManager mDataManager;
    private LocalStore mLocalStore;
    private Retrofit mRetrofit;
    public Boolean DEBUG;
    //for GA

    //private Tracker mTracker;


    public boolean isNetworkAvailable() {
        ConnectivityManager connectivity = (ConnectivityManager) getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {

        @Override
        public Response intercept(Chain chain) throws IOException {

            String loginToken = AppController.getInstance().getLoginManager().getLoginToken();
            if (loginToken != null) {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .addHeader("authorization", String.format("%s", loginToken))
                        .build();
                return chain.proceed(request);
            }

            Response originalResponse = chain.proceed(chain.request());
            Response.Builder builder = originalResponse.newBuilder();

            if (getInstance().isNetworkAvailable()) {
                int maxAge = 60; // read from cache for 1 minute
                //return builder.addHeader("Cache-Control", "public, max-age=" + maxAge).build();
                return builder.build();
            } else {
                int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                return builder.build();
                //return builder.addHeader("Cache-Control", "public, only-if-cached, max-stale=" + maxStale).build();
            }
        }
    };

    public Retrofit getRetrofit() {
        if (mRetrofit == null) {
            File httpCacheDirectory = new File(getInstance().getCacheDir(), "responses");
            int cacheSize = 10 * 1024 * 1024; // 10 MiB
            Cache cache = new Cache(httpCacheDirectory, cacheSize);
            OkHttpClient okClient = null;
            okClient = new OkHttpClient.Builder()
                    .cache(cache)
                    .addInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
                    .build();
            ObjectMapper objectMapper = new ObjectMapper();
            // register joda module, to parse DateTime field
            objectMapper.registerModule(new JodaModule());
            // http://stackoverflow.com/questions/10519265/jackson-overcoming-underscores-in-favor-of-camel-case
            objectMapper.setPropertyNamingStrategy(
                    PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
            JacksonConverterFactory jacksonConverterFactory = JacksonConverterFactory.create(objectMapper);
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(AppController.getInstance().getLocalStore().getBaseUrl())
                    .client(okClient)
                    .addConverterFactory(jacksonConverterFactory)
                    .build();
        }
        return mRetrofit;
    }

    private static AppController mInstance;

    public DatabaseManager getDatabaseManager() {
        if (mDatabaseManager == null) {
            mDatabaseManager = new DatabaseManager();
        }
        return mDatabaseManager;
    }

    public DataManager getDataManager() {
        if (mDataManager == null) {
            mDataManager = new DataManager();
        }
        return mDataManager;
    }

    public DatabaseHelper getDatabaseHelper() {
        if (mDatabaseHelper == null) {
            mDatabaseHelper = new DatabaseHelper(getApplicationContext());
        }
        return mDatabaseHelper;
    }

    @Override
    public void onCreate() {
        Log.d("Core", "AppController onCreate called");
        super.onCreate();
        mInstance = this;
        DEBUG = isDebuggable(this);
    }

    private String getStackTrace(Throwable th) {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);

        Throwable cause = th;

        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }

        final String stacktraceAsString = result.toString();
        printWriter.close();

        return stacktraceAsString;
    }


    public static synchronized AppController getInstance() {
        return mInstance;
    }

    private boolean isDebuggable(Context context) {
        boolean debuggable = false;

        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo appinfo = pm.getApplicationInfo(context.getPackageName(), 0);
            debuggable = (0 != (appinfo.flags & ApplicationInfo.FLAG_DEBUGGABLE));
        } catch (PackageManager.NameNotFoundException e) {
        /* debuggable variable will remain false */
        }

        return debuggable;
    }

    public LocalStore getLocalStore() {
        if (mLocalStore == null) {
            mLocalStore = new LocalStore(getApplicationContext());
        }

        return mLocalStore;
    }

    public LoginManager getLoginManager() {
        if (mLoginManager == null) {
            mLoginManager = new LoginManager();
        }
        return mLoginManager;
    }
    /*
     *
     */

    //@SendEvent(category = "Error", action = "%1$s", label = "%2$s : %3$s")
    public void SendError(String errorName, String activityName, String functionName) {

    }

    //@SendScreenView(screenName = "%1$s")
    public void sendScreenChange(String screenName) {

    }

    //@SendEvent(category = "%1$s", action = "%2$s", label = "%3$s")
    public void sendEvent(String category, String action, String label) {
    }

    //@SendEvent(category = "%1$s", action = "%2$s", label = "%3$s", valueBuilder = CountValueBuilder.class)
    public void sendEvent(String category, String action, String label, int value) {
    }
}