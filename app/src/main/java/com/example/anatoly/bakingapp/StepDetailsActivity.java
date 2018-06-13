package com.example.anatoly.bakingapp;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.anatoly.bakingapp.Model.Recipe;
import com.example.anatoly.bakingapp.Model.Step;

import static android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
import static android.view.View.SYSTEM_UI_FLAG_IMMERSIVE;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
import static android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN;
import static android.view.WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

public class StepDetailsActivity extends VideoFullscreenActivity
    implements StepDetailsFragment.SetStepListener
         {

    //String tag for fragment
    private static final String STEP_DETAILS_FRAGMENT_TAG = "step_details_fragment";

    //String key for Step instance stored in calling intent
    public static final String ARG_STEP = "step";
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
            stepDetailsFragment.setStep(recipe.getmSteps().get(stepIndex));
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
            App.getApp().releasePlayer();
        } else {
            //It's an orientation change.
        }
    }

             @Override
             protected void onSaveInstanceState(Bundle outState) {
                 super.onSaveInstanceState(outState);
                 outState.putInt(BUNDLE_STEP_INDEX, stepIndex);
             }

             // TODO: 13.06.18 Нарисовать схему фрагментов и активностей
             // и внедрить смену шага

             @Override
             public void setStep(boolean isNext) {
                int stepCount = recipe.getmSteps().size();
                stepIndex = isNext? stepIndex+1 : stepIndex-1;
                if(stepIndex>stepCount-1){stepIndex=0;}
                else if (stepIndex<0){stepIndex = stepCount-1;}
                stepDetailsFragment.setStep(recipe.getmSteps().get(stepIndex));
                getSupportFragmentManager()
                        .beginTransaction()
                        .detach(stepDetailsFragment)
                        .attach(stepDetailsFragment)
                        .commit();
             }
         }
