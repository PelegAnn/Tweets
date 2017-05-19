package com.peleg.tweets;

import android.content.Context;
import android.content.SharedPreferences;
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

    public static final String CONSUMER_KEY = "XGekuI7ujEA2EjMfVhqUcgyJV";
    public static final String CONSUMER_SECRET = "gAX2mTs4SEEkGmHuI09h9Wo5EYrZ7IGYrY51UMBIonMIyLECQZ";
    private static final String TAG = MainActivity.class.getName();

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

        if(!sharedPref.getBoolean(getString(R.string.has_access_token),false))
            getAccessToken();

        TweetsApi.setAuthValue("Bearer "+sharedPref.getString(getString(R.string.access_token),"error"));


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
    private static String encodeKeys(String consumerKey, String consumerSecret) {

            String fullKey = consumerKey + ":" + consumerSecret;
            String str = Base64.encodeToString(fullKey.getBytes(),Base64.NO_WRAP);

            TweetsApi.setAuthValue("Basic "+str);

            return "Basic "+str;
    }

    @Override
    public void onNewTweets(List<Tweet> items) {
        mViewPager.setCurrentItem(1);
        mSectionsPagerAdapter.add(items);
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
