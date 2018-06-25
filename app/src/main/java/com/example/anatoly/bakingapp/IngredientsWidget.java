package com.example.anatoly.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.example.anatoly.bakingapp.Model.Recipe;

import java.util.Locale;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link IngredientsWidgetConfigureActivity IngredientsWidgetConfigureActivity}
 */
public class IngredientsWidget extends AppWidgetProvider
{

    /**
     * Method of setting a data to make intents from two or more widgets differ got here:
     * https://stackoverflow.com/a/50996294
     */
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, Recipe recipe) {
        if(recipe==null){return;}
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);

        Intent intent = new Intent(context, IngredientsWidgetService.class);
        intent.putExtra(IngredientsWidgetService.EXTRA_RECIPE_ID, recipe.getId());
        intent.setData(Uri.fromParts("content", String.valueOf(recipe.getId()), null));
        views.setRemoteAdapter(R.id.lv_ingredients_widget, intent);
        
        views.setTextViewText(R.id.tv_recipe_name_widget, recipe.getName());
        views.setTextViewText(R.id.tv_recipe_servings_widget,
                String.format(Locale.getDefault(), "%s: %d", context.getString(R.string.servings_caption), recipe.getServings()));

        Intent activityIntent = new Intent(context, RecipeDetailsActivity.class);
        activityIntent.putExtra(RecipeDetailsActivity.ARG_RECIPE, recipe);
        PendingIntent startMainActivityPendingIntent = PendingIntent.getActivity(context, 0, activityIntent, 0);
        views.setOnClickPendingIntent(R.id.tv_recipe_name_widget, startMainActivityPendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        GetRecipesListService.startActionDownloadRecipesList(context);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            IngredientsWidgetConfigureActivity.deleteRecipeIdPref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}

