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
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.isNotEnabled;

import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class ShoppingListActivityTest {

    @Rule
    public ActivityScenarioRule<ShoppingListActivity> activityScenarioRule = new ActivityScenarioRule<>(ShoppingListActivity.class);

    @Test
    public void testEmptyItem() {
        onView(withId(R.id.bt_shopList))
                .perform(click());
        onView(hasDescendant(withText("Please enter an item")));
    }

    @Test
    public void testAddFilledItem() {

        onView(withId(R.id.et_shoppingList)).perform(typeText("Tomatoes"));
        onView(withId(R.id.bt_shopList))
                .perform(click());
        onView(hasDescendant(withText("Tomatoes")));
    }

    @Test
    public void testRemoveItem() {

        onView(withId(R.id.et_shoppingList)).perform(typeText("Coffee"));
        onView(withId(R.id.bt_shopList))
                .perform(click());
        onView(hasDescendant(withText("Coffee")));
        onView(withText("Coffee")).perform(click());
        onView(withText("Yes")).perform(click());
        onView(withText("Coffee")).check(doesNotExist());

    }
}