package com.gelato.gelato;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.gelato.gelato.cores.AppController;
import com.gelato.gelato.cores.CustomAppCompatActivity;
import com.gelato.gelato.cores.network.DefaultListener;
import com.gelato.gelato.models.Channel;
import com.gelato.gelato.tools.RippleView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends CustomAppCompatActivity {
    private CallbackManager mCallbackManager;
    RippleView btnFacebook;
    LoginButton loginButton;

    int adminCount = 0;


    private void registerByFacebook(LoginResult loginResult) {
        final String accessToken = loginResult.getAccessToken().getToken();
        final String id = loginResult.getAccessToken().getUserId();
        Bundle params = new Bundle();
        params.putString("fields", "id,first_name,gender,picture");
        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                String name = object.optString("first_name");
                String gender = object.optString("gender");
                String picture = "";
                try {
                    picture = object.getJSONObject("picture").getJSONObject("data").optString("url");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                AppController.getInstance().getDataManager().getUserService().register(id, accessToken, name, picture, gender).enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if(response.isSuccess()) {
                            AppController.getInstance().getLoginManager().performFacebookLogin(id, new DefaultListener() {
                                @Override
                                public void onSuccess() {
                                    AppController.getInstance().getLoginManager().loadUser(new DefaultListener() {
                                        @Override
                                        public void onSuccess() {

                                            loadChannel();
                                        }

                                        @Override
                                        public void onFailure() {

                                        }
                                    });
                                }

                                @Override
                                public void onFailure() {
                                    Log.d("asd","asd");
                                }
                            });
                        }else{
                            Log.d("asd","asd");
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        Log.d("asd","asd");
                    }
                });
            }
        });
        request.setParameters(params);
        request.executeAsync();
    }

    private FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(final LoginResult loginResult) {
            final String id = loginResult.getAccessToken().getUserId();
            AppController.getInstance().getLoginManager().performFacebookLogin(
                    id,
                    new DefaultListener() {
                        @Override
                        public void onSuccess() {
                            AppController.getInstance().getLoginManager().loadUser(new DefaultListener() {
                                @Override
                                public void onSuccess() {
                                    loadChannel();
                                }

                                @Override
                                public void onFailure() {

                                }
                            });

                        }

                        @Override
                        public void onFailure() {
                            registerByFacebook(loginResult);
                        }
                    });
        }

        @Override
        public void onCancel() {
            Log.d("asd","Asdsa");
        }

        @Override
        public void onError(FacebookException e) {
            Log.d("asd","Asdsa");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_login);
        mCallbackManager = CallbackManager.Factory.create();
        btnFacebook = (RippleView) findViewById(R.id.btnFacebook);
        btnFacebook.setOnClickListener(this);
        loginButton = (LoginButton) findViewById(R.id.btnFacebookLogin);
        loginButton.setReadPermissions(Arrays.asList("public_profile"));
        loginButton.registerCallback(mCallbackManager, mCallback);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btnFacebook:
                loginButton.performClick();
                break;
        }
    }

    private void loadChannel() {
        Call<List<Channel>> channelCall = AppController.getInstance().getDataManager().getChannelService().getChannels();
        channelCall.enqueue(new Callback<List<Channel>>() {
            @Override
            public void onResponse(Call<List<Channel>> call, Response<List<Channel>> response) {
                if (response.isSuccess()) {
                    if(response.body().size()==0)
                    {
                        Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        ArrayList<Channel> channels = new ArrayList<Channel>(response.body());
                        AppController.getInstance().getLocalStore().storeChannel(channels);
                        Intent intent = new Intent(getApplicationContext(), ChannelActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(0, 0);
                    }
                } else {
                    Intent intent = new Intent(getApplicationContext(), ChannelActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(0, 0);
                }
            }

            @Override
            public void onFailure(Call<List<Channel>> call, Throwable t) {

            }
        });
    }
}
