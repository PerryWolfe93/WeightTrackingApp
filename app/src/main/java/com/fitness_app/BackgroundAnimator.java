package com.fitness_app;


import android.graphics.drawable.AnimationDrawable;

import androidx.constraintlayout.widget.ConstraintLayout;

public class BackgroundAnimator {

    public void animateBackground(ConstraintLayout constraintLayout) {
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
    }
}
