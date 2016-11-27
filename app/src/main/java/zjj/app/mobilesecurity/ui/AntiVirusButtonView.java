package zjj.app.mobilesecurity.ui;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.graphics.drawable.shapes.Shape;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import zjj.app.mobilesecurity.R;
import zjj.app.mobilesecurity.utils.UIUtils;


public class AntiVirusButtonView extends FrameLayout {

    public static final String STATUS_GOOD = "good";
    public static final String STATUS_OK = "ok";
    public static final String STATUS_FAIR = "fair";

    private ImageView iv_security_ring;
    private ImageView iv_security_shield;
    private String status;
    private ImageView iv_security_circle;
    private GradientDrawable coloredRing;

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
        iv_security_circle.setOnClickListener(listener);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.ui_antivirus_topbtn, this);
        iv_security_ring = (ImageView) view.findViewById(R.id.iv_security_ring);
        iv_security_circle = (ImageView) view.findViewById(R.id.iv_security_circle);
        iv_security_shield = (ImageView) view.findViewById(R.id.iv_security_shield);

        coloredRing = (GradientDrawable) iv_security_ring.getBackground();
    }

    public void updateUIStatus() {
        switch (status) {
            case STATUS_GOOD:
                int colorFrom = ContextCompat.getColor(getContext(), R.color.colorPrimaryFair);
                int colorTo = ContextCompat.getColor(getContext(), R.color.colorPrimaryGood);

                UIUtils.colorChangeAnimation(colorFrom, colorTo, 1000, new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        coloredRing.setColor((Integer) animation.getAnimatedValue());
                    }
                });

                coloredRing.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryGood));
                iv_security_shield.setImageResource(R.drawable.security_good);
                break;
            case STATUS_OK:
                coloredRing.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryOk));
                iv_security_shield.setImageResource(R.drawable.security_ok);
                break;
            case STATUS_FAIR:
                coloredRing.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryFair));
                iv_security_shield.setImageResource(R.drawable.security_fair);
                break;
            default:
                coloredRing.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryGood));
                iv_security_shield.setImageResource(R.drawable.security_good);
                break;
        }
    }


}
