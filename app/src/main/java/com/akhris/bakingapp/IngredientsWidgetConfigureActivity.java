package com.akhris.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.akhris.bakingapp.Adapters.RecipesListAdapter;
import com.akhris.bakingapp.Base.LoadRecipesActivity;
import com.akhris.bakingapp.Model.Recipe;
import com.akhris.bakingapp.Utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * The configuration screen for the {@link IngredientsWidget IngredientsWidget} AppWidget.
 */
public class IngredientsWidgetConfigureActivity extends LoadRecipesActivity
        implements RecyclerViewOnClickListener
            {
                @BindView(R.id.rv_select_recipes_wca) RecyclerView recipesRecyclerView;
                @BindView(R.id.pb_loading_bar_wca) ProgressBar loadingRecipesBar;
                @BindView(R.id.add_button_wca) Button addWidgetButton;
    private static final String PREFS_NAME = "com.example.anatoly.bakingapp.IngredientsWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    public static final String EXTRA_IS_FROM_WIDGET="is_from_widget";

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private boolean isFromWidget = false;
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

        @OnClick(R.id.add_button_wca)
        public void onAddWidgetClick(View v) {
            if(selectedPos==RecyclerView.NO_POSITION){
                finish();
            }

            final Context context = IngredientsWidgetConfigureActivity.this;

            Recipe recipe = recipes.get(selectedPos);

            // When the button is clicked, store the recipe id locally
            saveRecipeIdPref(context, mAppWidgetId, recipe.getId());

            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            IngredientsWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId, recipe);
            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }



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
        int recipeId = prefs.getInt(PREF_PREFIX_KEY + appWidgetId, Recipe.INVALID_ID);
        return recipeId;
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
        ButterKnife.bind(this);

        if(!NetworkUtils.isOnline(this)) {
            Toast.makeText(this, R.string.check_connection, Toast.LENGTH_SHORT).show();
            return;
        }

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            isFromWidget = extras.getBoolean(
                    EXTRA_IS_FROM_WIDGET, false
            );
        }
        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        if(isFromWidget){
            addWidgetButton.setText(R.string.update_widget_button_text);
        }

        initLoader();
    }

    @Override
    public void itemClicked(int position) {
        selectedPos = position;
    }

    @Override
    public void onRecipesLoaded(ArrayList<Recipe> recipes) {
                    loadingRecipesBar.setVisibility(View.INVISIBLE);
                    this.recipes = recipes;
                    RecipesListAdapter adapter = new RecipesListAdapter(recipes, true);
                    adapter.setListener(this);
                    adapter.setHasStableIds(true);
                    int mColumnCount = getResources().getInteger(R.integer.columns_count);  //  1/2/3
                    recipesRecyclerView.setLayoutManager(new GridLayoutManager(this, mColumnCount, RecyclerView.VERTICAL, false));
                    recipesRecyclerView.setHasFixedSize(true);
                    recipesRecyclerView.setAdapter(adapter);
                    adapter.selectItem(getRecipePositionById(loadRecipeIdPref(this, mAppWidgetId)));
                }
            }

