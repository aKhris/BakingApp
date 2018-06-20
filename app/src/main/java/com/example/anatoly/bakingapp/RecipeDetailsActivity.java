package com.example.anatoly.bakingapp;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.example.anatoly.bakingapp.Model.Recipe;

public class RecipeDetailsActivity extends AppCompatActivity
        implements RecipeDetailsFragment.SelectStepListener
{

    public static final String ARG_RECIPE = "recipe";
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
        if(getSupportActionBar()!=null) {
            getSupportActionBar().setTitle(recipe.getName());
        }
    }

    public boolean isTablet(){
        return stepDetailsContainer!=null;
    }



    @Override
    public void stepSelected(int stepIndex) {
        if(isTablet()){     //use container to reload fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            StepDetailsFragment stepDetailsFragment = (StepDetailsFragment) fragmentManager.findFragmentByTag(STEP_DETAILS_FRAGMENT_TAG);
            if(stepDetailsFragment==null){
                stepDetailsFragment = new StepDetailsFragment();
                stepDetailsFragment.setParameters(recipe, stepIndex);
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.fl_step_details_container, stepDetailsFragment, STEP_DETAILS_FRAGMENT_TAG)
                        .commit();
            } else {

                stepDetailsFragment.setParameters(recipe, stepIndex);
                stepDetailsFragment.actualizeMediaSource();
//                stepDetailsFragment.refreshViews();
//                stepDetailsFragment.setPlayer();
//                stepDetailsFragment.setNoVideo(false);
            }



        } else {            //use intent to start new StepDetailsActivity
            Intent intent = new Intent (this, StepDetailsActivity.class);
            intent.putExtra(StepDetailsActivity.ARG_RECIPE, recipe);
            intent.putExtra(StepDetailsActivity.ARG_STEP_INDEX, stepIndex);
            startActivity(intent);
        }
    }

}
