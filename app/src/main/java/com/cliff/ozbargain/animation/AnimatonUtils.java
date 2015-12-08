package com.cliff.ozbargain.animation;

import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cliff.ozbargain.tools.DealListAdapter;

/**
 * Created by Clifford on 7/12/2015.
 */
public class AnimatonUtils {
    public static void animate(View view) {
        ObjectAnimator animateTranslateY = ObjectAnimator.ofFloat(view, "translationY",70,0);
        animateTranslateY.setDuration(500);
        animateTranslateY.start();
        view.setAlpha(0.0f);
        view.animate()
                .setDuration(300)
                .alpha(1.0f);
    }
}
