package com.example.memorryaplicationproject;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;


import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class PhotosActivityTest {

    @Rule
    public ActivityScenarioRule<PhotosActivity> activityScenarioRule = new ActivityScenarioRule<>(PhotosActivity.class);

    @Test
    public void testOnCreateWithNoPicture() {
        onView(withId(R.id.post_save_photo_button)).perform(click());
        onView(hasDescendant(withText("Please enter valid data")));
    }
}