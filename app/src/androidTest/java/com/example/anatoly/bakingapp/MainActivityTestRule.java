package com.example.anatoly.bakingapp;

import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.intent.rule.IntentsTestRule;


/**
 * I need to make my own test rule because:
 * I need an instance of idlingResource before activity calles it's onCreate method (because in
 * onCreate method I initialize the loader and idlingResource is using by it).
 * Solution got here: https://stackoverflow.com/a/34793638/7635275
 */
public class MainActivityTestRule extends IntentsTestRule<MainActivity> {

    private IdlingResource mIdlingResource;

    public MainActivityTestRule(Class<MainActivity> activityClass) {
        super(activityClass);
    }

    @Override
    protected void beforeActivityLaunched() {
        super.beforeActivityLaunched();
        mIdlingResource = MainActivity.getIdlingResource();
    }

    public IdlingResource getmIdlingResource() {
        return mIdlingResource;
    }
}
