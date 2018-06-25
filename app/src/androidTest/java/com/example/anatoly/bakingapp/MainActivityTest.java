package com.example.anatoly.bakingapp;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;

import com.example.anatoly.bakingapp.Adapters.RecipesListAdapter;
import com.example.anatoly.bakingapp.Model.Recipe;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public MainActivityTestRule mActivityTestRule =
            new MainActivityTestRule(MainActivity.class);

    private IdlingResource mIdlingResource;

    @Before
    public void stubAllExternalIntents() {
        // By default Espresso Intents does not stub any Intents. Stubbing needs to be setup before
        // every test run. In this case all external Intents will be blocked.
        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getmIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
    }



    /**
     * Testing here a random recyclerview item click.
     * Solution got here:
     * https://stackoverflow.com/a/35306034/7635275
     */
    @Test
    public void clickRecipesListItem_newRecipeDetailsActivityIsStarted() {
        final MainActivity activity = mActivityTestRule.getActivity();
        // Checking is RecyclerView displayed waiting for it using IdlingResource.
        // Without it findViewById returns null.
        onView(withId(R.id.rv_select_recipes))
                .check(matches(isDisplayed()));

        RecyclerView recyclerView = activity.findViewById(R.id.rv_select_recipes);
        RecipesListAdapter adapter = (RecipesListAdapter) recyclerView.getAdapter();
        int itemCount = adapter.getItemCount();
        Random ran = new Random();
        int randomPos = ran.nextInt(itemCount);
        Recipe recipe = adapter.getRecipe(randomPos);

        //Checking if that randomPos is correct
        assertNotNull(recipe);

        //Making click on that randomPos
        onView(withId(R.id.rv_select_recipes))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition(randomPos, click()));

        //Checking if we got correct outgoing intent containing correct Recipe object
        intended(
                allOf(
                hasExtra(is(RecipeDetailsActivity.ARG_RECIPE),is(recipe)),
                toPackage(RecipeDetailsActivity.class.getPackage().getName())
                )
                );
    }

    @After
    public void unregisterIdlingResource(){
        IdlingRegistry.getInstance().unregister(mIdlingResource);
    }
}