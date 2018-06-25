package com.example.anatoly.bakingapp.Base;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

import static android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
import static android.view.View.SYSTEM_UI_FLAG_IMMERSIVE;
import static android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN;
import static android.view.WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

public class VideoFullscreenActivity extends AppCompatActivity
        implements View.OnSystemUiVisibilityChangeListener
    {

        //Hide navigation panel in fullscreen mode after 3 seconds
        //after it appears again when user interacts with screen
        private static final long HIDE_NAVIGATION_DELAY_MILLIS = 3000;



        /**
     * Check current orientation and set correct flags to make activity fullscreen
     */
    public void checkFullscreen(){

        int orientation = getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_LANDSCAPE) {

            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(FLAG_FULLSCREEN,
                    FLAG_FULLSCREEN);

            getWindow().getDecorView().setSystemUiVisibility(getUIFlags());
            getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(this);
            if(getSupportActionBar()!=null){
                getSupportActionBar().hide();
            }

        }
    }

    /**
     * Making correct flags integer variable for setSystemUiVisibility(int flags) method
     * to make Activity run fullscreen.
     * If the device runs at least KITKAT it will use a cool immersive mode.
     * Got it here: https://developer.android.com/training/system-ui/immersive
     * @return Variable with fullscreen flags
     */
    private int getUIFlags(){
        int newVis = FLAG_FULLSCREEN
                | SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            newVis |= SYSTEM_UI_FLAG_IMMERSIVE;
        } else {
            newVis |= FLAG_LAYOUT_IN_SCREEN;

        }
        return newVis;
    }

        /**
         * Method of View.OnSystemUiVisibilityChangeListener.
         * When user interacts with screen, navigation panel may appear.
         * This method will hide the panel to make activity fullscreen again.
         * @param visibility - variable with current visibility flags.
         */
        @Override
        public void onSystemUiVisibilityChange(int visibility) {
            if((visibility & SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0){
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(getWindow()!=null && getWindow().getDecorView()!=null) {
                            getWindow().getDecorView().setSystemUiVisibility(getUIFlags());
                        }
                    }
                }, HIDE_NAVIGATION_DELAY_MILLIS);

            }
        }
}
