package com.peleg.tweetslibrary.model;

/**
 * Created by hannypeleg on 5/18/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.peleg.tweetslibrary.model.sizes.Large;
import com.peleg.tweetslibrary.model.sizes.Medium_;
import com.peleg.tweetslibrary.model.sizes.Small;
import com.peleg.tweetslibrary.model.sizes.Thumb;

public class Sizes {

    @SerializedName("small")
    @Expose
    private Small small;
    @SerializedName("medium")
    @Expose
    private Medium_ medium;
    @SerializedName("large")
    @Expose
    private Large large;
    @SerializedName("thumb")
    @Expose
    private Thumb thumb;

    public Small getSmall() {
        return small;
    }

    public void setSmall(Small small) {
        this.small = small;
    }

    public Medium_ getMedium() {
        return medium;
    }

    public void setMedium(Medium_ medium) {
        this.medium = medium;
    }

    public Large getLarge() {
        return large;
    }

    public void setLarge(Large large) {
        this.large = large;
    }

    public Thumb getThumb() {
        return thumb;
    }

    public void setThumb(Thumb thumb) {
        this.thumb = thumb;
    }

}
