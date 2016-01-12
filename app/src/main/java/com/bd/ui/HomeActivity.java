package com.bd.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import com.bd.R;
import com.bd.adapters.TweetAdapter;
import com.bd.presenters.HomePresenter;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

public class HomeActivity extends Activity {
    private HomePresenter mHomePresenter;
    private TweetAdapter mTweetAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);

        mHomePresenter = new HomePresenter();
        mHomePresenter.initPresenter(this);

        initView();
        mHomePresenter.onShowTweetsClicked();

    }

    private void initView() {
        RecyclerView rvHomeTimelineList = (RecyclerView) findViewById(R.id.rv_home_timeline_list);
        rvHomeTimelineList.setHasFixedSize(true);
        Context context = getApplicationContext();
        RecyclerView.LayoutManager rvManager = new LinearLayoutManager(context, LinearLayout.VERTICAL, false);
        rvHomeTimelineList.setLayoutManager(rvManager);
        mTweetAdapter = new TweetAdapter(getApplicationContext());
        rvHomeTimelineList.setAdapter(mTweetAdapter);

    }

    public void setHomeTimelineList(List<Tweet> tweetList) {
        mTweetAdapter.updateData(tweetList);
        mTweetAdapter.notifyDataSetChanged();
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
    }
}
