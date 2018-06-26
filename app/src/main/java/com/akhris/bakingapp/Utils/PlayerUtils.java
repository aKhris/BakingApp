package com.akhris.bakingapp.Utils;

import android.content.Context;
import android.net.Uri;

import com.akhris.bakingapp.Model.Recipe;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class PlayerUtils {

    public static ConcatenatingMediaSource makeMediaSource(Context context, Recipe recipe){

        ConcatenatingMediaSource mediaSource = new ConcatenatingMediaSource();
        mediaSource.addMediaSources(recipe.getMediaSources(context));
        return mediaSource;
    }

    public static SimpleExoPlayer makeExoPlayer(Context context){
        TrackSelector trackSelector = new DefaultTrackSelector();
        SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
//        player.setRepeatMode(Player.REPEAT_MODE_ALL);
        player.setPlayWhenReady(false);
        return player;
    }

    public static MediaSource makeMediaSource(Context context, String videoUrl) {
        String userAgent = Util.getUserAgent(context, context.getApplicationInfo().name);
        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(context, userAgent);

        return new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(videoUrl));
    }
}
