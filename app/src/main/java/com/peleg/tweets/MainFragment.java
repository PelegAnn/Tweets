package com.peleg.tweets;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.peleg.tweetslibrary.model.Status;
import com.peleg.tweetslibrary.model.TweetResponse;
import com.peleg.tweetslibrary.rest.TweetsApi;
import com.peleg.tweetslibrary.rest.TweetsApiInterface;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hannypeleg on 5/18/17.
 */

public class MainFragment extends Fragment {

    private static final String TAG = MainFragment.class.getName();
    private TextInputEditText hashtagET;
    private Button search;

    private List<Tweet> tweets;


    public static MainFragment newInstance() {
        return new MainFragment();
    }

    OnNewTweetsListener mCallback;

    // Container Activity must implement this interface
    public interface OnNewTweetsListener {
        void onNewTweets(List<Tweet> items);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        hashtagET = (TextInputEditText) view.findViewById(R.id.search_text);
        hashtagET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    searchTweets();
                    return true;
                }
                return false;
            }
        });
        search = (Button) view.findViewById(R.id.search_btn);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(getActivity());
                searchTweets();
            }
        });
        return view;
    }

    private void searchTweets() {
        TweetsApiInterface apiService = TweetsApi.getClient().create(TweetsApiInterface.class);
        String hashtag = validateInput();
        if (hashtag != null) {
            Call<TweetResponse> call = apiService.getTweets(hashtag);
            call.enqueue(new Callback<TweetResponse>() {
                @Override
                public void onResponse(Call<TweetResponse> call, Response<TweetResponse> response) {
                    if (response.code() == 200) {
                        tweets = new ArrayList<>();
                        for (Status s : response.body().getStatuses()) {
                            Tweet tweet = new Tweet();
                            tweet.setCreatedAt(s.getCreatedAt());
                            tweet.setText(s.getText());
                            tweet.setScreenName(s.getUser().getScreenName());
                            tweet.setMediaImgUrl(s.getUser().getProfileImageUrlHttps().replace("_normal", "_400x400"));

                            tweets.add(tweet);
                        }

                        mCallback.onNewTweets(tweets);
                    }
                }

                @Override
                public void onFailure(Call<TweetResponse> call, Throwable t) {
                    Log.e(TAG, t.toString());
                }
            });
        }
    }

    private String validateInput() {
        String hashtag = null;
        String hashtagTerm = hashtagET.getText().toString();
        if(hashtagTerm.isEmpty())
            return null;
        if(hashtagTerm.charAt(0)!='#') {
            hashtag = '#' + hashtagTerm;
        } else {
            hashtag = hashtagTerm;
        }
        try {
            hashtag = URLEncoder.encode(hashtag,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return hashtag;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity = getActivity();
        try {
            mCallback = (OnNewTweetsListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnNewTweetsListener");
        }
    }


    private static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }
}
