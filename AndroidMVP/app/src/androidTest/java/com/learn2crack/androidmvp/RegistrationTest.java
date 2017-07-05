package com.learn2crack.androidmvp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.jakewharton.espresso.OkHttp3IdlingResource;
import com.learn2crack.androidmvp.network.OkHttpProvider;
import com.learn2crack.androidmvp.ui.activity.MainActivity;

import static android.support.test.espresso.Espresso.*;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RegistrationTest {


    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);


    @Test
    public void test1CheckRegistrationSuccess() {

        IdlingResource resource  = OkHttp3IdlingResource.create("OkHttp", OkHttpProvider.getOkHttpInstance());

        Espresso.registerIdlingResources(resource);

        onView(withId(R.id.tv_register)).perform(click());

        onView(withId(R.id.et_name)).perform(typeText(Constants.NAME));

        closeSoftKeyboard();

        onView(withId(R.id.et_email)).perform(typeText(Constants.EMAIL));

        closeSoftKeyboard();

        onView(withId(R.id.et_password)).perform(typeText(Constants.PASSWORD));

        closeSoftKeyboard();

        onView(withId(R.id.btn_register)).perform(click());

        onView(withText(Constants.MESSAGE_REGISTRATION_SUCCESS)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        Espresso.unregisterIdlingResources(resource);

    }

    @Test
    public void test2CheckDuplicateRegistration() {

        IdlingResource resource  = OkHttp3IdlingResource.create("OkHttp", OkHttpProvider.getOkHttpInstance());

        Espresso.registerIdlingResources(resource);

        onView(withId(R.id.tv_register)).perform(click());

        onView(withId(R.id.et_name)).perform(typeText(Constants.NAME));

        closeSoftKeyboard();

        onView(withId(R.id.et_email)).perform(typeText(Constants.EMAIL));

        closeSoftKeyboard();

        onView(withId(R.id.et_password)).perform(typeText(Constants.PASSWORD));

        closeSoftKeyboard();

        onView(withId(R.id.btn_register)).perform(click());

        onView(withText(Constants.MESSAGE_ALREADY_REGISTERED)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        Espresso.unregisterIdlingResources(resource);

    }
}
