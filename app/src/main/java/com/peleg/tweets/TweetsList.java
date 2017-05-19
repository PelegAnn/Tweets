package com.peleg.tweets;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hannypeleg on 5/18/17.
 */

public class TweetsList {
    private List<Tweet> mItems;
    private static TweetsList mInstance;

    private TweetsList() {
        if(mItems == null) {
            mItems = new ArrayList<>();
        }
    }

    public static TweetsList getInstance() {
        if(mInstance == null)
            mInstance = new TweetsList();
        return mInstance;
    }

    public List<Tweet> getList() {
        return mItems;
    }

    public void setItems(List<Tweet> mItems) {
        this.mItems = mItems;
    }
}