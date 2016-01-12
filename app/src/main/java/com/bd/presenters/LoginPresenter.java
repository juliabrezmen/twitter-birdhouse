package com.bd.presenters;

import com.bd.ui.HomeActivity;
import com.bd.ui.LoginActivity;

public class LoginPresenter {
    private LoginActivity mActivity;

    public void initPresenter(LoginActivity activity) {
        mActivity = activity;
    }

    public void onSuccessLogin() {
        HomeActivity.start(mActivity);
    }
}
