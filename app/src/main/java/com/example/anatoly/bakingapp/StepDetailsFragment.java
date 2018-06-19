package com.example.anatoly.bakingapp;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anatoly.bakingapp.Base.VideoFragment;
import com.example.anatoly.bakingapp.Model.Recipe;
import com.example.anatoly.bakingapp.Model.Step;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Fragment containing video player and step description textview.
 * Extends from {@link VideoFragment} to save playback state in
 * onPause and restore it in onResume.
 */
public class StepDetailsFragment extends VideoFragment
{
    @Nullable @BindView(R.id.tv_step_description) TextView stepDescription;
    @BindView(R.id.pv_video_player) PlayerView mPlayerView;

    private static final String BUNDLE_STEP_INDEX = "step_index";
    private static final String BUNDLE_RECIPE = "recipe";

    private Recipe recipe;
    private int stepIndex;

    private App app;


    public StepDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.app = (App)context.getApplicationContext();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.app = null;
    }

    /**
     * Method is used after creating instance of the fragment in Activity class.
     * @param recipe Recipe instance
     * @param stepIndex index of recipe's step that this fragment has to display
     */
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
        ButterKnife.bind(this, rootView);
        refreshViews();

        return rootView;
    }

    /**
     * Method of refreshing views inside fragment. It's mainly used while switching between
     * recipe's steps to use one fragment for all of the steps instead of calling
     * FragmentManager.replace() each time the user chooses another step.
     * It's also used in the first initialization during creation of the fragment.
     */
    public void refreshViews(){
        Step step = recipe.getSteps().get(stepIndex);
        if(stepDescription!=null) {
            stepDescription.setText(step.getDescription());
        }
        setPlayer(app.getExoPlayer(recipe, stepIndex));
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(BUNDLE_RECIPE, recipe);
        outState.putInt(BUNDLE_STEP_INDEX, stepIndex);
    }



    @Override
    public PlayerView getPlayerView() {
        return mPlayerView;
    }

    @Override
    public void preparePlayer() {
        app.preparePlayer();
    }

    @Override
    public void changeStep(int index) {
        stepIndex = index;
        refreshViews();
        if(stepDescription==null) {
            Toast.makeText(app, recipe.getSteps().get(stepIndex).getShortDescription(), Toast.LENGTH_SHORT).show();
        }
    }


}
