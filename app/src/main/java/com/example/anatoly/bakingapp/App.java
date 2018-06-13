package com.example.anatoly.bakingapp;

import android.app.Application;
import android.net.Uri;
import android.util.Log;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class App extends Application {
    private static App app;
    private SimpleExoPlayer mExoPlayer;


    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }

    public static App getApp() {
        return app;
    }

    public SimpleExoPlayer getExoPlayer(Uri mediaUri) {
        if (mExoPlayer==null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector);

            String userAgent = Util.getUserAgent(this, this.getApplicationInfo().name);
            DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(this, userAgent);

            MediaSource mediaSource =
                    new ExtractorMediaSource.Factory(dataSourceFactory)
                            .createMediaSource(mediaUri);

            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }

        return mExoPlayer;
    }

    public void releasePlayer(){
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }
}
