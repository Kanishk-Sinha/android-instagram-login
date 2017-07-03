package com.kappstudio.apps.instagramm.sqllite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by kanishk on 13/12/16.
 */

public class UserStoreDBHandler extends SQLiteOpenHelper {

    // Database version
    private static final int DATABASE_VERSION = 1; // 1
    // Database Name
    private static final String DATABASE_NAME = "USERSTORE";
    // Contacts table name
    private static final String TABLE_USERINFO = "userInfo";

    private String USER_ID = "USER_ID", USER_ACCESS_TOKEN = "USER_ACCESS_TOKEN", USER_NAME = "USER_NAME",
            USER_PROFILE_PICTURE = "USER_PROFILE_PICTURE", USER_FOLLOWING = "USER_FOLLOWING", USER_FOLLOWERS = "USER_FOLLOWERS";

    Context ctx;
    public UserStoreDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        ctx = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_USERINFO_TABLE = "CREATE TABLE " + TABLE_USERINFO + "(" +
                "_id" + " INTEGER PRIMARY KEY," +
                USER_ID + " TEXT," +
                USER_ACCESS_TOKEN + " TEXT," +
                USER_NAME + " TEXT," +
                USER_PROFILE_PICTURE + " TEXT," +
                USER_FOLLOWING + " TEXT," +
                USER_FOLLOWERS + " TEXT" + ");";

        db.execSQL(CREATE_USERINFO_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERINFO);
        // Create tables again
        onCreate(db);

    }

    // CRUD operation
    public void  addUserInfo(UserStore userStore)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(USER_ID, userStore.getUSER_ID());
        contentValues.put(USER_ACCESS_TOKEN, userStore.getUSER_ACCESS_TOKEN());
        contentValues.put(USER_NAME, userStore.getUSER_NAME());
        contentValues.put(USER_PROFILE_PICTURE, userStore.getUSER_PROFILE_PICTURE());
        contentValues.put(USER_FOLLOWING, userStore.getUSER_FOLLOWING());
        contentValues.put(USER_FOLLOWERS, userStore.getUSER_FOLLOWERS());

        db.insert(TABLE_USERINFO, null, contentValues);
        db.close();

    }

    public ArrayList<UserStore> getUserInfo()
    {
        ArrayList<UserStore> list = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_USERINFO;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            do {
                UserStore userStore = new UserStore();
                userStore.setUSER_ID(cursor.getString(1));
                userStore.setUSER_ACCESS_TOKEN(cursor.getString(2));
                userStore.setUSER_NAME(cursor.getString(3));
                userStore.setUSER_PROFILE_PICTURE(cursor.getString(4));
                userStore.setUSER_FOLLOWING(cursor.getString(5));
                userStore.setUSER_FOLLOWERS(cursor.getString(6));
                //add to list
                list.add(userStore);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public void dropUserInfoTable()
    {
        ctx.deleteDatabase(DATABASE_NAME);
        /*String selectQuery = "DROP TABLE IF EXISTS " + TABLE_USERINFO;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.close();*/
    }


    public boolean isTableExist(String table_name){

        String selectQuery = "SELECT DISTINCT tbl_name FROM sqlite_master WHERE tbl_name = '"+table_name+"'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor!=null) {
            if(cursor.getCount()>0 && cursor.getCount()==1) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }
}
