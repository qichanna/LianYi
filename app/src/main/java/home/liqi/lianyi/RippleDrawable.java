package home.liqi.lianyi;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by liqi on 2017/4/16.
 */

public class RippleDrawable extends Drawable {

    private int mAlpha = 200;
    private int mRippleColor = 0;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float mRipplePointX,mRipplePointY;
    private float mRippleRadius =200;
    private Bitmap bitmap;

    public RippleDrawable(Bitmap bitmap) {
        this.bitmap = bitmap;
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        setmRippleColor(Color.RED);

        //滤镜方法
//        setColorFilter(new LightingColorFilter(0xFFFF0000,0x00330000));
    }

    public void onTouch(MotionEvent event){
        mRippleRadius = mRippleRadius + 10;
        invalidateSelf();

        switch (event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                    onTouchDown(event.getX(),event.getY());
                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:

                break;
            case MotionEvent.ACTION_CANCEL:

                break;
        }
    }

    private void onTouchDown(float x,float y){
        Log.e("eee", "onTouchDown: ");
        mRipplePointX = x;
        mRipplePointY = y;
        mRippleRadius = mRippleRadius + 20;
        invalidateSelf();

    }
    private void onTouchMove(float x,float y){

    }
    private void onTouchUp(float x,float y){

    }
    private void onTouchCancel(float x,float y){

    }



    public void setmRippleColor(int color){
        mRippleColor = color;
        onColorOrAlohaChange();

    }

    private void onColorOrAlohaChange(){
        mPaint.setColor(mRippleColor);

        if(mAlpha != 255) {
            int pAlpha = mPaint.getAlpha();
//        pAlpha = Color.alpha(mRippleColor);

            int realAlpha = (int) (pAlpha * (mAlpha / 255f));
            mPaint.setAlpha(realAlpha);
        }
        invalidateSelf();

    }

    @Override
    public void draw(Canvas canvas) {
//        canvas.drawColor(0x70FF0000);

        canvas.drawBitmap(bitmap,0,0,mPaint);
        canvas.drawCircle(mRipplePointX,mRipplePointY,mRippleRadius,mPaint);

    }

    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {
        mAlpha = alpha;
        onColorOrAlohaChange();
    }

    @Override
    public int getAlpha() {
        return mAlpha;
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        if(mPaint.getColorFilter() != colorFilter){
            mPaint.setColorFilter(colorFilter);
        }
        invalidateSelf();

    }

    @Override
    public int getOpacity() {
        int alpha = mPaint.getAlpha();
        if(alpha == 255){
            //不透明
            return PixelFormat.OPAQUE;
        }else if(alpha == 0){
            //全透明
            return PixelFormat.TRANSPARENT;
        }else {
            //半透明
            return PixelFormat.TRANSLUCENT;
        }
    }
}
