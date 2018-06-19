package com.example.anatoly.bakingapp.Adapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.example.anatoly.bakingapp.Base.SelectableRecyclerViewAdapter;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Method of highlighting selected item got here:
 * https://stackoverflow.com/questions/27194044/how-to-properly-highlight-selected-item-on-recyclerview
 */
public class StepsListAdapter extends SelectableRecyclerViewAdapter<RecyclerView.ViewHolder>{

    private Recipe recipe;
    private boolean isSelectable = false;

    private RecyclerViewOnClickListener listener;

    public StepsListAdapter(Recipe recipe, boolean isSelectable) {
        this.recipe = recipe;
        this.isSelectable = isSelectable;
    }

    public void setListener(RecyclerViewOnClickListener listener) {
        this.listener = listener;
    }


    @Override
    public boolean isSelectable() {
        return isSelectable;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View stepView = LayoutInflater.from(parent.getContext()).inflate(R.layout.step_card_layout, parent, false);
                return new StepViewHolder(stepView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
                StepViewHolder stepHolder = (StepViewHolder) holder;
                Step step = recipe.getSteps().get(position);
                if(position>0 && stepHolder.tvStepNumber!=null){
                    stepHolder.tvStepNumber.setText(
                            String.format(Locale.getDefault(), "%d. ", position));
                }
                stepHolder.tvStepName.setText(step.getShortDescription());
                if(stepHolder.tvStepFullDescription!=null){
                    stepHolder.tvStepFullDescription.setText(step.getDescription());
                }
    }

    @Override
    public int getItemCount() {
        return recipe.getSteps().size();
    }


    @Override
    public void itemClicked(int position) {
        if(listener!=null){
            listener.itemClicked(position);
        }
    }

    class StepViewHolder extends SelectableViewHolder{
        @Nullable @BindView(R.id.tv_step_number) TextView tvStepNumber;
        @BindView(R.id.tv_step_name) TextView tvStepName;
        @Nullable @BindView(R.id.tv_step_full_description) TextView tvStepFullDescription;

        StepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
