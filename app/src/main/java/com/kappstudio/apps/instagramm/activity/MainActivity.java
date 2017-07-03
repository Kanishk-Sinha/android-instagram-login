package com.kappstudio.apps.instagramm.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kappstudio.apps.instagramm.application.AppData;
import com.kappstudio.apps.instagramm.application.InstagramApp;
import com.kappstudio.apps.instagramm.R;

import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    Button btnConnect; // Button that launch the instagram webview

    private InstagramApp mApp; //InstagramApp instance

    private HashMap<String, String> userInfoHashmap = new HashMap<>();

    SharedPreferences sharedpreferences;
    public static final String INSTA_USERINFO_PREFERENCES = "InstaUserInfo" ;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == InstagramApp.WHAT_FINALIZE) {
                userInfoHashmap = mApp.getUserInfo();
            } else if (msg.what == InstagramApp.WHAT_FINALIZE) {
                Toast.makeText(MainActivity.this, "Check your network.",
                        Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnConnect = (Button) findViewById(R.id.buttonConnect);
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mApp.authorize();

            }
        });

        mApp = new InstagramApp(this, AppData.CLIENT_ID,
                AppData.CLIENT_SECRET, AppData.CALLBACK_URL);

        mApp.setListener(new InstagramApp.OAuthAuthenticationListener() {
            @Override
            public void onSuccess() {
// tvSummary.setText("Connected as " + mApp.getUserName());
                /*btnConnect.setText("Disconnect");
                llAfterLoginView.setVisibility(View.VISIBLE);
// userInfoHashmap = mApp.*/
                mApp.fetchUserName(handler);

                if (userInfoHashmap!=null) {

                    startActivity(new Intent(MainActivity.this, FeedActivity.class)
                            .putExtra("userInfo", userInfoHashmap));
                    finish();
                    /*Set<String> keys = userInfoHashmap.keySet();  //get all keys
                    for(String i: keys) {
                        Toast.makeText(MainActivity.this, "hashmap " + userInfoHashmap.get(i), Toast.LENGTH_SHORT).show();
                    }*/
                }
                else
                    Toast.makeText(MainActivity.this, "no data fetched", Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onFail(String error) {
                Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });


    }
}
