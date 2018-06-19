package com.example.anatoly.bakingapp.Base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.example.anatoly.bakingapp.R;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;


/**
 * This class is made to hide the implementation of {@link Player.EventListener}
 * from the Fragment that contains PlayerView instance and to make this fragment automatically
 * pause video (if it's playing) in onPause and resume (if it was playing) in onResume.
 */
public abstract class VideoFragment extends Fragment implements Player.EventListener {
    private static final String LOG_TAG = VideoFragment.class.getSimpleName();
    private boolean isPlaying=false;



    private static final String BUNDLE_IS_PLAYING = "is_playing";
    private int lastWindowIndex;

    public abstract PlayerView getPlayerView();
    public abstract void preparePlayer();
    public abstract void changeStep(int index);

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(BUNDLE_IS_PLAYING, isPlaying);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null && savedInstanceState.containsKey(BUNDLE_IS_PLAYING)){
            this.isPlaying = savedInstanceState.getBoolean(BUNDLE_IS_PLAYING);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setState(isPlaying);
        getPlayerView().getPlayer().addListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPlayerView().getPlayer().removeListener(this);
        setState(false);
    }

    private void setState(boolean state){
        getPlayerView().getPlayer().setPlayWhenReady(state);
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    /**
     * Method that is called when the ExoPlayer state changes.
     * @param playWhenReady true if ExoPlayer is playing, false if it's paused.
     * @param playbackState int describing the state of ExoPlayer. Can be STATE_READY, STATE_IDLE,
     *                      STATE_BUFFERING, or STATE_ENDED.
     */
    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if((playbackState == Player.STATE_READY) && playWhenReady){
//            Log.v(LOG_TAG,"PlayerStateChanged: playing");
            isPlaying = true;
        } else if((playbackState == Player.STATE_READY)){
//            Log.v(LOG_TAG,"PlayerStateChanged: paused");
            isPlaying = false;
        } else if (playbackState == Player.STATE_ENDED){
//            Log.v(LOG_TAG,"PlayerStateChanged: ended");
            isPlaying = false;
        }
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
//        getPlayerView().getPlayer().removeListener(this);
//        releasePlayer();
//        getPlayerView().setVisibility(View.GONE);
        setNoVideo(true);
    }

    @Override
    public void onPositionDiscontinuity(int reason) {
        Player player = getPlayerView().getPlayer();
        if(player.getCurrentWindowIndex()!=lastWindowIndex){
            changeStep(player.getCurrentWindowIndex());
            if(player.getPlaybackState()==Player.STATE_IDLE) {
                setNoVideo(false);
                preparePlayer();
            }

        }
        lastWindowIndex = player.getCurrentWindowIndex();
    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }

    private void setNoVideo(boolean noVideo){
        if(noVideo){
            getPlayerView().findViewById(R.id.iv_no_video).setVisibility(View.VISIBLE);
        } else {
            getPlayerView().findViewById(R.id.iv_no_video).setVisibility(View.INVISIBLE);
        }
    }

    public void setPlayer(Player player){
            setNoVideo(false);
            getPlayerView().setVisibility(View.VISIBLE);
            getPlayerView().setPlayer(player);
    }
}
