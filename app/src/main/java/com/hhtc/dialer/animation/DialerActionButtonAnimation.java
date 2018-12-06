package com.hhtc.dialer.animation;

import android.view.View;

public class DialerActionButtonAnimation {
    private static final int FAB_SCALE_IN_DURATION = 266;
    private static final int FAB_SCALE_IN_FADE_IN_DELAY = 100;
    private static final int FAB_ICON_FADE_OUT_DURATION = 66;


    public static void scaleIn(View view, int delayMs) {
        AnimUtils.scaleIn(view, FAB_SCALE_IN_DURATION, delayMs);
        AnimUtils.fadeIn(view, FAB_SCALE_IN_DURATION,
                delayMs + FAB_SCALE_IN_FADE_IN_DELAY, null);
    }

    public static void scaleOut(View view) {
        AnimUtils.scaleOut(view, 250);
        // Fade out the icon faster than the scale out animation, so that the icon scaling is less
        // obvious. We don't want it to scale, but the resizing the container is not as performant.
        AnimUtils.fadeOut(view, FAB_ICON_FADE_OUT_DURATION, null);
    }
}
