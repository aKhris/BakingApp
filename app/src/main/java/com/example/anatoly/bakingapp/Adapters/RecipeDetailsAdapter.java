package com.example.anatoly.bakingapp.Adapters;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.anatoly.bakingapp.Model.Ingredient;
import com.example.anatoly.bakingapp.Model.Recipe;
import com.example.anatoly.bakingapp.Model.Step;
import com.example.anatoly.bakingapp.R;
import com.example.anatoly.bakingapp.RecyclerViewOnClickListener;
import com.example.anatoly.bakingapp.SelectableRecyclerViewAdapter;


/**
 * Method of highlighting selected item got here:
 * https://stackoverflow.com/questions/27194044/how-to-properly-highlight-selected-item-on-recyclerview
 */
public class RecipeDetailsAdapter extends SelectableRecyclerViewAdapter<RecyclerView.ViewHolder>{

    private static final int VIEWTYPE_INGREDIENTS=0;
    private static final int VIEWTYPE_STEP=1;

    private Recipe recipe;

    private RecyclerViewOnClickListener listener;

    public RecipeDetailsAdapter(Recipe recipe) {
        this.recipe = recipe;
    }

    public void setListener(RecyclerViewOnClickListener listener) {
        this.listener = listener;
    }


    @Override
    public boolean isSelectable() {
        return true;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case VIEWTYPE_INGREDIENTS:
                View ingView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredients_card_layout, parent, false);
                return new IngredientsViewHolder(ingView);
            default:
                View stepView = LayoutInflater.from(parent.getContext()).inflate(R.layout.step_card_layout, parent, false);
                return new StepViewHolder(stepView);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        switch (getItemViewType(position)){
            case VIEWTYPE_INGREDIENTS:
                IngredientsViewHolder ingHolder = (IngredientsViewHolder) holder;
                ingHolder.tvIngredients.setText(Ingredient.getIngredientsList(recipe.getIngredients()));
                break;
            case VIEWTYPE_STEP:
                StepViewHolder stepHolder = (StepViewHolder) holder;
                Step step = recipe.getSteps().get(position-1);
                stepHolder.tvStepName.setText(step.getShortDescription());
                if(position>1) {
                    stepHolder.tvStepNumber.setText(String.valueOf(position - 1 + "."));
                } else {
                    stepHolder.tvStepNumber.setText("");
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return recipe.getSteps().size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return VIEWTYPE_INGREDIENTS;
        } else {
            return VIEWTYPE_STEP;
        }
    }

    @Override
    public void itemClicked(int position) {
        if(listener!=null){
            listener.itemClicked(position);
        }
    }

    class StepViewHolder extends SelectableViewHolder{
        TextView tvStepName;
        TextView tvStepNumber;

        StepViewHolder(View itemView) {
            super(itemView);
            tvStepName = itemView.findViewById(R.id.tv_step_name);
            tvStepNumber = itemView.findViewById(R.id.tv_step_number);
        }
    }

    class IngredientsViewHolder extends RecyclerView.ViewHolder{
        TextView tvIngredients;

        IngredientsViewHolder(View itemView){
            super(itemView);
            tvIngredients = itemView.findViewById(R.id.tv_ingredients);
        }

    }

}
