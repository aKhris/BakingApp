package com.akhris.bakingapp;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.akhris.bakingapp.Utils.PlayerUtils;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource;
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

    private static final String BUNDLE_NO_VIDEO = "no_video";
    private static final String BUNDLE_PLAYER_POSITION = "player_position";
    private static final String BUNDLE_PLAY_WHEN_READY = "play_when_ready";

    VideoPlayerCallbacks listener;
    private boolean mNoVideo;

    private SimpleExoPlayer mPlayer;
    private MediaSource mMediaSource;

    private long mPlayerPosition;
    private boolean mPlayWhenReady;

    @BindView(R.id.pv_video_player) PlayerView playerView;
    @BindView(R.id.iv_no_video) ImageView noVideoImageView;
    @BindView(R.id.exo_buffering) ProgressBar bufferingBar;
    @BindView(R.id.exo_shutter) View shutter;


    @Optional
    @OnClick(R.id.exo_next_custom)
    public void onNextClick(){
        listener.onNextClick();
        actualizeMediaSource();
    }
    @Optional
    @OnClick(R.id.exo_prev_custom)
    public void onPrevClick(){
        listener.onPrevClick();
        actualizeMediaSource();
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
        outState.putBoolean(BUNDLE_NO_VIDEO, mNoVideo);
        outState.putLong(BUNDLE_PLAYER_POSITION, mPlayerPosition);
        outState.putBoolean(BUNDLE_PLAY_WHEN_READY, mPlayWhenReady);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null) {
            mNoVideo = savedInstanceState.getBoolean(BUNDLE_NO_VIDEO);
            mPlayerPosition = savedInstanceState.getLong(BUNDLE_PLAYER_POSITION, 0L);
            mPlayWhenReady = savedInstanceState.getBoolean(BUNDLE_PLAY_WHEN_READY, false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initPlayer();
        mPlayer.addListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mPlayer.removeListener(this);
        releasePlayer();
    }

    private void releasePlayer(){
        if (mPlayer != null) {
            mPlayerPosition = mPlayer.getContentPosition();
            mPlayWhenReady = mPlayer.getPlayWhenReady();
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
            mMediaSource = null;
        }
    }

    private void actualizeMediaSource(){
        mMediaSource = listener.getMediaSource();
        mPlayer.prepare(mMediaSource, true, false);
    }

    private void initPlayer() {
        if (mPlayer==null) {
            mPlayer = PlayerUtils.makeExoPlayer(getContext());
        }

        if(mMediaSource ==null) {
            mMediaSource = listener.getMediaSource();
            mPlayer.prepare(mMediaSource, true, false);
            mPlayer.seekTo(mPlayerPosition);
            mPlayer.setPlayWhenReady(mPlayWhenReady);
        }

        playerView.setPlayer(mPlayer);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_video_player, container, false);
        ButterKnife.bind(this, rootView);
//        playerView.setPlayer(listener.getPlayer());
        setNoVideo(mNoVideo);
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
        if (playbackState == Player.STATE_BUFFERING){
            bufferingBar.setVisibility(View.VISIBLE);
        } else {
            bufferingBar.setVisibility(View.INVISIBLE);
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
            shutter.setBackgroundColor(Color.BLACK);
            shutter.setVisibility(View.VISIBLE);
        } else {
            noVideoImageView.setVisibility(View.INVISIBLE);
        }
        this.mNoVideo = noVideo;
    }



    interface VideoPlayerCallbacks {
        void onNextClick();
        void onPrevClick();
        MediaSource getMediaSource();
    }


}
