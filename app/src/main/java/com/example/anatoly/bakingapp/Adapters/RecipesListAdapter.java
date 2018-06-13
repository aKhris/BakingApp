package com.example.anatoly.bakingapp.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anatoly.bakingapp.Model.Recipe;
import com.example.anatoly.bakingapp.R;
import com.example.anatoly.bakingapp.RecyclerViewOnClickListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipesListAdapter extends RecyclerView.Adapter<RecipesListAdapter.RecipeViewHolder> {

    List<Recipe> recipes;
    RecyclerViewOnClickListener listener;

    public RecipesListAdapter(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    public void setListener(RecyclerViewOnClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_card_layout, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.tvRecipeName.setText(recipe.getmName());
        if(recipe.getmImageUrl().length()>0) {
            Picasso.get()
                    .load(recipe.getmImageUrl())
                    .into(holder.ivRecipeImage);
        }
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }




    class RecipeViewHolder extends RecyclerView.ViewHolder{
        ImageView ivRecipeImage;
        TextView tvRecipeName;

        RecipeViewHolder(View itemView) {
            super(itemView);
            ivRecipeImage = itemView.findViewById(R.id.iv_recipe_image);
            tvRecipeName = itemView.findViewById(R.id.tv_recipe_name);
            tvRecipeName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        listener.itemClicked(getAdapterPosition());
                    }
                }
            });
        }
    }




}
