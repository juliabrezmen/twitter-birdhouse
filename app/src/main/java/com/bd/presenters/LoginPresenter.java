package com.bd.presenters;

import android.app.Activity;
import com.bd.ui.HomeActivity;

public class LoginPresenter {
    private Activity mActivity;

    public void initPresenter(Activity activity) {
        mActivity = activity;
    }

    public void onSuccessLogin() {
        HomeActivity.start(mActivity);
    }
}
