package com.akhris.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.akhris.bakingapp.Model.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailsActivity extends AppCompatActivity
        implements RecipeDetailsFragment.RecipeDetailsListener
{
    @BindView(R.id.nsv_steps_scroll) NestedScrollView scrollView;
    @Nullable @BindView(R.id.fl_step_details_container) FrameLayout stepDetailsContainer;

    private final static String BUNDLE_SCROLL_POSITION = "scroll_position";

    public static final String ARG_RECIPE = "recipe";
    private static final String RECIPE_DETAILS_FRAGMENT_TAG = "recipe_details_fragment";
    public static final String STEP_DETAILS_FRAGMENT_TAG = "step_details_fragment";

    @Nullable private Recipe recipe;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        ButterKnife.bind(this);

        recipe = (Recipe) getIntent().getSerializableExtra(ARG_RECIPE);
        if(recipe==null){return;}   //May be so under test

        if(getSupportActionBar()!=null) {
            getSupportActionBar().setTitle(recipe.getName());
        }


        RecipeDetailsFragment recipeDetailsFragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        recipeDetailsFragment = (RecipeDetailsFragment) fragmentManager.findFragmentByTag(RECIPE_DETAILS_FRAGMENT_TAG);
        if (recipeDetailsFragment==null){
            recipeDetailsFragment = new RecipeDetailsFragment();
            recipeDetailsFragment.setRecipe(recipe);
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.fl_recipe_details_container, recipeDetailsFragment, RECIPE_DETAILS_FRAGMENT_TAG)
                    .commit();
        }

        if(savedInstanceState!=null && savedInstanceState.containsKey(BUNDLE_SCROLL_POSITION)){
                final int[] scrollCoordinates = savedInstanceState.getIntArray(BUNDLE_SCROLL_POSITION);
                if (scrollCoordinates != null) {
                    scrollView.post(new Runnable() {
                        public void run() {
                            scrollView.scrollTo(scrollCoordinates[0], scrollCoordinates[1]);
                        }
                    });
                }
        }

    }

    public boolean isTablet(){
        return stepDetailsContainer!=null;
    }



    @Override
    public void onStepSelected(int stepIndex) {
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
                stepDetailsFragment.refreshViews();
            }



        } else {            //use intent to start new StepDetailsActivity
            Intent intent = new Intent (this, StepDetailsActivity.class);
            intent.putExtra(StepDetailsActivity.ARG_RECIPE, recipe);
            intent.putExtra(StepDetailsActivity.ARG_STEP_INDEX, stepIndex);
            startActivity(intent);
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray(BUNDLE_SCROLL_POSITION, new int[]{scrollView.getScrollX(), scrollView.getScrollY()});
    }

    @Override
    public void onRecipeDetailsFragmentFirstInit(RecipeDetailsFragment fragment) {
        if(isTablet()){
            fragment.selectItem(0);
        }
    }
}
