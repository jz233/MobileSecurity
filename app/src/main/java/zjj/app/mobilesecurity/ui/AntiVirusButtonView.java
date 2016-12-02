package zjj.app.mobilesecurity.ui;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import zjj.app.mobilesecurity.R;


public class AntiVirusButtonView extends FrameLayout {

    public static final String STATUS_GOOD = "good";
    public static final String STATUS_OK = "ok";
    public static final String STATUS_FAIR = "fair";

    private ImageView iv_circle_colored;
    private String status;
    private GradientDrawable coloredCircle;
    private FrameLayout fl_btn_scan_virus;
    private ImageView iv_shield;
    private ImageView iv_shield_shadow;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        updateUIStatus();
    }

    public AntiVirusButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public AntiVirusButtonView(Context context) {
        super(context);
        initView(context);
    }

    public AntiVirusButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        status = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "status");
        initView(context);
        updateUIStatus();
    }

    public void setListener(OnClickListener listener) {
        fl_btn_scan_virus.setOnClickListener(listener);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.ui_antivirus_topbtn, this);
        iv_circle_colored = (ImageView) view.findViewById(R.id.iv_circle_colored);
        iv_shield = (ImageView)view.findViewById(R.id.iv_shield);
        iv_shield_shadow = (ImageView)view.findViewById(R.id.iv_shield_shadow);
        fl_btn_scan_virus = (FrameLayout)view.findViewById(R.id.fl_btn_scan_virus);

        coloredCircle = (GradientDrawable) iv_circle_colored.getBackground();
    }

    public void buttonClickAnimation() {
//        AnimationSet set = new AnimationSet(false);
//        Animation anim1 = AnimationUtils.loadAnimation(getContext(), R.anim.btn_clicked_1);
//        Animation anim2 = AnimationUtils.loadAnimation(getContext(), R.anim.btn_clicked_2);
//        set.addAnimation(anim1);
//        set.addAnimation(anim2);
        AnimationSet set = (AnimationSet) AnimationUtils.loadAnimation(getContext(), R.anim.btn_clicked_set);
        fl_btn_scan_virus.startAnimation(set);
        iv_shield.startAnimation(set);
        iv_shield_shadow.startAnimation(set);

    }

    public void updateUIStatus() {
        switch (status) {
            case STATUS_GOOD:
                /*int colorFrom = ContextCompat.getColor(getContext(), R.color.colorPrimaryFair);
                int colorTo = ContextCompat.getColor(getContext(), R.color.colorPrimaryGood);

                UIUtils.colorChangeAnimation(colorFrom, colorTo, 1000, new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        coloredCircle.setColor((Integer) animation.getAnimatedValue());
                    }
                });*/

                coloredCircle.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryGood));
                iv_shield.setImageResource(R.drawable.security_good);
                break;
            case STATUS_OK:
                coloredCircle.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryOk));
                iv_shield.setImageResource(R.drawable.security_ok);
                break;
            case STATUS_FAIR:
                coloredCircle.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryFair));
                iv_shield.setImageResource(R.drawable.security_fair);
                break;
            default:
                coloredCircle.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryGood));
                iv_shield.setImageResource(R.drawable.security_good);
                break;
        }
    }


}
