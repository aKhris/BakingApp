package com.example.anatoly.bakingapp;

import android.support.v4.app.FragmentManager;
import android.os.Bundle;

import com.example.anatoly.bakingapp.Base.VideoFullscreenActivity;
import com.example.anatoly.bakingapp.Model.Recipe;

public class StepDetailsActivity extends VideoFullscreenActivity
         {

    //String tag for fragment
    private static final String STEP_DETAILS_FRAGMENT_TAG = "step_details_fragment";

    //String key for Step instance stored in calling intent

    public static final String ARG_RECIPE = "recipe";
    public static final String ARG_STEP_INDEX = "step_index";
    public static final String BUNDLE_STEP_INDEX = "step_index";

    private Recipe recipe;
    private int stepIndex;

    StepDetailsFragment stepDetailsFragment;


    /**
     * Checking if there is a StepDetailsFragment instance in FragmentManager.
     * If not, create new one and put it into container.
     * @param savedInstanceState - bundle that we are not using here, because
     *                           the saving of the state is handling by the StepDetailsFragment
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        checkFullscreen();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);
        if(getIntent()!=null){
            recipe = (Recipe) getIntent().getSerializableExtra(ARG_RECIPE);
            stepIndex = getIntent().getIntExtra(ARG_STEP_INDEX,0);
        }

        if(savedInstanceState!=null){
            stepIndex = savedInstanceState.getInt(BUNDLE_STEP_INDEX);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        stepDetailsFragment = (StepDetailsFragment) fragmentManager.findFragmentByTag(STEP_DETAILS_FRAGMENT_TAG);
        if(stepDetailsFragment==null){
            stepDetailsFragment = new StepDetailsFragment();
            stepDetailsFragment.setParameters(recipe, stepIndex);
                        fragmentManager
                    .beginTransaction()
                    .replace(R.id.fl_step_details_container, stepDetailsFragment, STEP_DETAILS_FRAGMENT_TAG)
                    .commit();
        }
    }



    /**
     * If activity is being destroyed during orientation change we still want to use
     * the same SimpleExoPlayer (that is stored in App class).
     * If activity is going to destroy completely we have to release that player.
     * Solution got here: https://stackoverflow.com/a/9621078
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isFinishing()) {
            // do stuff
            ((App)(getApplicationContext())).releasePlayer();
        } else {
            //It's an orientation change.
        }
    }

             @Override
             protected void onSaveInstanceState(Bundle outState) {
                 super.onSaveInstanceState(outState);
                 outState.putInt(BUNDLE_STEP_INDEX, stepIndex);
             }

         }
