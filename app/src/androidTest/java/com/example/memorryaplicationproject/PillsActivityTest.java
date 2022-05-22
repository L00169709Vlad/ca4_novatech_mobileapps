package com.example.memorryaplicationproject;

import junit.framework.TestCase;

import androidx.test.espresso.ViewAssertion;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.isNotEnabled;

import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
@RunWith(AndroidJUnit4.class)

public class PillsActivityTest {
    @Rule
    public ActivityScenarioRule<PillsActivity> activityScenarioRule = new ActivityScenarioRule<>(PillsActivity.class);

    @Test
    public void testAddPill() {
        onView(withId(R.id.addPillBtn))
                .perform(click());
        onView(withId(R.id.pill_name)).check(matches(withHint("Pill Name")));
    }

    @Test
    public void testOnRemove() {
        onView(withId(R.id.addPillBtn))
                .perform(click());
        onView(withId(R.id.removePill))
                .perform(click()).check(doesNotExist());
    }
}