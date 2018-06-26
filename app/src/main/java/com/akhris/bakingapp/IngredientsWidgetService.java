package com.akhris.bakingapp;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.akhris.bakingapp.Model.Recipe;

public class IngredientsWidgetService extends RemoteViewsService {
    public static final String EXTRA_RECIPE_ID = "extra_recipe_id";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        int recipeId = intent.getIntExtra(EXTRA_RECIPE_ID, Recipe.INVALID_ID);
        if(recipeId==Recipe.INVALID_ID){return null;}
        return new IngredientsListFactory(this, recipeId);
    }
}
