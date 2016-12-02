package zjj.app.mobilesecurity.ui;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import zjj.app.mobilesecurity.R;


public class SpeedUpButtonView extends TopButtonView {
    private ImageView iv_circle_colored;
    private FrameLayout fl_btn_speedup;
    private ImageView iv_speedup_shadow;
    private ImageView iv_speedup;
    private GradientDrawable coloredCircle;


    public SpeedUpButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SpeedUpButtonView(Context context) {
        super(context);
    }

    public SpeedUpButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.ui_speedup_topbtn, this);

        iv_circle_colored = (ImageView) view.findViewById(R.id.iv_circle_colored);
        fl_btn_speedup = (FrameLayout) view.findViewById(R.id.fl_btn_speedup);
        iv_speedup_shadow = (ImageView) view.findViewById(R.id.iv_speedup_shadow);
        iv_speedup = (ImageView) view.findViewById(R.id.iv_speedup);

        coloredCircle = (GradientDrawable) iv_circle_colored.getBackground();

    }

    @Override
    public void setListener(OnClickListener listener) {
        fl_btn_speedup.setOnClickListener(listener);
    }

    @Override
    public void updateUIStatus(String status) {
        switch (status) {
            case STATUS_GOOD:
                coloredCircle.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryGood));
                iv_speedup.setImageResource(R.drawable.speedup_good);
                break;

            default:
                coloredCircle.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryGood));
                iv_speedup.setImageResource(R.drawable.speedup_good);
                break;
        }
    }

}
