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
