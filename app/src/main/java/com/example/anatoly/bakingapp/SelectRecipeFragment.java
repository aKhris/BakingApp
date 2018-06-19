package com.example.anatoly.bakingapp;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.anatoly.bakingapp.Adapters.RecipesListAdapter;
import com.example.anatoly.bakingapp.Model.Recipe;
import com.example.anatoly.bakingapp.Utils.JsonUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class SelectRecipeFragment extends Fragment
    implements RecyclerViewOnClickListener
{
    @BindView(R.id.rv_select_recipes) RecyclerView recipesRecyclerView;

    private static final String ARG_RECIPES = "recipes";
    private int mColumnCount=1;

    ArrayList<Recipe> recipes;
    SelectRecipeListener listener;

    public SelectRecipeFragment() {
        // Required empty public constructor
    }

    public void setRecipes(ArrayList<Recipe> recipes) {
        this.recipes = recipes;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof SelectRecipeListener){
            listener = (SelectRecipeListener)context;
        } else {
            throw new UnsupportedOperationException("Activity must implement SelectRecipeListener.");
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mColumnCount = getResources().getInteger(R.integer.columns_count);  //=3 for tablets otherwise =1
        if (savedInstanceState != null && savedInstanceState.containsKey(ARG_RECIPES)) {
            recipes = (ArrayList<Recipe>) savedInstanceState.getSerializable(ARG_RECIPES);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_select_recipe, container, false);
        ButterKnife.bind(this, view);
        RecipesListAdapter adapter = new RecipesListAdapter(recipes, false);
        adapter.setListener(this);
        adapter.setHasStableIds(true);
        recipesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), mColumnCount, RecyclerView.VERTICAL, false));
        recipesRecyclerView.setHasFixedSize(true);
        recipesRecyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void itemClicked(int position) {
        listener.recipeSelected(recipes.get(position));
    }

    interface SelectRecipeListener{
        void recipeSelected(Recipe recipe);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ARG_RECIPES, recipes);
    }
}
