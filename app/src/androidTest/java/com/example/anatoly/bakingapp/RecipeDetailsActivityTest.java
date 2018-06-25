package com.example.anatoly.bakingapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;

import com.example.anatoly.bakingapp.Adapters.StepsListAdapter;
import com.example.anatoly.bakingapp.Model.Recipe;
import com.example.anatoly.bakingapp.Model.Step;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


/**
 * Got how to override getActivityIntent() method here:
 * http://blog.xebia.com/android-intent-extras-espresso-rules/
 */

@RunWith(AndroidJUnit4.class)
public class RecipeDetailsActivityTest {
    @Rule
    public IntentsTestRule<RecipeDetailsActivity> mActivityTestRule =
            new IntentsTestRule<RecipeDetailsActivity>(RecipeDetailsActivity.class, true, false){
                @Override
                protected Intent getActivityIntent() {
                    mRecipe = TestUtils.getMockRecipe();
                    Intent intent = new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(), RecipeDetailsActivity.class);
                    intent.putExtra(RecipeDetailsActivity.ARG_RECIPE, mRecipe);
                    return intent;
                }
            };


    private Recipe mRecipe;
    private boolean isTablet = false;
    private RecipeDetailsActivity mActivity;


    /**
     * If we pass a null to .launchActivity method it will take intent from our overriden function
     * getActivityIntent()
     */
    @Before
    public void init(){
        mActivity = mActivityTestRule.launchActivity(null);
        isTablet = isTablet();
    }


    /**
     * Test clicking on steps list item depending on device (tablet/phone)
     */
    @Test
    public void clickStepsListItem_ItemIsShowed(){
        if(isTablet){
            clickStepsListItem_StepDetailsFragmentIsRefreshed();
        } else {
            clickStepsListItem_newStepDetailsActivityIsStarted();
        }
    }

    /**
     * Checking if we have correct intent after selecting recipe's step.
     * Used in a phone version.
     */
    public void clickStepsListItem_newStepDetailsActivityIsStarted(){
        int randomPos = makeClickOnFirstItem();
        //Checking if we got correct outgoing intent containing correct Recipe object
        intended(
                allOf(
                        hasExtra(is(StepDetailsActivity.ARG_RECIPE),is(mRecipe)),
                        hasExtra(is(StepDetailsActivity.ARG_STEP_INDEX),is(randomPos)),
                        toPackage(StepDetailsActivity.class.getPackage().getName())
                )
        );

    }

    /**
     * Checking if fragment gets correct recipe and stepIndex variables.
     * Used in a tablet version.
     */
    public void clickStepsListItem_StepDetailsFragmentIsRefreshed(){
        int randomPos = makeClickOnFirstItem();
        StepDetailsFragment fragment = (StepDetailsFragment)
                mActivity.getSupportFragmentManager().findFragmentByTag(RecipeDetailsActivity.STEP_DETAILS_FRAGMENT_TAG);

        //Checking if fragment exists
        assertNotNull(fragment);

        //Checking if it contains correct recipe
        assertEquals(mRecipe, fragment.getRecipe());

        //Checking if it contains correct step
        assertEquals(randomPos, fragment.getStepIndex());
    }


    /**
     * Initially it has to make a click on a random position, but I had some issues
     * when that position is out of the screen.
     * RecyclerViewActions.scrollToPosition doesn't help, maybe because I use
     * recyclerview inside NestedScrollView.
     * That is why I comment out some lines here and just click on the first item.
     */
    private int makeClickOnFirstItem(){
    RecyclerView stepsRecyclerView = mActivity.findViewById(R.id.rv_recipe_details);
    StepsListAdapter adapter = (StepsListAdapter) stepsRecyclerView.getAdapter();
    //    int itemCount = adapter.getItemCount();
    //    Random ran = new Random();
    //        int randomPos = ran.nextInt(itemCount);
    int randomPos = 0;
    Step step = adapter.getStep(randomPos);

    //Checking if that randomPos is correct
    assertNotNull(step);

    //Making click on that randomPos
    onView(withId(R.id.rv_recipe_details))
            .perform(RecyclerViewActions
                             .actionOnItemAtPosition(randomPos, click()));
        return randomPos;
    }

    /**
     * Method returns true if it's a tablet
     * Solution got here:
     * https://stackoverflow.com/a/11330947/7635275
     */

    private boolean isTablet() {
        return (InstrumentationRegistry.getContext().getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
