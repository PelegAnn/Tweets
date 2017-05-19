package com.peleg.tweets;

import java.util.List;

/**
 * Created by hannypeleg on 5/18/17.
 */

public class Tweet {

    private String createdAt;
    private String text;
    private List<String> hashtags;
    private List<String> mediaUrls;
    private String screenName;
    private String mediaImgUrl;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getHashtags() {
        return hashtags;
    }

    public void setHashtags(List<String> hashtags) {
        this.hashtags = hashtags;
    }

    public List<String> getMediaUrls() {
        return mediaUrls;
    }

    public void setMediaUrls(List<String> mediaUrls) {
        this.mediaUrls = mediaUrls;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getMediaImgUrl() {
        return mediaImgUrl;
    }

    public void setMediaImgUrl(String mediaImgUrl) {
        this.mediaImgUrl = mediaImgUrl;
    }
}
