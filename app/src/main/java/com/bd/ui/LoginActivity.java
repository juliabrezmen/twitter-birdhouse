package com.bd.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.bd.R;
import com.bd.presenters.LoginPresenter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

public class LoginActivity extends Activity {
    private TwitterLoginButton mLoginButton;
    private LoginPresenter mSignInPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        initView();

        mSignInPresenter = new LoginPresenter();
        mSignInPresenter.initPresenter(this);
    }

    private void initView() {
        mLoginButton = (TwitterLoginButton) findViewById(R.id.login_button);
        mLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // Do something with result, which provides a TwitterSession for making API calls
                mSignInPresenter.onSuccessLogin();
            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("app", "in onActivityResult");
        // Pass the activity result to the login button.
        mLoginButton.onActivityResult(requestCode, resultCode, data);
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
}
