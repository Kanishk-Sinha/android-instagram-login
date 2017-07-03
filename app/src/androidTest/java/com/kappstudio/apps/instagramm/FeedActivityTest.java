package com.kappstudio.apps.instagramm;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;

import com.kappstudio.apps.instagramm.activity.FeedActivity;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by kanishk on 13/12/16.
 */

public class FeedActivityTest {

    @Rule
    public ActivityTestRule<FeedActivity> mActivityRule
            = new ActivityTestRule<>(FeedActivity.class);

    // Test the logout button
    @Test
    public void testButton() {
        onView(withText("logout")).perform(click());
    }

    @Test
    public void testRecyclerView() {
        onView(withId(R.id.images_rcview)).perform(RecyclerViewActions.scrollToPosition(6));
    }
}
