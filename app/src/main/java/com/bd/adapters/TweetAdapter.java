package com.bd.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bd.R;
import com.bd.database.RealmString;
import com.bd.database.TweetData;
import com.bd.imageloader.CircleTransform;
import com.bd.utils.DateUtils;
import com.bd.utils.SpannableUtils;
import com.bd.utils.UrlUtils;
import com.squareup.picasso.Picasso;
import io.realm.RealmList;

import java.util.ArrayList;
import java.util.List;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {
    private List<TweetData> tweetDataList;
    private Context context;
    private int hashTagColor;
    private int userMentionsColor;
    private int urlColor;
    private Listener listener;

    public TweetAdapter(Context context, Listener listener) {
        this.tweetDataList = new ArrayList<>();
        this.context = context;
        this.listener = listener;
        this.hashTagColor = context.getResources().getColor(R.color.blue);
        this.userMentionsColor = context.getResources().getColor(R.color.orange);
        this.urlColor = context.getResources().getColor(R.color.blue);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tweet_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        TweetData tweet = tweetDataList.get(position);

        viewHolder.txtFullName.setText(tweet.getFullName());
        viewHolder.txtNickName.setText(tweet.getNickName());

        String tweetText = tweet.getText();
        Spannable spannable = new SpannableStringBuilder(tweetText);
        highlightTags(tweet, spannable);
        highlightUsers(tweet, spannable);
        highlightUrls(tweet, spannable);
        viewHolder.txtTweetText.setText(spannable);

        viewHolder.txtFavouriteCount.setText(String.valueOf(tweet.getFavoriteCount()));
        viewHolder.txtRetweetCount.setText(String.valueOf(tweet.getRetweetCount()));
        viewHolder.txtCreatedAt.setText(DateUtils.createShortDate(tweet.getDate()));

        initFavoriteView(viewHolder, tweet);
        initRetweetView(viewHolder, tweet);
        initAvatarView(viewHolder, tweet);
        initTweetImage(viewHolder, tweet);
    }

    private void initAvatarView(ViewHolder viewHolder, TweetData tweet) {
        String avatarUrl = UrlUtils.createOriginImageUrl(tweet.getAvatarUrl());
        if (!TextUtils.isEmpty(avatarUrl)) {
            Picasso.with(context).load(avatarUrl).transform(new CircleTransform()).into(viewHolder.imgAvatar);
        }
    }

    private void initTweetImage(ViewHolder viewHolder, TweetData tweet) {
        String tweetImageUrl = tweet.getTweetImageUrl();
        if (tweetImageUrl != null) {
            viewHolder.imgTweet.setVisibility(View.VISIBLE);
            Picasso.with(context).load(tweetImageUrl).into(viewHolder.imgTweet);
        } else {
            viewHolder.imgTweet.setVisibility(View.GONE);
        }
    }

    private void highlightUrls(TweetData tweet, Spannable spannable) {
        RealmList<RealmString> urlList = tweet.getUrlList();
        if (urlList != null && !urlList.isEmpty()) {
            for (RealmString url : urlList) {
                SpannableUtils.color(spannable, url.getString(), urlColor);
            }
        }
    }

    private void highlightUsers(TweetData tweet, Spannable spannable) {
        RealmList<RealmString> userMentionList = tweet.getUserMentionsList();
        if (userMentionList != null && !userMentionList.isEmpty()) {
            for (RealmString user : userMentionList) {
                SpannableUtils.color(spannable, "@" + user.getString(), userMentionsColor);
            }
        }
    }

    private void highlightTags(TweetData tweet, Spannable spannable) {
        RealmList<RealmString> hashtagList = tweet.getHashtagList();
        if (hashtagList != null && !hashtagList.isEmpty()) {
            for (RealmString tagData : hashtagList) {
                SpannableUtils.color(spannable, "#" + tagData.getString(), hashTagColor);
            }
        }
    }

    private void initRetweetView(ViewHolder viewHolder, final TweetData tweet) {
        if (tweet.isRetweeted()) {
            viewHolder.imgRetweeted.setImageResource(R.drawable.ic_retweet_white);
            viewHolder.txtRetweetCount.setTextColor(context.getResources().getColor(R.color.gray_200));
        } else {
            viewHolder.imgRetweeted.setImageResource(R.drawable.ic_retweet_gray);
            viewHolder.txtRetweetCount.setTextColor(context.getResources().getColor(R.color.gray_600));
        }
        viewHolder.imgRetweeted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRetweetClicked(tweet);
            }
        });
    }

    private void initFavoriteView(final ViewHolder viewHolder, final TweetData tweet) {
        setFavourite(viewHolder, tweet.isFavorited());
        viewHolder.imgFavorited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFavourite(viewHolder, !tweet.isFavorited());

                int count = Integer.parseInt(viewHolder.txtFavouriteCount.getText().toString());
                if (tweet.isFavorited()) {
                    viewHolder.txtFavouriteCount.setText(String.valueOf(count - 1));
                } else {
                    viewHolder.txtFavouriteCount.setText(String.valueOf(count + 1));
                }

                listener.onFavouriteClicked(tweet);
            }
        });
    }

    private void setFavourite(ViewHolder viewHolder, boolean isFavorited) {
        if (isFavorited) {
            viewHolder.imgFavorited.setImageResource(R.drawable.ic_heart_red);
            viewHolder.txtFavouriteCount.setTextColor(context.getResources().getColor(R.color.red));
        } else {
            viewHolder.imgFavorited.setImageResource(R.drawable.ic_heart_gray);
            viewHolder.txtFavouriteCount.setTextColor(context.getResources().getColor(R.color.gray_600));
        }
    }

    @Override
    public int getItemCount() {
        return tweetDataList.size();
    }

    public void updateData(List<TweetData> tweetList) {
        if (tweetDataList != null) {
            this.tweetDataList = tweetList;
        }
    }

    public interface Listener {
        void onFavouriteClicked(TweetData tweet);

        void onRetweetClicked(TweetData tweet);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTweetText;
        TextView txtCreatedAt;
        TextView txtFullName;
        TextView txtNickName;
        TextView txtFavouriteCount;
        TextView txtRetweetCount;
        ImageView imgAvatar;
        ImageView imgRetweeted;
        ImageView imgFavorited;
        ImageView imgTweet;

        public ViewHolder(View itemView) {
            super(itemView);
            txtTweetText = (TextView) itemView.findViewById(R.id.txt_tweet_text);
            txtCreatedAt = (TextView) itemView.findViewById(R.id.txt_createdAt);
            txtFullName = (TextView) itemView.findViewById(R.id.txt_full_name);
            txtNickName = (TextView) itemView.findViewById(R.id.txt_nick_name);
            txtFavouriteCount = (TextView) itemView.findViewById(R.id.txt_favourite_count);
            txtRetweetCount = (TextView) itemView.findViewById(R.id.txt_retweet_count);
            imgAvatar = (ImageView) itemView.findViewById(R.id.img_avatar);
            imgRetweeted = (ImageView) itemView.findViewById(R.id.img_retweeted);
            imgFavorited = (ImageView) itemView.findViewById(R.id.img_favorited);
            imgTweet = (ImageView) itemView.findViewById(R.id.img_tweet);
        }
    }


}
