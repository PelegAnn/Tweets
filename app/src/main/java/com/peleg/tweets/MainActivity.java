package com.peleg.tweets;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.ViewGroup;

import com.peleg.tweetslibrary.model.OauthResponse;
import com.peleg.tweetslibrary.rest.TweetsApi;
import com.peleg.tweetslibrary.rest.TweetsApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MainFragment.OnNewTweetsListener{

    private static final String TAG = MainActivity.class.getName();

    public static final String CONSUMER_KEY = "tn4tESYeJ4E95pmEbqz2DRX6K";
    public static final String CONSUMER_SECRET = "s18o2Q0OYqh7gJAJliukD2wPZFYt4vVMT4hEKYEpfN4MweYKwx";

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the two
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        sharedPref = this.getPreferences(Context.MODE_PRIVATE);

        if(!isConnected(this)) {
            Snackbar.make(mViewPager, getString(R.string.connection_required), Snackbar.LENGTH_LONG)
                    .show();
        } else {
            // check if we have already bearer saved
            if (!sharedPref.getBoolean(getString(R.string.has_access_token), false)) {
                getAccessToken();
            } else {
                addBearer();
            }
        }
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    private void getAccessToken() {
        encodeKeys(CONSUMER_KEY,CONSUMER_SECRET);
        TweetsApiInterface apiService = TweetsApi.getClient().create(TweetsApiInterface.class);
        Call<OauthResponse> call = apiService.ObtainABearer("client_credentials");
        call.enqueue(new Callback<OauthResponse>() {
            @Override
            public void onResponse(Call<OauthResponse> call, Response<OauthResponse> response) {
                if(response.code() == 200) {
                    editor = sharedPref.edit();
                    editor.putBoolean(getString(R.string.has_access_token),true);
                    if(response.body().getTokenType().equals("bearer")) {
                        editor.putString(getString(R.string.access_token), response.body().getAccessToken()).apply();
                        addBearer();
                    }
                }
            }

            @Override
            public void onFailure(Call<OauthResponse> call, Throwable t) {
                Log.e(TAG,t.getMessage());
            }
        });
    }

    // Encodes the consumer key and secret to create the basic authorization key
    private static void encodeKeys(String consumerKey, String consumerSecret) {

        String fullKey = consumerKey + ":" + consumerSecret;
        String str = Base64.encodeToString(fullKey.getBytes(),Base64.NO_WRAP);

        TweetsApi.setAuthValue("Basic "+str);
    }

    @Override
    public void onNewTweets(List<Tweet> items) {
        // Navigate to TweetsFragment
        mViewPager.setCurrentItem(1);
        mSectionsPagerAdapter.add(items);
    }

    private void addBearer() {
        TweetsApi.setAuthValue("Bearer "+sharedPref.getString(getString(R.string.access_token),"error"));
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public static class SectionsPagerAdapter extends FragmentPagerAdapter {

        private TweetsFragment tweetsFragment;
        private MainFragment mainFragment;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return MainFragment.newInstance();
                case 1:
                    return TweetsFragment.newInstance();
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        public void add(List<Tweet> items) {
            if(tweetsFragment!= null) {
                tweetsFragment.updateList(items);
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
            // save the appropriate reference depending on position
            switch (position) {
                case 0:
                    mainFragment = (MainFragment) createdFragment;
                    break;
                case 1:
                    tweetsFragment = (TweetsFragment) createdFragment;
                    break;
            }
            return createdFragment;
        }

    }


}
