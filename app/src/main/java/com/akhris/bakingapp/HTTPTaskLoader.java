package com.akhris.bakingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.akhris.bakingapp.Model.Recipe;
import com.akhris.bakingapp.Utils.JsonUtils;

import java.util.ArrayList;

public class HTTPTaskLoader extends AsyncTaskLoader<ArrayList<Recipe>> {
    private ArrayList<Recipe> cacheRecipes;    // List for caching recipes

    public HTTPTaskLoader(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        if(cacheRecipes!=null){
            deliverResult(cacheRecipes);
        } else {
            forceLoad();
        }
    }

    @Nullable
    @Override
    public ArrayList<Recipe> loadInBackground() {
        return JsonUtils.getRecipesList();
    }

    @Override
    public void deliverResult(@Nullable ArrayList<Recipe> recipes) {
        cacheRecipes = recipes;
        super.deliverResult(recipes);
    }
}
