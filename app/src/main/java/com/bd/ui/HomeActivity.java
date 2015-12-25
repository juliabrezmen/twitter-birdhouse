package com.bd.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.bd.R;
import com.bd.presenters.HomePresenter;

public class HomeActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);

        HomePresenter mHomePresenter = new HomePresenter();
        mHomePresenter.initPresenter(this);

    }

    public static void start(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
    }
}
