package com.bd.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import com.bd.R;
import com.bd.adapters.TweetAdapter;
import com.bd.database.TweetData;
import com.bd.presenters.HomePresenter;

import java.util.List;

public class HomeActivity extends Activity {

    private HomePresenter homePresenter;
    private TweetAdapter tweetAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

        homePresenter = new HomePresenter(this);
        homePresenter.initPresenter();
    }

    private void initView() {
        setContentView(R.layout.home_layout);

        tweetAdapter = new TweetAdapter(getApplicationContext());
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                homePresenter.onPullToRefresh();
            }
        });

        RecyclerView.LayoutManager rvManager = new LinearLayoutManager(getApplicationContext(), LinearLayout.VERTICAL, false);
        RecyclerView rvHomeTimelineList = (RecyclerView) findViewById(R.id.rv_home_timeline_list);
        rvHomeTimelineList.setHasFixedSize(true);
        rvHomeTimelineList.setLayoutManager(rvManager);
        rvHomeTimelineList.setAdapter(tweetAdapter);
    }

    public void hidePullToRefresh() {
        swipeRefreshLayout.setRefreshing(false);
    }

    public void setHomeTimelineList(@NonNull List<TweetData> tweetList) {
        tweetAdapter.updateData(tweetList);
        tweetAdapter.notifyDataSetChanged();
    }

    public static void start(@NonNull Activity activity) {
        Intent intent = new Intent(activity, HomeActivity.class);
        activity.startActivity(intent);
    }
}
