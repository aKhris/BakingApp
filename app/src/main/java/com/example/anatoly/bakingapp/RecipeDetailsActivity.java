package com.example.anatoly.bakingapp;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.anatoly.bakingapp.Model.Recipe;

public class RecipeDetailsActivity extends VideoFullscreenActivity
        implements RecipeDetailsFragment.SelectStepListener
{

    public static final String ARG_RECIPE = "recipe";
//    private static final String BUNDLE_RECIPE = "recipe";
    private static final String RECIPE_DETAILS_FRAGMENT_TAG = "recipe_details_fragment";
    private static final String STEP_DETAILS_FRAGMENT_TAG = "step_details_fragment";

    private Recipe recipe;

    FrameLayout stepDetailsContainer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        stepDetailsContainer = findViewById(R.id.fl_step_details_container);
        RecipeDetailsFragment recipeDetailsFragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        recipe = (Recipe) getIntent().getSerializableExtra(ARG_RECIPE);
        if (savedInstanceState==null && findViewById(R.id.fl_recipe_details_container)!=null){
            recipeDetailsFragment = new RecipeDetailsFragment();
            recipeDetailsFragment.setRecipe(recipe);
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.fl_recipe_details_container, recipeDetailsFragment, RECIPE_DETAILS_FRAGMENT_TAG)
                    .commit();
        }
//        if(isTablet()){
//            stepSelected(recipe.getSteps().get(0));    //Load first step fragment on the right side if it's a tablet
//        }

    }

    private boolean isTablet(){
        return stepDetailsContainer!=null;
    }


//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putSerializable(BUNDLE_RECIPE, recipe);
//    }

    @Override
    public void stepSelected(int stepIndex) {
        Toast.makeText(this, recipe.getSteps().get(stepIndex).getDescription(), Toast.LENGTH_SHORT).show();
        App.getApp().releasePlayer();
        if(isTablet()){     //use container to reload fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            StepDetailsFragment stepDetailsFragment = (StepDetailsFragment) fragmentManager.findFragmentByTag(STEP_DETAILS_FRAGMENT_TAG);
            if(stepDetailsFragment==null){
                stepDetailsFragment = new StepDetailsFragment();
                stepDetailsFragment.setParameters(recipe, stepIndex);
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.fl_step_details_container, stepDetailsFragment)
                        .commit();
            } else {
                stepDetailsFragment.setParameters(recipe, stepIndex);
                stepDetailsFragment.refreshViews();
            }



        } else {            //use intent to start new StepDetailsActivity
            Intent intent = new Intent (this, StepDetailsActivity.class);
            intent.putExtra(StepDetailsActivity.ARG_RECIPE, recipe);
            intent.putExtra(StepDetailsActivity.ARG_STEP_INDEX, stepIndex);
            startActivity(intent);
        }
    }

}
