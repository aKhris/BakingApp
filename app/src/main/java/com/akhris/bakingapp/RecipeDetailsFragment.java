package com.akhris.bakingapp;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.akhris.bakingapp.Adapters.IngredientsListAdapter;
import com.akhris.bakingapp.Adapters.StepsListAdapter;
import com.akhris.bakingapp.Model.Recipe;
import com.akhris.bakingapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeDetailsFragment extends Fragment {

    @BindView(R.id.rv_recipe_details) RecyclerView detailsRecyclerView;
    @BindView(R.id.rv_ingredients_list) RecyclerView ingredientsRecyclerView;

    private static final String BUNDLE_RECIPE = "recipe";
    private static final String BUNDLE_IS_TABLET = "is_tablet";

    private boolean isTablet=false;
    private RecipeDetailsListener recipeDetailsListener;
    private Recipe recipe;
    private StepsListAdapter adapter;


    public RecipeDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof RecipeDetailsListener){
            recipeDetailsListener = (RecipeDetailsListener) context;
        } else {
            throw new UnsupportedOperationException("Activity must implement RecipeDetailsListener.");
        }
        RecipeDetailsActivity activity = (RecipeDetailsActivity)context;
        isTablet = activity.isTablet();
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null && savedInstanceState.containsKey(BUNDLE_RECIPE)){
            recipe = (Recipe) savedInstanceState.getSerializable(BUNDLE_RECIPE);
            isTablet = savedInstanceState.getBoolean(BUNDLE_IS_TABLET);
        }

    }


    /**
     * How to add divider to ingredientsRecyclerView got here:
     * https://stackoverflow.com/a/27037230
     */

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_recipe_details, container, false);
        ButterKnife.bind(this, rootView);

        detailsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        detailsRecyclerView.setHasFixedSize(true);

        adapter = new StepsListAdapter(recipe, isTablet);
        adapter.setListener(new RecyclerViewOnClickListener() {
            @Override
            public void itemClicked(int position) {
                recipeDetailsListener.onStepSelected(position);
            }
        });
        detailsRecyclerView.setAdapter(adapter);
        adapter.onRetainInstanceState(savedInstanceState);

        ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        ingredientsRecyclerView.setHasFixedSize(true);
        IngredientsListAdapter ingAdapter = new IngredientsListAdapter(recipe.getIngredients());
        ingAdapter.setHasStableIds(true);
        ingredientsRecyclerView.setAdapter(ingAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(ingredientsRecyclerView.getContext(),
                LinearLayoutManager.VERTICAL);
        ingredientsRecyclerView.addItemDecoration(dividerItemDecoration);
        if(savedInstanceState==null) {
            recipeDetailsListener.onRecipeDetailsFragmentFirstInit(this);
        }
        return rootView;
    }


    /**
     * Force recipe's step selection.
     * Used for initial selection of first step when user opens the recipe activity on a tablet.
     */
    public void selectItem(int position){
        adapter.selectItem(position);
        adapter.itemClicked(position);
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(BUNDLE_RECIPE, recipe);
        outState.putBoolean(BUNDLE_IS_TABLET, isTablet);
        adapter.onSaveInstanceState(outState);
    }

    interface RecipeDetailsListener {
        void onStepSelected(int stepIndex);
        void onRecipeDetailsFragmentFirstInit(RecipeDetailsFragment fragment);
    }
}
