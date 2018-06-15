package com.example.anatoly.bakingapp;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.anatoly.bakingapp.Model.Recipe;
import com.example.anatoly.bakingapp.Model.Step;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;


/**
 * A simple {@link Fragment} subclass.
 */
public class StepDetailsFragment extends Fragment
    implements View.OnClickListener
{

    private static final String LOG_TAG = StepDetailsFragment.class.getSimpleName();
    private static final String BUNDLE_STEP = "step";
    private static final String BUNDLE_STEP_INDEX = "step_index";
    private static final String BUNDLE_RECIPE = "recipe";

    private Recipe recipe;
    private int stepIndex;
    private TextView stepDescription;
    private PlayerView mPlayerView;


    public StepDetailsFragment() {
        // Required empty public constructor
    }

    public void setParameters(Recipe recipe, int stepIndex) {
        this.recipe = recipe;
        this.stepIndex = stepIndex;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null){
            this.recipe = (Recipe) savedInstanceState.getSerializable(BUNDLE_RECIPE);
            this.stepIndex = savedInstanceState.getInt(BUNDLE_STEP_INDEX, 0);
        }
    }

    /**
     * Method of how to get instance of PlayerControlView partially got here:
     * https://geoffledak.com/blog/2017/09/11/how-to-add-a-fullscreen-toggle-button-to-exoplayer-in-android/
     *
     * But it seems that we need PlayerControlView replacing it's placeholder in XML, not just put it beside.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_step_details, container, false);

        stepDescription = rootView.findViewById(R.id.tv_step_description);
        mPlayerView = rootView.findViewById(R.id.pv_video_player);

        PlayerControlView controlView = mPlayerView.findViewById(R.id.exo_controller);
        ImageButton prevButton = controlView.findViewById(R.id.exo_prev_custom);
        ImageButton nextButton = controlView.findViewById(R.id.exo_next_custom);
        if(prevButton!=null) {
            prevButton.setOnClickListener(this);
        }
        if(nextButton!=null){
            nextButton.setOnClickListener(this);
        }

        refreshViews();

        return rootView;
    }

    public void refreshViews(){
        Step step = recipe.getSteps().get(stepIndex);

        if(stepDescription!=null) {
            stepDescription.setText(step.getDescription());
        }
        App.getApp().releasePlayer();
        mPlayerView.setPlayer(App.getApp().getExoPlayer(Uri.parse(step.getVideoUrl())));
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(BUNDLE_RECIPE, recipe);
        outState.putInt(BUNDLE_STEP_INDEX, stepIndex);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.exo_prev_custom:
                setNextStepIndex(false);
                break;
            case R.id.exo_next_custom:
                setNextStepIndex(true);
                break;
        }
        refreshViews();
    }

    private void setNextStepIndex(boolean isIncrement){
        int stepCount = recipe.getSteps().size();
        stepIndex = isIncrement? stepIndex+1 : stepIndex-1;
        if(stepIndex>stepCount-1){stepIndex=0;}
        else if (stepIndex<0){stepIndex = stepCount-1;}
    }

}
