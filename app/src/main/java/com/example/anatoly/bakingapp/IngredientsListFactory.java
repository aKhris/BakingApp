package com.example.anatoly.bakingapp;

import android.content.Context;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.anatoly.bakingapp.Model.Ingredient;
import com.example.anatoly.bakingapp.Model.Recipe;
import com.example.anatoly.bakingapp.Utils.JsonUtils;

import java.util.List;
import java.util.Locale;

public class IngredientsListFactory implements RemoteViewsService.RemoteViewsFactory {

    private int recipeId;
    private List<Ingredient> ingredients;
    private Context mContext;


    IngredientsListFactory(Context mContext, int recipeId) {
        this.recipeId = recipeId;
        this.mContext = mContext;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        Recipe recipe = JsonUtils.getRecipeById(recipeId);
        if(recipe==null){return;}
        ingredients = recipe.getIngredients();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (ingredients==null){return 0;}
        return ingredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Ingredient ingredient = ingredients.get(position);
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.ingredients_item_layout_widget);
        views.setTextViewText(R.id.tv_ingredient_number,
                String.format(Locale.getDefault(), "%d. ", position+1));
        views.setTextViewText(R.id.tv_ingredient_name, ingredient.getIngredient());
        views.setTextViewText(R.id.tv_ingredient_quantity,
                String.format(Locale.getDefault(), "%d %s", ingredient.getQuantity(), ingredient.getMeasure())
                );
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
