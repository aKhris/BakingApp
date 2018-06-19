package com.example.anatoly.bakingapp;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.anatoly.bakingapp.Base.LoadRecipesActivity;
import com.example.anatoly.bakingapp.Model.Recipe;
import com.example.anatoly.bakingapp.Utils.NetworkUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends LoadRecipesActivity
    implements SelectRecipeFragment.SelectRecipeListener

{
    @BindView(R.id.pb_loading_bar) ProgressBar progressBar;

    private static final String SELECT_RECIPE_FRAGMENT_TAG = "select_recipe_fragment";
    private SelectRecipeFragment selectRecipeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if(!NetworkUtils.isOnline(this)){
            Toast.makeText(this, R.string.check_connection, Toast.LENGTH_SHORT).show();
            return;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        selectRecipeFragment = (SelectRecipeFragment) fragmentManager.findFragmentByTag(SELECT_RECIPE_FRAGMENT_TAG);
        if(selectRecipeFragment==null) {
            selectRecipeFragment = new SelectRecipeFragment();
        }
        progressBar.setVisibility(View.VISIBLE);
        initLoader();
    }


    @Override
    public void recipeSelected(Recipe recipe) {
        Intent intent = new Intent(this, RecipeDetailsActivity.class);
        intent.putExtra(RecipeDetailsActivity.ARG_RECIPE, recipe);
        startActivity(intent);
    }


    @Override
    public void onRecipesLoaded(ArrayList<Recipe> recipes) {
        progressBar.setVisibility(View.GONE);
        selectRecipeFragment.setRecipes(recipes);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.fl_select_recipe_container, selectRecipeFragment, SELECT_RECIPE_FRAGMENT_TAG)
                .commit();
    }
}
