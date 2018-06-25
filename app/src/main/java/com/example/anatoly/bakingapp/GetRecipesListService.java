package com.example.anatoly.bakingapp;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.example.anatoly.bakingapp.Model.Recipe;
import com.example.anatoly.bakingapp.Utils.JsonUtils;

import java.util.ArrayList;

public class GetRecipesListService extends IntentService {

    public static final String ACTION_UPDATE_WIDGETS = "com.example.anatoly.bakingapp.action.update_widgets";

    private static final String SERVICE_NAME = GetRecipesListService.class.getSimpleName();

    public GetRecipesListService() {
        super(SERVICE_NAME);
    }

    public static void startActionDownloadRecipesList(Context context){
        Intent intent = new Intent(context, GetRecipesListService.class);
        intent.setAction(ACTION_UPDATE_WIDGETS);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent==null || intent.getAction()==null){return;}
        if(intent.getAction().equals(ACTION_UPDATE_WIDGETS)){
            handleActionUpdateWidgets();
        }
    }

    private void handleActionUpdateWidgets(){

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, IngredientsWidget.class));
        ArrayList<Recipe> recipes = JsonUtils.getRecipesList();

        for (int appWidgetId:appWidgetIds) {
            int recipeId = IngredientsWidgetConfigureActivity.loadRecipeIdPref(this, appWidgetId);
            Recipe recipe = getRecipeById(recipes, recipeId);
            if(recipe!=null) {
                IngredientsWidget.updateAppWidget(this, appWidgetManager, appWidgetId, recipe);
            }
        }

    }

    @Nullable
    private static Recipe getRecipeById(ArrayList<Recipe> recipes, int id){
        for (Recipe recipe:recipes) {
            if(recipe.getId()==id){
                return recipe;
            }
        }
        return null;
    }
}
