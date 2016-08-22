package zjj.app.mobilesecurity.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import zjj.app.mobilesecurity.R;

/**
 * 自定义分割线
 */
public class DividerDecoration extends RecyclerView.ItemDecoration{

    private int height = 1;
    private Paint paint;

    public DividerDecoration(Context context, int height) {
        this.height = (int) TypedValue.applyDimension(height, TypedValue.COMPLEX_UNIT_DIP,context.getResources().getDisplayMetrics());
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.parseColor("#22000000"));
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        outRect.bottom = height;
//        outRect.set(0, 0, 0, height);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int count = parent.getChildCount();
        for(int i=0; i<count; i++){
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + height;

            c.drawRect(left, top, right, bottom, paint);
        }
    }
}