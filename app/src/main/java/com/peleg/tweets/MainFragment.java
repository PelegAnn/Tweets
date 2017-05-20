package com.peleg.tweets;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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

    private View mView;

    private ProgressDialog progressDialog;


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
        mView = inflater.inflate(R.layout.fragment_main, container, false);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setTitle(getString(R.string.loading));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        hashtagET = (TextInputEditText) mView.findViewById(R.id.search_text);
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
        search = (Button) mView.findViewById(R.id.search_btn);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchTweets();
            }
        });
        return mView;
    }

    private void searchTweets() {
        hideSoftKeyboard(getActivity());
        progressDialog.show();
        // network connection required
        if(MainActivity.isConnected(getContext())) {
            TweetsApiInterface apiService = TweetsApi.getClient().create(TweetsApiInterface.class);
            String hashtag = validateInput();
            if (hashtag != null) {
                Call<TweetResponse> call = apiService.getTweets(hashtag);
                call.enqueue(new Callback<TweetResponse>() {
                    @Override
                    public void onResponse(Call<TweetResponse> call, Response<TweetResponse> response) {
                        progressDialog.dismiss();
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
                        } else {
                            hideSoftKeyboard(getActivity());
                            String errorCode = String.valueOf(response.code());
                            Log.e(TAG,errorCode);
                            Snackbar.make(mView, getString(R.string.error_accured) + " "+ errorCode, Snackbar.LENGTH_LONG)
                                    .show();
                        }
                    }

                    @Override
                    public void onFailure(Call<TweetResponse> call, Throwable t) {
                        progressDialog.dismiss();
                        Log.e(TAG, t.toString());
                    }
                });
            }
        } else {
            Snackbar.make(mView, getString(R.string.connection_required), Snackbar.LENGTH_LONG)
                    .show();
        }
    }

    private String validateInput() {
        String hashtag = null;
        String hashtagTerm = hashtagET.getText().toString();

        if (!TextUtils.isEmpty(hashtagTerm)) {

            // If not an hashtag, add #
            if (hashtagTerm.charAt(0) != '#') {
                hashtag = '#' + hashtagTerm;
            } else {
                hashtag = hashtagTerm;
            }
            try {
                hashtag = URLEncoder.encode(hashtag, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
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
