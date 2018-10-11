package com.a00934249.renzd.photogallery;

import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class UITest {
    String[] caption;
    String[] fromDate;
    String[] toDate;
    String[] fromTime;
    String[] toTime;
    String[] lat;
    String[] lon;

    @Before
    public void Setup() {

        caption = new String[] {"Hello", "Caption"};
        fromDate =new String[] {"20181009", "20181111"};
        toDate = new String[]{"20181012", "20181012"};
        fromTime = new String[]{"011010", "120010"};
        toTime = new String[]{"131020", "231010"};
        lat = new String[]{"49.24202020", "48.90101010"};
        lon = new String[]{"123.00202020", "122.9101010"};
    }

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule(MainActivity.class);

    @Test
    public void filterCaptionTest()
    {
        onView(withId(R.id.btnSearch)).perform(click());
        onView(withId(R.id.search_fromDate)).perform(clearText());
        onView(withId(R.id.search_toDate)).perform(clearText());
        onView(withId(R.id.search_fromTime)).perform(clearText());
        onView(withId(R.id.search_toTime)).perform(clearText());
        onView(withId(R.id.search_lat)).perform(clearText());
        onView(withId(R.id.search_lon)).perform(clearText());
        onView(withId(R.id.search_caption)).perform(clearText());
        onView(withId(R.id.search_caption)).perform(typeText(caption[0]), closeSoftKeyboard());
        onView(withId(R.id.search_search)).perform(click());
        for (int i = 0; i <= 3; i++) {
            onView(withId(R.id.btnRight)).perform(click());
        }
    }

    @Test
    public void filterCaptionLoc()
    {
        onView(withId(R.id.btnSearch)).perform(click());
        onView(withId(R.id.search_fromDate)).perform(clearText());
        onView(withId(R.id.search_toDate)).perform(clearText());
        onView(withId(R.id.search_fromTime)).perform(clearText());
        onView(withId(R.id.search_toTime)).perform(clearText());
        onView(withId(R.id.search_lat)).perform(clearText());
        onView(withId(R.id.search_lon)).perform(clearText());
        onView(withId(R.id.search_caption)).perform(clearText());
        onView(withId(R.id.search_caption)).perform(typeText(caption[1]), closeSoftKeyboard());
        onView(withId(R.id.search_caption)).perform(typeText(caption[1]), closeSoftKeyboard());
        onView(withId(R.id.search_caption)).perform(typeText(caption[1]), closeSoftKeyboard());
        onView(withId(R.id.search_search)).perform(click());
        for (int i = 0; i <= 3; i++) {
            onView(withId(R.id.btnRight)).perform(click());
        }
    }

    @Test
    public void filterCaptionDate()
    {
        onView(withId(R.id.btnSearch)).perform(click());
        onView(withId(R.id.search_fromDate)).perform(clearText());
        onView(withId(R.id.search_toDate)).perform(clearText());
        onView(withId(R.id.search_fromTime)).perform(clearText());
        onView(withId(R.id.search_toTime)).perform(clearText());
        onView(withId(R.id.search_lat)).perform(clearText());
        onView(withId(R.id.search_lon)).perform(clearText());
        onView(withId(R.id.search_caption)).perform(clearText());

        onView(withId(R.id.search_caption)).perform(typeText(caption[0]), closeSoftKeyboard());
        onView(withId(R.id.search_fromDate)).perform(typeText(fromDate[0]), closeSoftKeyboard());
        onView(withId(R.id.search_toDate)).perform(typeText(toDate[0]), closeSoftKeyboard());
        onView(withId(R.id.search_search)).perform(click());

        for (int i = 0; i <= 3; i++) {
            onView(withId(R.id.btnRight)).perform(click());
        }
    }

    @Test
    public void filterCaptionDate2()
    {
        onView(withId(R.id.btnSearch)).perform(click());
        onView(withId(R.id.search_fromDate)).perform(clearText());
        onView(withId(R.id.search_toDate)).perform(clearText());
        onView(withId(R.id.search_fromTime)).perform(clearText());
        onView(withId(R.id.search_toTime)).perform(clearText());
        onView(withId(R.id.search_lat)).perform(clearText());
        onView(withId(R.id.search_lon)).perform(clearText());
        onView(withId(R.id.search_caption)).perform(clearText());

        onView(withId(R.id.search_caption)).perform(typeText(caption[1]), closeSoftKeyboard());
        onView(withId(R.id.search_fromDate)).perform(typeText(fromDate[1]), closeSoftKeyboard());
        onView(withId(R.id.search_toDate)).perform(typeText(toDate[1]), closeSoftKeyboard());
        onView(withId(R.id.search_search)).perform(click());

        for (int i = 0; i <= 3; i++) {
            onView(withId(R.id.btnRight)).perform(click());
        }
    }

    @Test
    public void filterCaptionDateLoc()
    {
        onView(withId(R.id.btnSearch)).perform(click());
        onView(withId(R.id.search_fromDate)).perform(clearText());
        onView(withId(R.id.search_toDate)).perform(clearText());
        onView(withId(R.id.search_fromTime)).perform(clearText());
        onView(withId(R.id.search_toTime)).perform(clearText());
        onView(withId(R.id.search_lat)).perform(clearText());
        onView(withId(R.id.search_lon)).perform(clearText());
        onView(withId(R.id.search_caption)).perform(clearText());

        onView(withId(R.id.search_caption)).perform(typeText(caption[0]), closeSoftKeyboard());
        onView(withId(R.id.search_fromDate)).perform(typeText(fromDate[0]), closeSoftKeyboard());
        onView(withId(R.id.search_toDate)).perform(typeText(toDate[1]), closeSoftKeyboard());
        onView(withId(R.id.search_fromTime)).perform(typeText(fromTime[0]), closeSoftKeyboard());
        onView(withId(R.id.search_toTime)).perform(typeText(toTime[0]), closeSoftKeyboard());
        onView(withId(R.id.search_search)).perform(click());

        for (int i = 0; i <= 3; i++) {
            onView(withId(R.id.btnRight)).perform(click());
        }
    }

    @Test
    public void filterCaptionDateLoc2()
    {
        onView(withId(R.id.btnSearch)).perform(click());
        onView(withId(R.id.search_fromDate)).perform(clearText());
        onView(withId(R.id.search_toDate)).perform(clearText());
        onView(withId(R.id.search_fromTime)).perform(clearText());
        onView(withId(R.id.search_toTime)).perform(clearText());
        onView(withId(R.id.search_lat)).perform(clearText());
        onView(withId(R.id.search_lon)).perform(clearText());
        onView(withId(R.id.search_caption)).perform(clearText());

        onView(withId(R.id.search_caption)).perform(typeText(caption[1]), closeSoftKeyboard());
        onView(withId(R.id.search_fromDate)).perform(typeText(fromDate[1]), closeSoftKeyboard());
        onView(withId(R.id.search_toDate)).perform(typeText(toDate[1]), closeSoftKeyboard());
        onView(withId(R.id.search_lat)).perform(typeText(lat[1]), closeSoftKeyboard());
        onView(withId(R.id.search_lon)).perform(typeText(lon[1]), closeSoftKeyboard());
        onView(withId(R.id.search_search)).perform(click());

        for (int i = 0; i <= 3; i++) {
            onView(withId(R.id.btnRight)).perform(click());
        }
    }

}