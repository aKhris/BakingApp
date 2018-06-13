package com.example.anatoly.bakingapp.Adapters;

import android.support.annotation.NonNull;
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

public class RecipeDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEWTYPE_INGREDIENTS=0;
    private static final int VIEWTYPE_STEP=1;

    private Recipe recipe;

    RecyclerViewOnClickListener listener;

    public RecipeDetailsAdapter(Recipe recipe) {
        this.recipe = recipe;
    }

    public void setListener(RecyclerViewOnClickListener listener) {
        this.listener = listener;
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
        switch (getItemViewType(position)){
            case VIEWTYPE_INGREDIENTS:
                IngredientsViewHolder ingHolder = (IngredientsViewHolder) holder;
                ingHolder.tvIngredients.setText(Ingredient.getIngredientsList(recipe.getmIngredients()));
                break;
            case VIEWTYPE_STEP:
                StepViewHolder stepHolder = (StepViewHolder) holder;
                Step step = recipe.getmSteps().get(position-1);
                stepHolder.tvStepName.setText(step.getmShortDescription());
                stepHolder.tvStepNumber.setText(String.valueOf(position+"."));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return recipe.getmSteps().size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return VIEWTYPE_INGREDIENTS;
        } else {
            return VIEWTYPE_STEP;
        }
    }

    class StepViewHolder extends RecyclerView.ViewHolder{
        TextView tvStepName;
        TextView tvStepNumber;

        StepViewHolder(View itemView) {
            super(itemView);
            tvStepName = itemView.findViewById(R.id.tv_step_name);
            tvStepNumber = itemView.findViewById(R.id.tv_step_number);
            tvStepName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        listener.itemClicked(getAdapterPosition());
                    }
                }
            });
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
