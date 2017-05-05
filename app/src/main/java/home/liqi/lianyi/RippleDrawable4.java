package home.liqi.lianyi;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * Created by liqi on 2017/4/16.
 */

public class RippleDrawable4 extends Drawable {

    private int mAlpha = 200;
    private int mRippleColor = 0;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float mRipplePointX,mRipplePointY;
    private float mRippleRadius = 0;
    private Bitmap bitmap;

    public RippleDrawable4(Bitmap bitmap) {
        this.bitmap = bitmap;
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        setmRippleColor(Color.RED);

        //滤镜方法
//        setColorFilter(new LightingColorFilter(0xFFFF0000,0x00330000));
    }

    //
    private boolean mTouchRelease;

    public void onTouch(MotionEvent event){

        switch (event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                if(mExitDone){
                    Log.e("aaa", "wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww");
                    mRippleRadius = 0;
                    onTouchDown(event.getX(),event.getY());
                }
                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:
                if(mExitDone && mEnterDone) {
                    Log.e("aaa", "qqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
                    onTouchUp(event.getX(), event.getY());
                }
                break;
            case MotionEvent.ACTION_CANCEL:

                break;
        }
    }

    private void onTouchDown(float x,float y){
        mDonePointX = x;
        mDonePointY = y;
        //按下，没有抬起
        mTouchRelease = false;
        startEnterRunnable();

    }
    private void onTouchMove(float x,float y){

    }
    private void onTouchUp(float x,float y){
        //标识手抬起
        mTouchRelease = true;
        //当进入动画完成时，启动退出动画
        if(mEnterDone){
            startExitRunnable();
        }
//        unscheduleSelf(mEnterRunnable);
    }
    private void onTouchCancel(float x,float y){
        //标识手抬起
        mTouchRelease = true;
        //当进入动画完成时，启动退出动画
        if(mEnterDone){
            startExitRunnable();
        }
    }

    /**
     * 启动进入动画
     */
    private void startEnterRunnable(){
        Log.e("aaa", "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb: ");
        mPaintAlpha = 255;
        mEnterProgress = 0;
        mEnterDone = false;
        unscheduleSelf(mExitRunnable);
        unscheduleSelf(mEnterRunnable);
        scheduleSelf(mEnterRunnable,SystemClock.uptimeMillis());
    }

    /**
     * 启动退出动画
     */
    private void startExitRunnable(){
        mExitProgress = 0;
        mExitDone = false;
        unscheduleSelf(mEnterRunnable);
        unscheduleSelf(mExitRunnable);
        scheduleSelf(mExitRunnable,SystemClock.uptimeMillis());
    }

    private boolean mEnterDone;
    private boolean mExitDone = true;
    //进入动画进度值
    private float mEnterProgress = 0;
    //每次递增的进度值
    private float mEnterIncrement = 16f/360;
    //进入动画插值器，用于实现从快到慢的效果
    private Interpolator mEnterPolator = new DecelerateInterpolator(2);
    //动画的回调
    private Runnable mEnterRunnable = new Runnable() {
        @Override
        public void run() {
            mEnterProgress = mEnterProgress + mEnterIncrement;

            if(mEnterProgress > 1){
                onEnterProgress(1);
                onEnterDone();
                return;
            }

            float realProgress = mEnterPolator.getInterpolation(mEnterProgress);

            onEnterProgress(realProgress);
            //延迟16毫秒，保证界面刷新频率接近60FPS
            scheduleSelf(this, SystemClock.uptimeMillis() + 16);
        }
    };

    //当进入动画完成时触发
    private void onEnterDone(){
        mEnterDone = true;
        //当用户手放开时，启动退出动画
        if(mTouchRelease){
            startExitRunnable();
        }
    }

    //当退出动画完成时触发
    private void onExitDone(){
        mRippleRadius = 0;
        mExitDone = true;
    }

    /**
     * 进入动画刷新方法
     * @param progress
     */
    private void onEnterProgress(float progress){
        mRippleRadius = getProgressValue(mStartRadius,mEndRadius,progress);
        Log.e("aaa", "onEnterProgress: eeeeeeeeeeeeeeeeeeeeeee    "+mRippleRadius );
        mRipplePointX = getProgressValue(mDonePointX,mCenterPointX,progress);
        mRipplePointY = getProgressValue(mDonePointY,mCenterPointY,progress);
        int alpha = (int) getProgressValue(0,255,progress);
        mBackgroundColor = changeColorAlpha(0x30000000,alpha);
        Log.e("aaa", "onEnterProgress: 1111111111111111111111111    "+mRippleRadius );
        invalidateSelf();
    }

    //退出动画进度值
    private float mExitProgress = 0;
    //每次递增的进度值
    private float mExitIncrement = 16f/2800;
    //进入动画插值器，用于实现从慢到块的效果
    private Interpolator mExitPolator = new AccelerateInterpolator(2);
    //动画的回调
    private Runnable mExitRunnable = new Runnable() {
        @Override
        public void run() {
            mExitProgress = mExitProgress + mExitIncrement;

            if(mExitProgress > 1){
                onExitProgress(1);
                onExitDone();
                return;
            }

            float realProgress = mExitPolator.getInterpolation(mExitProgress);

            onExitProgress(realProgress);
            //延迟16毫秒，保证界面刷新频率接近60FPS
            scheduleSelf(this, SystemClock.uptimeMillis() + 16);
        }
    };

    /**
     * 退出动画刷新方法
     * @param progress
     */
    private void onExitProgress(float progress){
        int alpha = (int) getProgressValue(255,0,progress);
        mBackgroundColor = changeColorAlpha(0x30000000,alpha);
        mPaintAlpha = (int) getProgressValue(255,0,progress);
        Log.e("aaa", "onEnterProgress: 22222222222222222222    "+mRippleRadius );
        invalidateSelf();
    }

    private float getProgressValue(float start,float end,float progress){
        return start + (end - start)*progress;
    }

    //按下时的点击点
    private float mDonePointX,mDonePointY;
    //控件的中心区域
    private float mCenterPointX,mCenterPointY;

    //开始和结束的半径
    private float mStartRadius,mEndRadius;

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        mCenterPointX = bounds.centerX();
        mCenterPointY = bounds.centerY();

        //得到园的最大半径
        float maxRadius = Math.max(mCenterPointX,mCenterPointY);
        mStartRadius = maxRadius * 0f;
        mEndRadius = maxRadius * 0.8f;
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

    //背景颜色
    private int mBackgroundColor;
    //更改颜色透明度方法
    private int changeColorAlpha(int color,int alpha){
        int a = Color.alpha(color);
        a = (int) (a * (alpha/255f));
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        return Color.argb(a,r,g,b);
    }

    private int mPaintAlpha = 255;
    @Override
    public void draw(Canvas canvas) {
        //绘制背景区域颜色
        canvas.drawColor(mBackgroundColor);
//        canvas.drawBitmap(bitmap,0,0,mPaint);
        mPaint.setAlpha(mPaintAlpha);
        Log.e("aaa", "mRippleRadius: "+ mRippleRadius + " alpha "+mPaintAlpha);
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
