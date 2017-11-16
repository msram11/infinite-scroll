package com.ramz.infinitescroll;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.ramz.infinitescroll.recyclerview.FriendsListAdapter;
import com.ramz.infinitescroll.widget.InfiniteScrollListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Displays the list of friends for the current user
 */

public class FBFriendsListActivity extends AppCompatActivity {

    private List<String> friendsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fbfriends_list);
        RecyclerView recyclerView = findViewById(R.id.friends_view_list);
        // retrieve the list of friends from the bundle
        Intent intent = getIntent();
        if (intent != null) {
            String[] friendsArray = intent.getStringArrayExtra(LoginActivity.FRIEND_LIST);
            friendsList = Arrays.asList(friendsArray);
        } else {
            // bundle is empty
            Log.e("FriendsListActivity", "Bundle is EMPTY!!!");
        }
        // set the list to recycler view
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        // set default animator for recycler view
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        // adding divider lines between views
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        final RecyclerView.Adapter mAdapter = new FriendsListAdapter(friendsList);
        // add the adapter
        recyclerView.setAdapter(mAdapter);
        InfiniteScrollListener scrollListener = new InfiniteScrollListener((LinearLayoutManager) layoutManager) {
            @Override
            public void loadMore(int page, int totalItemsCount, RecyclerView view) {
                // in our case, we need to show the list from the beginning
                Log.i("FriendsListActivity", "need to load more data");
            }
        };
        // Adds the scroll listener to RecyclerView
        recyclerView.addOnScrollListener(scrollListener);
    }
}
