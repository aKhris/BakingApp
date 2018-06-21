package com.example.anatoly.bakingapp.Adapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

    /**
     * Using of regex got here:
     * http://www.vogella.com/tutorials/JavaRegularExpressions/article.html#using-regular-expressions-with-string-methods
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
                StepViewHolder stepHolder = (StepViewHolder) holder;
                Step step = recipe.getSteps().get(position);
                if(stepHolder.tvStepNumber!=null){
                    if(position>0){
                        stepHolder.tvStepNumber.setVisibility(View.VISIBLE);
                        stepHolder.ivStepImage.setVisibility(View.VISIBLE);
                        stepHolder.tvStepNumber.setText(
                                String.format(Locale.getDefault(), "%d", position));
                    } else {
                        stepHolder.tvStepNumber.setVisibility(View.INVISIBLE);
                        stepHolder.ivStepImage.setVisibility(View.INVISIBLE);
                    }
                }

                stepHolder.tvStepName.setText(step.getShortDescription());
                if(stepHolder.tvStepFullDescription!=null){
                    String description = step.getDescription();
                    String regex = "(\\d{1,3}\\.\\s)(.*)";
                    if(description.matches(regex)){
                        description = description.replaceAll(regex, "$2");
                    }
                    stepHolder.tvStepFullDescription.setText(description);
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
        @Nullable @BindView(R.id.iv_step_image) ImageView ivStepImage;
        @BindView(R.id.tv_step_name) TextView tvStepName;
        @Nullable @BindView(R.id.tv_step_full_description) TextView tvStepFullDescription;

        StepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
