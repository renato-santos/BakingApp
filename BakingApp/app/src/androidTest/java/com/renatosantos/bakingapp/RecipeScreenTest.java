package com.renatosantos.bakingapp;


import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.Toolbar;

import com.renatosantos.bakingapp.ui.MainActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;


import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;


@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RecipeScreenTest {

    private static final String BAKING_APP = "Baking Time";

    private static final String NUTELLA_PIE_RECIPE = "Nutella Pie";
    private static final String BROWNIES_RECIPE = "Brownies";
    private static final String YELLOW_CAKE_RECIPE = "Yellow Cake";
    private static final String CHEESECAKE_RECIPE = "Cheesecake";

    @Rule
    public ActivityTestRule<MainActivity> mMainActivityTestRule = new ActivityTestRule<>(MainActivity.class);


    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {

        mIdlingResource = mMainActivityTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);

    }


    @Test
    public void A_displayCorrectLabels() {

        onView(withId(R.id.recyclerview_recipes)).perform(RecyclerViewActions.scrollToPosition(0));
        onView(withText(NUTELLA_PIE_RECIPE)).check(matches(isDisplayed()));

        onView(withId(R.id.recyclerview_recipes)).perform(RecyclerViewActions.scrollToPosition(1));
        onView(withText(BROWNIES_RECIPE)).check(matches(isDisplayed()));

        onView(withId(R.id.recyclerview_recipes)).perform(RecyclerViewActions.scrollToPosition(2));
        onView(withText(YELLOW_CAKE_RECIPE)).check(matches(isDisplayed()));

        onView(withId(R.id.recyclerview_recipes)).perform(RecyclerViewActions.scrollToPosition(3));
        onView(withText(CHEESECAKE_RECIPE)).check(matches(isDisplayed()));
    }


    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }

}
