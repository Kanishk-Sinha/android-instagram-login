package com.kappstudio.apps.instagramm.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kappstudio.apps.instagramm.R;
import com.kappstudio.apps.instagramm.adapters.ImagesAdapter;
import com.kappstudio.apps.instagramm.pojo.ImageObject;
import com.kappstudio.apps.instagramm.sqllite.UserStore;
import com.kappstudio.apps.instagramm.sqllite.UserStoreDBHandler;
import com.kappstudio.apps.instagramm.utils.GridItemDecoration;
import com.kappstudio.apps.instagramm.utils.HttpHandler;
import com.kappstudio.apps.instagramm.utils.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class FeedActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImagesAdapter imagesAdapter;
    private HashMap<String, String> userInfo;
    private ArrayList<ImageObject> imageThumbList = new ArrayList<>();
    Context c;
    private ProgressDialog pd;
    private int WHAT_FINALIZE = 0;
    public static final String TAG_DATA = "data";
    public static final String TAG_IMAGES = "images";
    public static final String TAG_THUMBNAIL = "standard_resolution";
    public static final String TAG_URL = "url";
    private UserStoreDBHandler dbHandler;

    // user data
    private String user_id, user_name, access_token, user_image, user_following, user_followers;

    private String url ;

    private RelativeLayout rel;

    private TextView username, userfollowing, userfollowers;
    private ImageView userimg, background;
    private CardView userdashboard;
    private Button logoutBtn;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (pd != null && pd.isShowing())
                pd.dismiss();
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
        setContentView(R.layout.activity_feed);
        logoutBtn = (Button) findViewById(R.id.buttonlogout);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        getUserInfoFromSQLLite();

        // make url to get images
        url = "https://api.instagram.com/v1/users/"
                + "self"
                + "/media/recent/?access_token="
                + access_token
                + "&count=" + -1;

        userdashboard = (CardView) findViewById(R.id.user_dashboard);
        username = (TextView) findViewById(R.id.username);
        userfollowers = (TextView) findViewById(R.id.userFollowerCount);
        userfollowing = (TextView) findViewById(R.id.userFollowedbyCount);
        userimg = (ImageView) findViewById(R.id.userimg);
        background = (ImageView) findViewById(R.id.backgroundimage);

        setDashboard();

        // initialize recycler view
        rel = (RelativeLayout) findViewById(R.id.main_content);
        recyclerView = (RecyclerView) findViewById(R.id.images_rcview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,1));
        int spanCount = 2; // 2 columns
        int spacing = 5; // 50px
        boolean includeEdge = true;
        recyclerView.addItemDecoration(new GridItemDecoration(spanCount, spacing, includeEdge));
        recyclerView.setNestedScrollingEnabled(false);

        // context
        c = FeedActivity.this;

        // execute async task to get image addresses from Instagram
        new GetImages().execute();

    }

    public void logout()
    {
        dbHandler = new UserStoreDBHandler(this);
        dbHandler.dropUserInfoTable();
        dbHandler.close();
        /*Intent i = new Intent(FeedActivity.this, MainActivity.class);
        startActivity(i);*/
        finish();
    }

    public void getUserInfoFromSQLLite()
    {
        dbHandler = new UserStoreDBHandler(this);
        ArrayList<UserStore> list = dbHandler.getUserInfo();
        UserStore modelData = list.get(0);
        user_id = modelData.getUSER_ID();
        access_token = modelData.getUSER_ACCESS_TOKEN();
        user_name = modelData.getUSER_NAME();
        user_image = modelData.getUSER_PROFILE_PICTURE();
        user_following = modelData.getUSER_FOLLOWING();
        user_followers = modelData.getUSER_FOLLOWERS();
        dbHandler.close();

    }


    public void setDashboard(){

        new ImageLoader(userimg).execute(user_image);
        new ImageLoader(background).execute(user_image);

        username.setText(user_name);
        userfollowers.setText("You have " + user_followers + " followers");
        userfollowing.setText("You are following " + user_following + " people");

    }
    public void setAdapter(){

        imagesAdapter = new ImagesAdapter(this, imageThumbList, recyclerView);
        recyclerView.setAdapter(imagesAdapter);
        imagesAdapter.notifyItemInserted(imageThumbList.size()-1);

    }

    private class GetImages extends AsyncTask<Void, Void, Void> {

        int what = WHAT_FINALIZE;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pd = ProgressDialog.show(c, "", "Loading images..");
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e("Tag", "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray result = jsonObj.getJSONArray(TAG_DATA);

                    Log.e("data length", " " + result.length());

                    // looping through All Contacts
                    for (int i = 0; i < result.length(); i++) {

                        JSONObject c = result.getJSONObject(i);

                        JSONObject images_obj = c
                                .getJSONObject(TAG_IMAGES);

                        JSONObject standard_image = images_obj.getJSONObject(TAG_THUMBNAIL);

                        String str_url = standard_image.getString(TAG_URL);
                        Log.e("data url", str_url);

                        imageThumbList.add(new ImageObject(str_url));

                    }
                } catch (final JSONException e) {
                    Log.e("Tag", "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(FeedActivity.this,
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
                        Toast.makeText(FeedActivity.this,
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
            handler.sendEmptyMessage(what);
        }
    }
}
