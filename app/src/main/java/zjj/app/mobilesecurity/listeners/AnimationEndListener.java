package zjj.app.mobilesecurity.listeners;

import android.view.animation.Animation;


public abstract class AnimationEndListener implements Animation.AnimationListener {



    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        OnAnimEnd(animation);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    public abstract void OnAnimEnd(Animation animation);


}
