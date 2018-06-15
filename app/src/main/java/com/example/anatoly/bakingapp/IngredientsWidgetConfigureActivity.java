package com.example.anatoly.bakingapp;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.example.anatoly.bakingapp.Adapters.RecipesListAdapter;
import com.example.anatoly.bakingapp.Model.Recipe;

import java.util.List;

/**
 * The configuration screen for the {@link IngredientsWidget IngredientsWidget} AppWidget.
 */
public class IngredientsWidgetConfigureActivity extends Activity implements RecyclerViewOnClickListener {

    private static final String PREFS_NAME = "com.example.anatoly.bakingapp.IngredientsWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    private int selectedPos=RecyclerView.NO_POSITION;

    private List<Recipe> recipes;


    private int getRecipePositionById (int id){
        for (int i = 0; i < recipes.size(); i++) {
            if(recipes.get(i).getId()==id){
                return i;
            }
        }
        return RecyclerView.NO_POSITION;
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            if(selectedPos==RecyclerView.NO_POSITION){
                finish();
            }

            final Context context = IngredientsWidgetConfigureActivity.this;

            // When the button is clicked, store the recipe id locally
            saveRecipeIdPref(context, mAppWidgetId, recipes.get(selectedPos).getId());

            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            IngredientsWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };


    public IngredientsWidgetConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveRecipeIdPref(Context context, int appWidgetId, int recipeId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putInt(PREF_PREFIX_KEY + appWidgetId, recipeId);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static int loadRecipeIdPref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getInt(PREF_PREFIX_KEY + appWidgetId, Recipe.INVALID_ID);
    }

    static void deleteRecipeIdPref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.ingredients_widget_configure);

        recipes = JsonUtils.getRecipesList(this);

        RecyclerView recipesRecyclerView = findViewById(R.id.rv_select_recipes_wca);
        int mColumnCount = getResources().getInteger(R.integer.columns_count);  //=3 for tablets otherwise =1

        RecipesListAdapter adapter = new RecipesListAdapter(recipes, true);
        adapter.setListener(this);
        adapter.setHasStableIds(true);
        recipesRecyclerView.setLayoutManager(new GridLayoutManager(this, mColumnCount, RecyclerView.VERTICAL, false));
        recipesRecyclerView.setHasFixedSize(true);
        recipesRecyclerView.setAdapter(adapter);

        findViewById(R.id.add_button).setOnClickListener(mOnClickListener);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        adapter.selectItem(getRecipePositionById(loadRecipeIdPref(IngredientsWidgetConfigureActivity.this, mAppWidgetId)));
    }

    @Override
    public void itemClicked(int position) {
        selectedPos = position;
    }
}

