package com.akhris.bakingapp.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akhris.bakingapp.R;
import com.akhris.bakingapp.Model.Ingredient;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsListAdapter extends RecyclerView.Adapter<IngredientsListAdapter.IngredientsViewHolder> {

    private List<Ingredient> ingredients;

    public IngredientsListAdapter(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public IngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View ingView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredients_item_layout, parent, false);
        return new IngredientsViewHolder(ingView);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);
        holder.number.setText(
                String.format(Locale.getDefault(), "%d. ", position+1)
        );
        holder.name.setText(ingredient.getIngredient());
        holder.quantity.setText(
                String.format(Locale.getDefault(), "%d %s", ingredient.getQuantity(), ingredient.getMeasure()));
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class IngredientsViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_ingredient_name) TextView name;
        @BindView(R.id.tv_ingredient_number) TextView number;
        @BindView(R.id.tv_ingredient_quantity) TextView quantity;

        IngredientsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
