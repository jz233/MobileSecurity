package zjj.app.mobilesecurity.utils;


import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;

public class UIUtils {

    public static void colorChangeAnimation(int from, int to, int duration, ValueAnimator.AnimatorUpdateListener listener) {
        ValueAnimator valueAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), from, to);
        valueAnimator.setDuration(duration);
        valueAnimator.addUpdateListener(listener);
        valueAnimator.start();
    }
}
