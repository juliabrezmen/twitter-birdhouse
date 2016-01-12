package com.bd.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bd.R;
import com.bd.utils.DateUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {
    private List<Tweet> mTweetList;
    private Context context;

    public TweetAdapter(Context context) {
        mTweetList = new ArrayList<>();
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tweet_item, viewGroup, false);
        return new ViewHolder((LinearLayout) v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Tweet tweet = mTweetList.get(i);
        User user = tweet.user;

        viewHolder.txtFullName.setText(user.name);
        viewHolder.txtNickName.setText("@" + user.screenName);
        Picasso.with(context).load(user.profileImageUrl).transform(new CircleTransform()).into(viewHolder.imgAvatar);

        viewHolder.txtTweetText.setText(tweet.text);
        viewHolder.txtFavouriteCount.setText(String.valueOf(tweet.favoriteCount));
        viewHolder.txtRetweetCount.setText(String.valueOf(tweet.retweetCount));
        setFavourited(viewHolder, tweet);
        setRetweeted(viewHolder, tweet);

        setTweetDate(viewHolder, tweet);


    }

    private void setTweetDate(ViewHolder viewHolder, Tweet tweet) {
        String createdAt = tweet.createdAt;
        SimpleDateFormat parser = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy");

        try {
            Date convertedDate = parser.parse(createdAt);
            String shortDate = DateUtils.createShortDate(convertedDate);

            viewHolder.txtCreatedAt.setText(shortDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void setRetweeted(ViewHolder viewHolder, Tweet tweet) {
        if (tweet.retweeted) {
            viewHolder.imgRetweeted.setImageResource(R.drawable.ic_retweet_white);
            viewHolder.txtRetweetCount.setTextColor(context.getResources().getColor(R.color.gray_200));
        } else {
            viewHolder.imgRetweeted.setImageResource(R.drawable.ic_retweet_gray);
            viewHolder.txtRetweetCount.setTextColor(context.getResources().getColor(R.color.gray_600));
        }
    }

    private void setFavourited(ViewHolder viewHolder, Tweet tweet) {
        if (tweet.favorited) {
            viewHolder.imgFavorited.setImageResource(R.drawable.ic_heart_red);
            viewHolder.txtFavouriteCount.setTextColor(context.getResources().getColor(R.color.red));
        } else {
            viewHolder.imgFavorited.setImageResource(R.drawable.ic_heart_gray);
            viewHolder.txtFavouriteCount.setTextColor(context.getResources().getColor(R.color.gray_600));
        }
    }

    @Override
    public int getItemCount() {
        return mTweetList.size();
    }

    public void updateData(List<Tweet> tweetList) {
        if (mTweetList != null) {
            this.mTweetList = tweetList;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTweetText;
        TextView txtCreatedAt;
        TextView txtFullName;
        TextView txtNickName;
        TextView txtFavouriteCount;
        TextView txtRetweetCount;
        ImageView imgAvatar;
        ImageView imgRetweeted;
        ImageView imgFavorited;

        public ViewHolder(LinearLayout itemView) {
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
        }
    }

    private class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }
}
