package zjj.app.mobilesecurity.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;

import zjj.app.mobilesecurity.R;

public class TransitionView extends View {

    Paint paint;
    Bitmap bitmap;

    int bitmapWidth = 0;
    int bitmapHeight = 0;

    int[] arrayColor = null;
    int arrayColorLength = 0;
    long startTime = 0;
    int backVolume = 0;


    public TransitionView(Context context) {
        super(context);
        init(context);
    }

    public TransitionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context){
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.img_memory);
        bitmap = bitmap.copy(bitmap.getConfig(), true);
        setBackground(new BitmapDrawable(context.getResources(), bitmap));

        bitmapWidth = bitmap.getWidth();
        bitmapHeight = bitmap.getHeight();
        arrayColorLength = bitmapWidth * bitmapHeight;
        arrayColor = new int[arrayColorLength];

        int count=0;
        //获取每个像素的颜色值
        for(int i=0; i<bitmapWidth; i++){
            for(int j=0; j<bitmapHeight; j++){
                arrayColor[count] = bitmap.getPixel(j, i);
                count++;
            }
        }
        startTime = System.currentTimeMillis();

    }
    public int getRandom(int bottom, int top){
        return ((Math.abs(new Random().nextInt()))%(top + 1 - bottom) + bottom);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if((System.currentTimeMillis() - startTime) >= 1000){
            startTime = System.currentTimeMillis();
            setVolume(getRandom(0, 100));
        }

        invalidate();
    }

    public int getValue(int volume){
        return bitmapHeight - (bitmapHeight*volume/100);
    }

    public void setVolume(int volume){
        int startY, endY;
        boolean isAdd;
        if(backVolume > volume){
            isAdd = false;
            startY = getValue(backVolume);
            endY = getValue(volume);
        }else{
            isAdd = true;
            startY = getValue(volume);
            endY = getValue(backVolume);
        }

        int count = startY * bitmapHeight;
        for(int i=startY; i<endY; i++){
            for(int j=0; j<bitmapWidth; j++){
                if(isAdd){
                    int color = bitmap.getPixel(j, i);
                    if(color != 0){
                        bitmap.setPixel(j, i, Color.BLACK);
                    }
                }else{
                    int color = arrayColor[count];
                    bitmap.setPixel(j, i,color);
                }
                count++;
            }
        }
        backVolume = volume;
    }
}
