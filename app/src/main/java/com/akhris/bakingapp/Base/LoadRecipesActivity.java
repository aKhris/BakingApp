package com.akhris.bakingapp.Base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;

import com.akhris.bakingapp.HTTPTaskLoader;
import com.akhris.bakingapp.Model.Recipe;

import java.util.ArrayList;

public abstract class LoadRecipesActivity extends AppCompatActivity
    implements LoaderManager.LoaderCallbacks<ArrayList<Recipe>> {
    private static final int RECIPES_LOADER_ID = 342;

    public void initLoader(){
        getSupportLoaderManager().initLoader(RECIPES_LOADER_ID, null, this);
    }

    public abstract void onRecipesLoaded(ArrayList<Recipe> recipes);

    @NonNull
    @Override
    public Loader<ArrayList<Recipe>> onCreateLoader(int id, @Nullable Bundle args) {
        return new HTTPTaskLoader(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Recipe>> loader, ArrayList<Recipe> data) {
        onRecipesLoaded(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Recipe>> loader) {

    }
}
