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
import com.example.anatoly.bakingapp.SelectableRecyclerViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipesListAdapter extends SelectableRecyclerViewAdapter<RecyclerView.ViewHolder>{

    private List<Recipe> recipes;
    private RecyclerViewOnClickListener listener;
    private boolean isSelectable=false;


    public RecipesListAdapter(List<Recipe> recipes, boolean isSelectable) {
        this.recipes = recipes;
        this.isSelectable = isSelectable;
    }

    public void setListener(RecyclerViewOnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean isSelectable() {
        return isSelectable;
    }

    @Override
    public void itemClicked(int position) {
        if(listener!=null){
            listener.itemClicked(position);
        }
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_card_layout, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        super.onBindViewHolder(viewHolder, position);
        Recipe recipe = recipes.get(position);
        if(!(viewHolder instanceof RecipeViewHolder)){return;}
        RecipeViewHolder holder = (RecipeViewHolder)viewHolder;
        holder.tvRecipeName.setText(recipe.getName());
        if(recipe.getImageUrl().length()>0) {
            Picasso.get()
                    .load(recipe.getImageUrl())
                    .into(holder.ivRecipeImage);
        }
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }




    class RecipeViewHolder extends SelectableViewHolder{
        ImageView ivRecipeImage;
        TextView tvRecipeName;

        RecipeViewHolder(View itemView) {
            super(itemView);
            ivRecipeImage = itemView.findViewById(R.id.iv_recipe_image);
            tvRecipeName = itemView.findViewById(R.id.tv_recipe_name);
        }
    }




}
