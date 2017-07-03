package com.kappstudio.apps.instagramm.pojo;

/**
 * Created by kanishk on 12/12/16.
 */

public class NewsFeedObject {

    private String newsfeed_image_url;
    private String newsfeed_user_name;

    public NewsFeedObject(String newsfeed_image_url, String newsfeed_user_name) {
        this.newsfeed_image_url = newsfeed_image_url;
        this.newsfeed_user_name = newsfeed_user_name;
    }

    public String getNewsfeed_image_url() {
        return newsfeed_image_url;
    }

    public void setNewsfeed_image_url(String newsfeed_image_url) {
        this.newsfeed_image_url = newsfeed_image_url;
    }

    public String getNewsfeed_user_name() {
        return newsfeed_user_name;
    }

    public void setNewsfeed_user_name(String newsfeed_user_name) {
        this.newsfeed_user_name = newsfeed_user_name;
    }
}
