package com.example.anatoly.bakingapp;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;


/**
 * A simple {@link Fragment} subclass.
 */
public class VideoPlayerFragment extends Fragment
    implements Player.EventListener
{

    private static final String BUNDLE_IS_PLAYING = "is_playing";
    private static final String BUNDLE_NO_VIDEO = "no_video";


    VideoPlayerCallbacks listener;
    private boolean noVideo;
    private boolean isPlaying;


    @BindView(R.id.pv_video_player) PlayerView playerView;
    @BindView(R.id.iv_no_video) ImageView noVideoImageView;


    @Optional
    @OnClick(R.id.exo_next_custom)
    public void onNextClick(){
        listener.onNextClick();
    }
    @Optional
    @OnClick(R.id.exo_prev_custom)
    public void onPrevClick(){
        listener.onPrevClick();
    }

    public VideoPlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(getParentFragment()!=null && getParentFragment() instanceof VideoPlayerCallbacks){
            this.listener = (VideoPlayerCallbacks)getParentFragment();
        } else {
            throw new UnsupportedOperationException("Parent of VideoPlayerFragment must implement VideoPlayerCallbacks");
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(BUNDLE_NO_VIDEO, noVideo);
        outState.putBoolean(BUNDLE_IS_PLAYING, isPlaying);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null
                && savedInstanceState.containsKey(BUNDLE_IS_PLAYING)
                && savedInstanceState.containsKey(BUNDLE_NO_VIDEO)) {
            noVideo = savedInstanceState.getBoolean(BUNDLE_NO_VIDEO);
            isPlaying = savedInstanceState.getBoolean(BUNDLE_IS_PLAYING);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        listener.getPlayer().addListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        listener.getPlayer().removeListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_video_player, container, false);
        ButterKnife.bind(this, rootView);
        playerView.setPlayer(listener.getPlayer());
        setState(isPlaying);
        setNoVideo(noVideo);
        return rootView;
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        setNoVideo(false);
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        setNoVideo(true);
    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }


    public void setNoVideo(boolean noVideo){
        if(noVideo){
            noVideoImageView.setVisibility(View.VISIBLE);
        } else {
            noVideoImageView.setVisibility(View.INVISIBLE);
        }
        this.noVideo = noVideo;
    }

    private void setState(boolean state){
        playerView.getPlayer().setPlayWhenReady(state);
    }


//    public void setMediaSource(MediaSource mediaSource){
//        this.player.prepare(mediaSource, true, false);
//    }


    interface VideoPlayerCallbacks {
        void onNextClick();
        void onPrevClick();
        SimpleExoPlayer getPlayer();
    }

}
