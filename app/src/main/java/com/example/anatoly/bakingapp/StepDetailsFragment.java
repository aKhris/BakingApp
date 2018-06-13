package com.example.anatoly.bakingapp;


import android.content.Context;
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
import android.widget.Toast;

import com.example.anatoly.bakingapp.Model.Step;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;

import static android.view.View.VISIBLE;


/**
 * A simple {@link Fragment} subclass.
 */
public class StepDetailsFragment extends Fragment
    implements View.OnClickListener
{

    private static final String LOG_TAG = StepDetailsFragment.class.getSimpleName();
    private static final String BUNDLE_STEP = "step";

    private Step step;
    private SetStepListener listener;

    public StepDetailsFragment() {
        // Required empty public constructor
    }

    public void setStep(Step step) {
        this.step = step;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null && savedInstanceState.containsKey(BUNDLE_STEP)){
            this.step = (Step) savedInstanceState.getSerializable(BUNDLE_STEP);
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
        TextView stepDescription = rootView.findViewById(R.id.tv_step_description);
        if(stepDescription!=null) {
            stepDescription.setText(step.getmDescription());
        }
        PlayerView mPlayerView = rootView.findViewById(R.id.pv_video_player);
        SimpleExoPlayer player = App.getApp().getExoPlayer(Uri.parse(step.getmVideoUrl()));
        mPlayerView.setPlayer(player);

        PlayerControlView controlView = mPlayerView.findViewById(R.id.exo_controller);
        ImageButton prevButton = controlView.findViewById(R.id.exo_prev_custom);
        ImageButton nextButton = controlView.findViewById(R.id.exo_next_custom);
        if(prevButton!=null) {
            prevButton.setOnClickListener(this);
        }
        if(nextButton!=null){
            nextButton.setOnClickListener(this);
        }
        return rootView;
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(BUNDLE_STEP, step);
    }


    @Override
    public void onClick(View v) {
        if(listener==null){return;}
        switch (v.getId()){
            case R.id.exo_prev_custom:
                listener.setStep(false);
                break;
            case R.id.exo_next_custom:
                listener.setStep(true);
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof SetStepListener){
            this.listener = (SetStepListener) context;
        } else {
            throw new UnsupportedOperationException("Activity must implement SetStepListener");
        }
    }

    public interface SetStepListener{
        void setStep(boolean isNext);
    }
}
