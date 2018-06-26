package com.akhris.bakingapp;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.akhris.bakingapp.Model.Recipe;
import com.akhris.bakingapp.Model.Step;
import com.akhris.bakingapp.R;
import com.google.android.exoplayer2.SimpleExoPlayer;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Fragment containing video player fragment and step description textview.
 */
public class StepDetailsFragment
    extends Fragment
    implements VideoPlayerFragment.VideoPlayerCallbacks
{
    @Nullable @BindView(R.id.tv_step_description) TextView stepDescription;

    private static final String BUNDLE_STEP_INDEX = "step_index";
    private static final String BUNDLE_RECIPE = "recipe";

    private static final String VIDEO_FRAGMENT_TAG = "video_fragment_tag";

    /**
     * listener is null on a tablet
     */
    @Nullable
    private StepChangeListener listener;

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
        if(context instanceof StepChangeListener){
            this.listener = (StepChangeListener) context;
        }
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_step_details, container, false);
        ButterKnife.bind(this, rootView);

        FragmentManager fragmentManager = getChildFragmentManager();

        VideoPlayerFragment videoPlayerFragment = (VideoPlayerFragment) fragmentManager.findFragmentByTag(VIDEO_FRAGMENT_TAG);
                if(videoPlayerFragment==null){
                    videoPlayerFragment = new VideoPlayerFragment();
                    fragmentManager
                            .beginTransaction()
                            .replace(R.id.video_player_fragment_container, videoPlayerFragment, VIDEO_FRAGMENT_TAG)
                            .commit();
                }

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
    }
    
    private void showToast(){
        if(stepDescription==null) {
            Toast.makeText(app, getCurrentStep().getShortDescription(), Toast.LENGTH_SHORT).show();
        }
    }

    private Step getCurrentStep(){
        return recipe.getSteps().get(stepIndex);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(BUNDLE_RECIPE, recipe);
        outState.putInt(BUNDLE_STEP_INDEX, stepIndex);
    }

    public void actualizeMediaSource(){
        app.changeMediaSource(recipe, stepIndex);
    }

    @Override
    public void onNextClick() {
        changeStepIndex(true);
        app.changeMediaSource(recipe,stepIndex);
        refreshViews();
        showToast();
        tellListener();
    }

    @Override
    public void onPrevClick() {
        changeStepIndex(false);
        app.changeMediaSource(recipe,stepIndex);
        refreshViews();
        showToast();
        tellListener();
    }


    private void tellListener(){
        if(listener!=null){
            listener.onStepChanged(stepIndex);
        }
    }

    @Override
    public SimpleExoPlayer getPlayer() {

        return app.getExoPlayer(recipe, stepIndex);
    }


    private void changeStepIndex(boolean isNext){
        int stepCount = recipe.getSteps().size();
        stepIndex = isNext? stepIndex+1 : stepIndex-1;
        if(stepIndex>stepCount-1){stepIndex=0;}
        else if (stepIndex<0){stepIndex = stepCount-1;}
    }

    /**
     * Interface is used to tell parent activity (if it's not a tablet) that user changed the step
     * by tapping on playback control buttons (next/previous). Therefore we can change appbar title
     * there.
     */
    interface StepChangeListener{
        void onStepChanged(int stepIndex);
    }

    @VisibleForTesting
    public Recipe getRecipe(){
        return recipe;
    }

    @VisibleForTesting
    public int getStepIndex(){
        return stepIndex;
    }

}
