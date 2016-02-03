package com.bd.presenters;

import com.bd.managers.PreferencesManager;
import com.bd.ui.HomeActivity;
import com.bd.ui.LoginActivity;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterSession;

public class LoginPresenter {
    private LoginActivity activity;

    public void initPresenter(LoginActivity activity) {
        this.activity = activity;
    }

    public void onSuccessLogin(Result<TwitterSession> result) {
        PreferencesManager.saveUserName(result.data.getUserName(), activity.getApplicationContext());
        HomeActivity.start(activity);
    }
}
