package com.peleg.tweets;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.test.suitebuilder.TestMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by hannypeleg on 5/18/17.
 */

public class TweetsFragment extends Fragment {

    private List<Tweet> mItems;
    private TweetsAdapter mAdapter;
    private RecyclerView tweetsRV;
    private TextView noResults;

    public static TweetsFragment newInstance() {
        return new TweetsFragment();
    }

    public TweetsFragment(){};
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tweets, container, false);

        tweetsRV = (RecyclerView) view.findViewById(R.id.tweets_list);

        noResults = (TextView) view.findViewById(R.id.no_result_text);

        mItems = TweetsList.getInstance().getList();
        mAdapter = new TweetsAdapter(mItems,getContext());

        SpacesDecoration decoration = new SpacesDecoration(16);
        tweetsRV.addItemDecoration(decoration);

        tweetsRV.setAdapter(mAdapter);
        tweetsRV.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        return view;
    }

    public void updateList(List<Tweet> items) {
        if(items.size() == 0) {
            tweetsRV.setVisibility(View.GONE);
            noResults.setVisibility(View.VISIBLE);
        } else {
            tweetsRV.setVisibility(View.VISIBLE);
        }
        TweetsList.getInstance().setItems(items);
        if(mAdapter!= null) {
           mAdapter.addAll(items);
        }

    }
}
