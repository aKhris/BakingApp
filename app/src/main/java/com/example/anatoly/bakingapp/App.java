package com.example.anatoly.bakingapp;

import android.app.Application;
import android.net.Uri;
import android.util.Log;

import com.example.anatoly.bakingapp.Model.Recipe;
import com.google.android.exoplayer2.ExoPlayerFactory;
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
    private int lastPlayedIndex = -1;
    private ConcatenatingMediaSource mediaSource;

    public SimpleExoPlayer getExoPlayer(Recipe recipe, int stepIndex) {
        if (mExoPlayer==null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector);


//        if(lastPlayedVideo!=null && mediaUri.equals(lastPlayedVideo)){
//            return mExoPlayer;
//        }  else {
//            String userAgent = Util.getUserAgent(this, this.getApplicationInfo().name);
//            DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(this, userAgent);

//            MediaSource mediaSource =
//                    new ExtractorMediaSource.Factory(dataSourceFactory)
//                            .createMediaSource(mediaUri);


            mediaSource = new ConcatenatingMediaSource();
            mediaSource.addMediaSources(recipe.getMediaSources(this));
//            mExoPlayer.stop();
            mExoPlayer.prepare(mediaSource, false, false);
            if(stepIndex!=lastPlayedIndex) {
                mExoPlayer.seekTo(stepIndex, 0L);
            }
//            lastPlayedVideo = mediaUri;
        }
            lastPlayedIndex = stepIndex;
            return mExoPlayer;
//        }
    }

    // TODO: 19.06.18 Сделать возможность сброса mediaSource
    //И сбрасывать его при смене рецепта и при последнем сбросе player
    public void preparePlayer(){
        int index = mExoPlayer.getCurrentWindowIndex();
        mExoPlayer.prepare(mediaSource);
        mExoPlayer.seekTo(index, 0L);
    }

    public void releasePlayer(){
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }


}
