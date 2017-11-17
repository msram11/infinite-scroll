package com.ramz.infinitescroll;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Login Screen - Integrated with FB
 */
public class LoginActivity extends AppCompatActivity {

    // UI references.
    private View mProgressView;
    private LoginButton mLoginButton;
    private TextView mNoFriendsText;

    private CallbackManager callbackManager;

    private List<String> friendsList = new ArrayList<>();

    private static final String MY_FRIENDS = "me/friends";
    static final String FRIEND_LIST = "friend_list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // initialize the callback manager
        callbackManager = CallbackManager.Factory.create();
        // login button
        mLoginButton = findViewById(R.id.login_button);
        // progress dialog
        mProgressView = findViewById(R.id.login_progress);
        mNoFriendsText = findViewById(R.id.no_friends_text);
        // TODO: Need to remove this permission, if not required
        mLoginButton.setReadPermissions("user_friends");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // call back after login happens
        mLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Login -> onSuccess", "LoginResult is: " + loginResult.toString());
                Intent intent = new Intent(LoginActivity.this, FBFriendsListActivity.class);
                // retrieve logged-in user's friends
                retrieveFriendsList(loginResult);
                // pass the values & navigate to next screen
                if (!friendsList.isEmpty()) {
                    intent.putExtra(FRIEND_LIST, friendsList.toArray());
                    startActivity(intent);
                } else {
                    Log.d("LoginActivity", "Friends List is empty");
                    // display text showing no friends available
                    mNoFriendsText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancel() {
                Log.e("Login -> onCancel", "Login Cancelled");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("Login -> FBException", error.getMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // FB SDK call back
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void retrieveFriendsList(LoginResult result) {
        // show progress bar
        mProgressView.setVisibility(View.VISIBLE);
        // make graph API request to retrieve user's friends
        new GraphRequest(result.getAccessToken(), MY_FRIENDS, null, HttpMethod.GET,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        Log.d("LoginActivity", response.toString());
                        try {
                            JSONObject object = response.getJSONObject();
                            JSONArray array = object.getJSONArray("data");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject dataObject = array.getJSONObject(i);
                                String fbId = dataObject.getString("id");
                                String fbName = dataObject.getString("name");
                                Log.e("ID is:: " , fbId);
                                Log.e("FB Name:: ", fbName);
                                friendsList.add(fbName);
                            }
                        } catch (JSONException e) {
                            Log.e("retrieveFriendsList() ", e.getMessage());
                        } finally {
                            // hide the progress bar
                            mProgressView.setVisibility(View.GONE);
                        }
                    }
                }).executeAsync();
    }
}