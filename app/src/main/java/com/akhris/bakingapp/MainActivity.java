package com.akhris.bakingapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.akhris.bakingapp.Base.LoadRecipesActivity;
import com.akhris.bakingapp.Model.Recipe;
import com.akhris.bakingapp.R;
import com.akhris.bakingapp.Utils.NetworkUtils;
import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends LoadRecipesActivity
    implements SelectRecipeFragment.SelectRecipeListener

{
    @BindView(R.id.pb_loading_bar) ProgressBar progressBar;

    private static final String SELECT_RECIPE_FRAGMENT_TAG = "select_recipe_fragment";
    private SelectRecipeFragment selectRecipeFragment;

    @Nullable private static SimpleIdlingResource idlingResource;

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

        //Setting state of idlingResource to false since we are starting to download recipes
        if (idlingResource != null) {
            idlingResource.setIdleState(false);
        }
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
        selectRecipeFragment.setIdlingResource(idlingResource);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.fl_select_recipe_container, selectRecipeFragment, SELECT_RECIPE_FRAGMENT_TAG)
                .commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        idlingResource=null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.help_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_help:
                new LibsBuilder()
                        .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                        .start(this);
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Only called from test, creates and returns a new {@link SimpleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public static IdlingResource getIdlingResource() {
        if (idlingResource == null) {
            idlingResource = new SimpleIdlingResource();
        }
        return idlingResource;
    }
}
