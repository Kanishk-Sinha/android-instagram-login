package com.kappstudio.apps.instagramm.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.kappstudio.apps.instagramm.R;
import com.kappstudio.apps.instagramm.sqllite.UserStore;
import com.kappstudio.apps.instagramm.sqllite.UserStoreDBHandler;

import java.io.File;
import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private UserStoreDBHandler dbHandler;
    private Boolean isUserTableExists;
    private static final String TABLE_USERINFO = "userInfo";
    private String access_token;
    private static final String DATABASE_NAME = "USERSTORE";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        dbHandler = new UserStoreDBHandler(this);

        isUserTableExists = checkIfTableExists(); // user table is dropped on every logout action

        if (isUserTableExists)
        {
            getUserInfoFromSQLLite();
            if (access_token.equalsIgnoreCase("nodata"))
            {
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
            else
            {
                Intent i = new Intent(SplashActivity.this, FeedActivity.class);
                startActivity(i);
                finish();
            }
        }
        else
        {
            Intent i = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    public boolean checkIfTableExists()
    {
        File dbtest = new File("/data/data/" + "com.kappstudio.apps.instagramm" + "/databases/" + DATABASE_NAME);
        if (dbtest.exists())
        {
            boolean exist = dbHandler.isTableExist(TABLE_USERINFO);
            return exist;
        }
        else return false;
    }

    public String getUserInfoFromSQLLite()
    {
        UserStore modelData;
        ArrayList<UserStore> list = dbHandler.getUserInfo();
        if (list!=null)
        {
            modelData = list.get(0);
            access_token = modelData.getUSER_ACCESS_TOKEN();
            return access_token;
        }
        else return "nodata";

    }
}
