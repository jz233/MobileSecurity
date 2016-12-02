package zjj.app.mobilesecurity.ui;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * 可获取滑动方向的ViewPager
 */
public class DirectionalViewPager extends ViewPager {
    private int startX, endX;
    public static final int STATIC = 0;
    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    private int direction;
    private int lastMovedX = 0;
    public boolean directionChanged = false;
    /**
     * 阈值，不设置或过小的话，临界区域的颜色变化会有小问题，原因未知
     */
    private final static int THRESHOLD = 50;

    /**
     * pager 移动的方向， 非手指水平滑动方向(相反方向) <br/>
     * 0 -- static <br/>
     * 1 -- left <br/>
     * 2 -- right <br/>
     */
    public int getDirection() {
        return direction;
    }

    public DirectionalViewPager(Context context) {
        super(context);
    }

    public DirectionalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();

        if (action == MotionEvent.ACTION_DOWN) {
            startX = (int) ev.getRawX();
        }
        if (action == MotionEvent.ACTION_MOVE) {
            endX = (int) ev.getRawX();
            int movedX = endX - startX;
            //阈值为50
            if (Math.abs(movedX) < THRESHOLD) {
                direction = STATIC;
            }else{
                //如果乘积为负, 则滑动方向相对起始位置方向发生转变, 重新设置colorTo
                if (lastMovedX * movedX <= 0) {
                    directionChanged = true;
                }
                //上次移动的值(正负与方向有关)
                lastMovedX = movedX;

                //移动时暂时忽略STATIC
                direction = movedX > 0 ? LEFT : RIGHT;
            }
        }
        if (action == MotionEvent.ACTION_UP) {
//            direction = STATIC;
            directionChanged = false;
        }
        return super.onTouchEvent(ev);
    }
}
