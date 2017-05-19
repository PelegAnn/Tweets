package com.peleg.tweetslibrary.rest;

import com.peleg.tweetslibrary.model.OauthResponse;
import com.peleg.tweetslibrary.model.TweetResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by hannypeleg on 5/17/17.
 */

public interface TweetsApiInterface {


    //https://api.twitter.com/1.1/search/tweets.json
    @GET("1.1/search/tweets.json")
    Call<TweetResponse> getTweets(@Query("q") String hashtag);

    @POST("oauth2/token")
    @Headers({"Cache-Control: max-age=640000", "User-Agent: My-App-Name"})
    @FormUrlEncoded
    Call<OauthResponse> ObtainABearer(@Field("grant_type") String body);



}
