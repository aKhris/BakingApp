package com.example.anatoly.bakingapp;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.anatoly.bakingapp.Adapters.RecipesListAdapter;
import com.example.anatoly.bakingapp.Model.Recipe;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SelectRecipeFragment extends Fragment
    implements RecyclerViewOnClickListener
{

    private static final String ARG_RECIPES = "recipes";

    ArrayList<Recipe> recipes;
    SelectRecipeListener listener;

    public SelectRecipeFragment() {
        // Required empty public constructor
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
        if (savedInstanceState != null && savedInstanceState.containsKey(ARG_RECIPES)) {
            recipes = (ArrayList<Recipe>) savedInstanceState.getSerializable(ARG_RECIPES);
        } else {
            recipes = JsonUtils.getRecipesList(getContext());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_select_recipe, container, false);
        RecyclerView recipesRecyclerView = view.findViewById(R.id.rv_select_recipes);
        RecipesListAdapter adapter = new RecipesListAdapter(recipes);
        adapter.setListener(this);
        adapter.setHasStableIds(true);
        recipesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
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
