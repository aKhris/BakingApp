package com.example.anatoly.bakingapp;

import android.app.Application;
import android.net.Uri;
import android.util.Log;

import com.example.anatoly.bakingapp.Model.Recipe;
import com.example.anatoly.bakingapp.Utils.PlayerUtils;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.CompositeMediaSource;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.DynamicConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class App extends Application {
    private SimpleExoPlayer mExoPlayer;
    private int lastRecipeId = Recipe.INVALID_ID;
    private int lastPlayedIndex = -1;
    private MediaSource mediaSource;

    public SimpleExoPlayer getExoPlayer(Recipe recipe, int stepIndex) {
        if (mExoPlayer==null) {
           mExoPlayer = PlayerUtils.makeExoPlayer(this);
        }

            if(mediaSource==null || recipe.getId()!=lastRecipeId || lastPlayedIndex!=stepIndex) {
                changeMediaSource(recipe, stepIndex);
            }
            return mExoPlayer;
    }

    public void changeMediaSource(Recipe recipe, int stepIndex){
        mediaSource = PlayerUtils.makeMediaSource(this, recipe.getSteps().get(stepIndex).getVideoUrl());
        lastPlayedIndex = stepIndex;
        lastRecipeId = recipe.getId();
        mExoPlayer.prepare(mediaSource, true, false);
    }


    public void releasePlayer(){
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }


}
