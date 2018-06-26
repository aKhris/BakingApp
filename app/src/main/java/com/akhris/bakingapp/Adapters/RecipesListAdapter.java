package com.akhris.bakingapp.Adapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.akhris.bakingapp.Model.Recipe;
import com.akhris.bakingapp.R;
import com.akhris.bakingapp.RecyclerViewOnClickListener;
import com.akhris.bakingapp.Base.SelectableRecyclerViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

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
        holder.tvRecipeServings.setText(
                String.format(Locale.getDefault(), "%s: %d", holder.itemView.getContext().getString(R.string.servings_caption), recipe.getServings())
        );
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
        @BindView(R.id.iv_recipe_image) ImageView ivRecipeImage;
        @BindView(R.id.tv_recipe_name) TextView tvRecipeName;
        @BindView(R.id.tv_recipe_servings) TextView tvRecipeServings;

        RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @VisibleForTesting
    @Nullable
    public Recipe getRecipe (int position){
        if(position<getItemCount()){
            return recipes.get(position);
        }
        return null;
    }


}
