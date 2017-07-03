package com.kappstudio.apps.instagramm.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.widget.Toast;

import com.kappstudio.apps.instagramm.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.kappstudio.apps.instagramm.adapters.NewsFeedAdapter;
import com.kappstudio.apps.instagramm.pojo.NewsFeedObject;
import com.kappstudio.apps.instagramm.utils.GridItemDecoration;
import com.kappstudio.apps.instagramm.utils.HttpHandler;

import static android.content.ContentValues.TAG;
import static com.kappstudio.apps.instagramm.activity.FeedActivity.TAG_IMAGES;
import static com.kappstudio.apps.instagramm.activity.FeedActivity.TAG_THUMBNAIL;

public class NewsFeedActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NewsFeedAdapter newsFeedAdapter;
    private ArrayList<NewsFeedObject> list = new ArrayList<>();
    private ArrayList<String> idList;
    private Context c;
    private ProgressDialog progressDialog;
    private int WHAT_FINALIZE = 0;

    // tags for followed users
    public static final String TAG_DATA = "data";
    public static final String TAG_FOLLOWED_USERNAME = "username";
    public static final String TAG_FOLLOWED_USERID = "id";
    public static final String TAG_URL = "url";

    SharedPreferences sharedPreferences;
    public static final String INSTA_AT_PREFERENCES = "InstaAccessToken" ;

    private String access_token;
    private String url_followed_users ;
    private String url_followed_users_post ;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();
            if (msg.what == WHAT_FINALIZE) {
                setAdapter();
            } else {
                Toast.makeText(c, "Check your network.",
                        Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);

        // get token
        sharedPreferences = getSharedPreferences(INSTA_AT_PREFERENCES, Context.MODE_PRIVATE);
        access_token = sharedPreferences.getString("InstaAccessToken", "");

        // make url to get all followed users
        url_followed_users = "https://api.instagram.com/v1/users/self/follows?access_token="
                + access_token;

        // initialize recycler view
        recyclerView = (RecyclerView) findViewById(R.id.newsfeed_rcview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,1));
        int spanCount = 2; // 2 columns
        int spacing = 5; // 50px
        boolean includeEdge = true;
        recyclerView.addItemDecoration(new GridItemDecoration(spanCount, spacing, includeEdge));
        recyclerView.setNestedScrollingEnabled(false);

        // context
        c = NewsFeedActivity.this;

        // execute async task to get all the followed users
        new GetFollowedUsers().execute();

    }

    public void setAdapter(){
        newsFeedAdapter = new NewsFeedAdapter(this, list, recyclerView);
        recyclerView.setAdapter(newsFeedAdapter);
        newsFeedAdapter.notifyItemInserted(list.size() - 1);
    }

    private class GetFollowedUsers extends AsyncTask<Void, Void, Void> {

        int what = WHAT_FINALIZE;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            progressDialog = ProgressDialog.show(c, "", "Loading news feed ..");
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url_followed_users);

            Log.e("Tag", "Response from url: " + jsonStr);

            if (jsonStr != null) {

                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray result = jsonObj.getJSONArray(TAG_DATA);

                    // looping through All Contacts
                    for (int i = 0; i < result.length(); i++) {

                        JSONObject c = result.getJSONObject(i);
                        String str_url = c.getString(TAG_FOLLOWED_USERID);
                        Log.e("TAG_FOLLOWED_USERID", str_url);
                        idList.add(str_url);

                    }
                }

                catch (final JSONException e) {
                    Log.e("Tag", "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(NewsFeedActivity.this,
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(NewsFeedActivity.this,
                                "Unable to refresh stories. Make sure you are connected to the Internet.",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //handler.sendEmptyMessage(what);
            new GetFollowedUsersRecentPost().execute();
        }
    }

    private class GetFollowedUsersRecentPost extends AsyncTask<Void, Void, Void> {

        int what = WHAT_FINALIZE;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            for (int i = 0; i < idList.size(); i++) {

                url_followed_users_post = "https://api.instagram.com/v1/users/"
                        + idList.get(i)
                        + "/media/recent/?access_token="
                        + access_token
                        + "&count=" + 20;

                HttpHandler sh = new HttpHandler();
                // Making a request to url and getting response
                String jsonStr = sh.makeServiceCall(url_followed_users_post);

                Log.e("Tag", "Response from url: " + jsonStr);

                if (jsonStr != null) {

                    try {
                        JSONObject jsonObj = new JSONObject(jsonStr);

                        // Getting JSON Array node
                        JSONArray result = jsonObj.getJSONArray(TAG_DATA);

                        // looping through All Contacts
                        for (int in = 0; in < result.length(); in++) {

                            JSONObject c = result.getJSONObject(in);

                            JSONObject user_object =  c.getJSONObject("user");
                            String user_name = user_object.getString("username");

                            JSONObject images_obj = c
                                    .getJSONObject(TAG_IMAGES);
                            JSONObject standard_image = images_obj.getJSONObject(TAG_THUMBNAIL);
                            String str_url = standard_image.getString(TAG_URL);

                            list.add(new NewsFeedObject(str_url, user_name));

                        }
                    }

                    catch (final JSONException e) {
                        Log.e("Tag", "Json parsing error: " + e.getMessage());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(NewsFeedActivity.this,
                                        "Json parsing error: " + e.getMessage(),
                                        Toast.LENGTH_LONG)
                                        .show();
                            }
                        });

                    }
                }

                else {
                    Log.e(TAG, "Couldn't get json from server.");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(NewsFeedActivity.this,
                                    "Unable to refresh stories. Make sure you are connected to the Internet.",
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            handler.sendEmptyMessage(what);
        }
    }

}
