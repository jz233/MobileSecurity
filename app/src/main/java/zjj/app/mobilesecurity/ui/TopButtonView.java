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


public abstract class TopButtonView extends FrameLayout {

    public static final String STATUS_GOOD = "good";
    public static final String STATUS_OK = "ok";
    public static final String STATUS_FAIR = "fair";

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        updateUIStatus(status);
    }

    public TopButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public TopButtonView(Context context) {
        super(context);
        initView(context);
    }

    public TopButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        status = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "status");
        initView(context);
    }

    public abstract void initView(Context context);

    public abstract void setListener(OnClickListener listener) ;

    public abstract void updateUIStatus(String status);


}
