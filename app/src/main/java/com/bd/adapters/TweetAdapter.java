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
import com.bd.database.TagData;
import com.bd.database.TweetData;
import com.bd.imageloader.CircleTransform;
import com.bd.utils.DateUtils;
import com.bd.utils.L;
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

    public TweetAdapter(Context context) {
        this.tweetDataList = new ArrayList<>();
        this.context = context;
        this.hashTagColor = context.getResources().getColor(R.color.blue);
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
        //highlightUsers()
        viewHolder.txtTweetText.setText(spannable);

        viewHolder.txtFavouriteCount.setText(String.valueOf(tweet.getFavoriteCount()));
        viewHolder.txtRetweetCount.setText(String.valueOf(tweet.getRetweetCount()));
        viewHolder.txtCreatedAt.setText(DateUtils.createShortDate(tweet.getDate()));
        initFavoriteView(viewHolder, tweet);
        initRetweetView(viewHolder, tweet);

        String avatarUrl = UrlUtils.createOriginImageUrl(tweet.getAvatarUrl());
        if (!TextUtils.isEmpty(avatarUrl)) {
            Picasso.with(context).load(avatarUrl).transform(new CircleTransform()).into(viewHolder.imgAvatar);
        }

        String tweetImageUrl = tweet.getTweetImageUrl();
        if (tweetImageUrl != null) {
            viewHolder.imgTweet.setVisibility(View.VISIBLE);
            Picasso.with(context).load(tweetImageUrl).into(viewHolder.imgTweet);
        } else {
            viewHolder.imgTweet.setVisibility(View.GONE);
        }
    }

    private void highlightTags(TweetData tweet, Spannable spannable) {
        RealmList<TagData> hashtagList = tweet.getHashtagList();
        if (hashtagList != null && !hashtagList.isEmpty()) {
            for (TagData tagData : hashtagList) {
                L.i("TAG: " + tagData.getTag());
                SpannableUtils.color(spannable, "#" + tagData.getTag(), hashTagColor);
            }
        }
    }

    private void initRetweetView(ViewHolder viewHolder, TweetData tweet) {
        if (tweet.isRetweeted()) {
            viewHolder.imgRetweeted.setImageResource(R.drawable.ic_retweet_white);
            viewHolder.txtRetweetCount.setTextColor(context.getResources().getColor(R.color.gray_200));
        } else {
            viewHolder.imgRetweeted.setImageResource(R.drawable.ic_retweet_gray);
            viewHolder.txtRetweetCount.setTextColor(context.getResources().getColor(R.color.gray_600));
        }
    }

    private void initFavoriteView(ViewHolder viewHolder, TweetData tweet) {
        if (tweet.isFavorited()) {
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
