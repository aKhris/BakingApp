package com.akhris.bakingapp;


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

import com.akhris.bakingapp.Adapters.RecipesListAdapter;
import com.akhris.bakingapp.Model.Recipe;
import com.akhris.bakingapp.R;

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


    private static final String BUNDLE_SCROLL_POSITION = "scroll_position";
    private static final String BUNDLE_RECIPES = "recipes";
    private int mColumnCount=1;
    @Nullable private SimpleIdlingResource idlingResource;

    ArrayList<Recipe> recipes;
    SelectRecipeListener listener;

    public SelectRecipeFragment() {
        // Required empty public constructor
    }

    public void setRecipes(ArrayList<Recipe> recipes) {
        this.recipes = recipes;
    }

    public void setIdlingResource(@Nullable SimpleIdlingResource idlingResource) {
        this.idlingResource = idlingResource;
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
    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mColumnCount = getResources().getInteger(R.integer.columns_count);  //1 or 2 or 3 depending on a value in integers.xml
        if (savedInstanceState != null && savedInstanceState.containsKey(BUNDLE_RECIPES)) {
            recipes = (ArrayList<Recipe>) savedInstanceState.getSerializable(BUNDLE_RECIPES);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
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
        if (savedInstanceState != null && savedInstanceState.containsKey(BUNDLE_SCROLL_POSITION)) {
            int position = savedInstanceState.getInt(BUNDLE_SCROLL_POSITION);
            recipesRecyclerView.getLayoutManager().scrollToPosition(position);
        }

        //Setting idlingResource state to true
        //since we are ready for clicking on recyclerview items
        if (idlingResource != null) {
            idlingResource.setIdleState(true);
        }
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
        outState.putSerializable(BUNDLE_RECIPES, recipes);
        outState.putInt(BUNDLE_SCROLL_POSITION,
                ((GridLayoutManager)recipesRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition());
    }
}
