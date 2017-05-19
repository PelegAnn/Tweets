package com.peleg.tweets;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by hannypeleg on 5/18/17.
 */

class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    private static final String TAG = TweetsAdapter.class.getName();
    private List<Tweet> mItems;
    private Context mContext;

    private TweetsAdapter.ViewHolder mHolder;

    public TweetsAdapter(List<Tweet> mItems, Context mContext) {
        this.mItems = mItems;
        this.mContext = mContext;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public TextView mUserName, mText;
        public ImageView mImage;
        public CardView mCardView;




        public ViewHolder(View itemView) {
            super(itemView);

            mHolder = this;

            mCardView = (CardView) itemView.findViewById(R.id.card_view);
            mImage = (ImageView) itemView.findViewById(R.id.item_image);
            mUserName = (TextView) itemView.findViewById(R.id.user_name);
            mText = (TextView) itemView.findViewById(R.id.text);

            mImage.setOnClickListener(this);
            mCardView.setOnClickListener(this);
            mCardView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.e(TAG," OnClick! " + view.getId());

        }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        View itemView = layoutInflater.inflate(R.layout.tweet_view, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Tweet tweet = mItems.get(position);

        TextView nameView = holder.mUserName;
        nameView.setText(tweet.getScreenName());

        TextView textView = holder.mText;
        textView.setText(tweet.getText());

        String path = tweet.getMediaImgUrl();

        ImageView itemImageView = holder.mImage;
        if(path != null) {
            Picasso.with(mContext).load(path)
                    .placeholder(R.drawable.placeholder)
                    .into(itemImageView);
        } else {
            itemImageView.setImageResource(R.drawable.placeholder);
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    public void addAll(List<Tweet> tweets) {
        if(mItems!=null) {
            mItems.clear();
            mItems.addAll(tweets);
        } else {
            mItems = tweets;
        }
        notifyDataSetChanged();
    }
}
