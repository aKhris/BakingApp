package com.example.anatoly.bakingapp;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.anatoly.bakingapp.Model.Recipe;

public class MainActivity extends AppCompatActivity
    implements SelectRecipeFragment.SelectRecipeListener
{
    private static final String SELECT_RECIPE_FRAGMENT_TAG = "select_recipe_fragment";
    private SelectRecipeFragment selectRecipeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = getSupportFragmentManager();

        if(savedInstanceState == null){
            selectRecipeFragment = new SelectRecipeFragment();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.fl_select_recipe_container, selectRecipeFragment, SELECT_RECIPE_FRAGMENT_TAG)
                    .commit();
        } else {
            selectRecipeFragment = (SelectRecipeFragment) fragmentManager.findFragmentByTag(SELECT_RECIPE_FRAGMENT_TAG);
        }
    }

    @Override
    public void recipeSelected(Recipe recipe) {
        Intent intent = new Intent(this, RecipeDetailsActivity.class);
        intent.putExtra(RecipeDetailsActivity.ARG_RECIPE, recipe);
        startActivity(intent);
    }
}
