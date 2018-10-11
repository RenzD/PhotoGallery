package com.a00934249.renzd.photogallery;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class DataTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void TestSearch() {
        onView(withId(R.id.btnSearch)).perform(click());
        onView(withId(R.id.captionText)).perform(typeText("31/01/18"), closeSoftKeyboard());
        onView(withId(R.id.search_search)).perform(click());
        for (int i = 0; i <= 5; i++) {
            onView(withId(R.id.btnRight)).perform(click());
        }
    }
}
