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

import com.example.anatoly.bakingapp.Adapters.RecipeDetailsAdapter;
import com.example.anatoly.bakingapp.Model.Recipe;
import com.example.anatoly.bakingapp.Model.Step;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeDetailsFragment extends Fragment {

    private static final String BUNDLE_RECIPE = "recipe";

    private SelectStepListener stepListener;
    private Recipe recipe;



    public RecipeDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof SelectStepListener){
            stepListener = (SelectStepListener) context;
        } else {
            throw new UnsupportedOperationException("Activity must implement SelectStepListener.");
        }
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null && savedInstanceState.containsKey(BUNDLE_RECIPE)){
            recipe = (Recipe) savedInstanceState.getSerializable(BUNDLE_RECIPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_recipes_detail, container, false);
        RecyclerView detailsRecyclerView = rootView.findViewById(R.id.rv_recipe_details);
        detailsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        detailsRecyclerView.setHasFixedSize(true);
        RecipeDetailsAdapter adapter = new RecipeDetailsAdapter(recipe);
        adapter.setListener(new RecyclerViewOnClickListener() {
            @Override
            public void itemClicked(int position) {
                stepListener.stepSelected(recipe.getmSteps().get(position-1));
            }
        });
        detailsRecyclerView.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(BUNDLE_RECIPE, recipe);
    }

    interface SelectStepListener{
        void stepSelected(Step step);
    }
}
