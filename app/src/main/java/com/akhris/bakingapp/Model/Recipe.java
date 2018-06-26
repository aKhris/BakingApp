package com.akhris.bakingapp.Model;

import android.content.Context;
import android.net.Uri;

import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Recipe implements Serializable{
    public static final int INVALID_ID=-1;

    private int mId;
    private String mName;
    private List<Ingredient> mIngredients;
    private List<Step> mSteps;
    private int mServings;
    private String mImageUrl;

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public List<Ingredient> getIngredients() {
        return mIngredients;
    }

    public void setIngredients(List<Ingredient> mIngredients) {
        this.mIngredients = mIngredients;
    }

    public List<Step> getSteps() {
        return mSteps;
    }

    public void setSteps(List<Step> mSteps) {
        this.mSteps = mSteps;
    }

    public int getServings() {
        return mServings;
    }

    public void setServings(int mServings) {
        this.mServings = mServings;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public List<MediaSource> getMediaSources(Context context){
        ArrayList<MediaSource> mediaSources = new ArrayList<>();
        for (Step s:mSteps) {
            String userAgent = Util.getUserAgent(context, context.getApplicationInfo().name);
            DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(context, userAgent);
            MediaSource mediaSource =
                    new ExtractorMediaSource.Factory(dataSourceFactory)
                            .createMediaSource(Uri.parse(s.getVideoUrl()));
            mediaSources.add(mediaSource);
        }
        return mediaSources;
    }


    /**
     * Need to override this method to use it in MainActivityTest
     * while checking if the intent extra contains the correct Recipe object or not.
     */
    @Override
    public boolean equals(Object obj) {
        return (
                (obj instanceof Recipe) &&
                        (((Recipe) obj).getId() == this.mId)
        );
    }
}
