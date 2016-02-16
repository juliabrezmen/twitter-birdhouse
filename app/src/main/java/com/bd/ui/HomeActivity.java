package com.bd.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import com.bd.R;
import com.bd.adapters.TweetAdapter;
import com.bd.database.TweetData;
import com.bd.presenters.HomePresenter;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private HomePresenter presenter;
    private TweetAdapter tweetAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

        presenter = new HomePresenter(this);
        presenter.initPresenter();
    }

    @Override
    protected void onDestroy() {
        presenter.onActivityDestroy();
        super.onDestroy();
    }

    private void initView() {
        setContentView(R.layout.home_layout);

        tweetAdapter = new TweetAdapter(getApplicationContext(), new TweetAdapter.Listener() {
            @Override
            public void onFavouriteClicked(TweetData tweet) {
                presenter.onFavouriteClicked(tweet);
            }

            @Override
            public void onRetweetClicked(TweetData tweet) {
                presenter.onRetweetClicked(tweet);
            }
        });
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.onPullToRefresh();
            }
        });

        RecyclerView.LayoutManager rvManager = new LinearLayoutManager(getApplicationContext(), LinearLayout.VERTICAL, false);
        RecyclerView rvHomeTimelineList = (RecyclerView) findViewById(R.id.rv_home_timeline_list);
        rvHomeTimelineList.setHasFixedSize(true);
        rvHomeTimelineList.setLayoutManager(rvManager);
        rvHomeTimelineList.setAdapter(tweetAdapter);

        FloatingActionButton btnNewTweet = (FloatingActionButton) findViewById(R.id.btn_new_tweet);
        btnNewTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onNewTweetClicked();
            }
        });
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
