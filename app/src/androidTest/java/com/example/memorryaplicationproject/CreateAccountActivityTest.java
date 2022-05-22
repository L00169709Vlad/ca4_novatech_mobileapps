package com.example.memorryaplicationproject;

import junit.framework.TestCase;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
@RunWith(AndroidJUnit4.class)
public class CreateAccountActivityTest {

    @Rule
    public ActivityScenarioRule<CreateAccountActivity> activityScenarioRule = new ActivityScenarioRule<>(CreateAccountActivity.class);

    @Test
    public void testOnCreateWithEmptyFields() {
        onView(withId(R.id.create_account_button))
                .perform(click());
        onView(hasDescendant(withText("Fields not filled")));
    }

    @Test
    public void testCreateWithAlreadyUsedEmail() {
        onView(withId(R.id.username_account)).perform((typeText("Vlad Munteanu")));
        closeSoftKeyboard();
        onView(withId(R.id.email_account)).perform((typeText("vlad@lyit.com")));
        closeSoftKeyboard();
        onView(withId(R.id.password_account)).perform((typeText("password")));
        closeSoftKeyboard();

        onView(withId(R.id.create_account_button))
                .perform(click());
        onView(hasDescendant(withText("Email is already in use, please sign in with a different email")));

    }
}