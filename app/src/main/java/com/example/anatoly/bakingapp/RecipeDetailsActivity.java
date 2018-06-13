package com.example.anatoly.bakingapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.anatoly.bakingapp.Model.Recipe;
import com.example.anatoly.bakingapp.Model.Step;

public class RecipeDetailsActivity extends VideoFullscreenActivity
        implements RecipeDetailsFragment.SelectStepListener,
        StepDetailsFragment.SetStepListener
{

    public static final String ARG_RECIPE = "recipe";
//    private static final String BUNDLE_RECIPE = "recipe";
    private static final String RECIPE_DETAILS_FRAGMENT_TAG = "recipe_details_fragment";

    FrameLayout stepDetailsContainer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        stepDetailsContainer = findViewById(R.id.fl_step_details_container);
        RecipeDetailsFragment recipeDetailsFragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        Recipe recipe = (Recipe) getIntent().getSerializableExtra(ARG_RECIPE);
        if (savedInstanceState==null && findViewById(R.id.fl_recipe_details_container)!=null){
            recipeDetailsFragment = new RecipeDetailsFragment();
            recipeDetailsFragment.setRecipe(recipe);
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.fl_recipe_details_container, recipeDetailsFragment, RECIPE_DETAILS_FRAGMENT_TAG)
                    .commit();
        }
        if(isTablet()){
            stepSelected(recipe.getmSteps().get(0));    //Load first step fragment on the right side if it's a tablet
        }

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
    public void stepSelected(Step step) {
        Toast.makeText(this, step.getmDescription(), Toast.LENGTH_SHORT).show();
        App.getApp().releasePlayer();
        if(isTablet()){     //use container to reload fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            StepDetailsFragment stepDetailsFragment = new StepDetailsFragment();
            stepDetailsFragment.setStep(step);
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.fl_step_details_container, stepDetailsFragment)
                    .commit();

        } else {            //use intent to start new StepDetailsActivity
            Intent intent = new Intent (this, StepDetailsActivity.class);
            intent.putExtra(StepDetailsActivity.ARG_STEP, step);
            startActivity(intent);
        }
    }

    @Override
    public void setStep(boolean isNext) {

    }
}
