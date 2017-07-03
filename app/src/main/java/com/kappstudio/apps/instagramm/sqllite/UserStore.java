package com.kappstudio.apps.instagramm.sqllite;

/**
 * Created by kanishk on 13/12/16.
 */

public class UserStore {

    private String USER_ID, USER_ACCESS_TOKEN, USER_NAME,
            USER_PROFILE_PICTURE, USER_FOLLOWING, USER_FOLLOWERS;

    public UserStore(){}

    public UserStore(String USER_ID, String USER_ACCESS_TOKEN, String USER_NAME, String USER_PROFILE_PICTURE, String USER_FOLLOWING, String USER_FOLLOWERS) {
        this.USER_ID = USER_ID;
        this.USER_ACCESS_TOKEN = USER_ACCESS_TOKEN;
        this.USER_NAME = USER_NAME;
        this.USER_PROFILE_PICTURE = USER_PROFILE_PICTURE;
        this.USER_FOLLOWING = USER_FOLLOWING;
        this.USER_FOLLOWERS = USER_FOLLOWERS;
    }

    public String getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(String USER_ID) {
        this.USER_ID = USER_ID;
    }

    public String getUSER_ACCESS_TOKEN() {
        return USER_ACCESS_TOKEN;
    }

    public void setUSER_ACCESS_TOKEN(String USER_ACCESS_TOKEN) {
        this.USER_ACCESS_TOKEN = USER_ACCESS_TOKEN;
    }

    public String getUSER_NAME() {
        return USER_NAME;
    }

    public void setUSER_NAME(String USER_NAME) {
        this.USER_NAME = USER_NAME;
    }

    public String getUSER_PROFILE_PICTURE() {
        return USER_PROFILE_PICTURE;
    }

    public void setUSER_PROFILE_PICTURE(String USER_PROFILE_PICTURE) {
        this.USER_PROFILE_PICTURE = USER_PROFILE_PICTURE;
    }

    public String getUSER_FOLLOWING() {
        return USER_FOLLOWING;
    }

    public void setUSER_FOLLOWING(String USER_FOLLOWING) {
        this.USER_FOLLOWING = USER_FOLLOWING;
    }

    public String getUSER_FOLLOWERS() {
        return USER_FOLLOWERS;
    }

    public void setUSER_FOLLOWERS(String USER_FOLLOWERS) {
        this.USER_FOLLOWERS = USER_FOLLOWERS;
    }
}
