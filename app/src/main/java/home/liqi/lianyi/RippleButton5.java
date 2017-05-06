package home.liqi.lianyi;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

/**
 * Created by liqi on 2017/4/16.
 */

public class RippleButton5 extends Button {

    private RippleDrawable5 mRippleDrawable;

    public RippleButton5(Context context) {
        this(context,null);
    }

    public RippleButton5(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RippleButton5(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRippleDrawable = new RippleDrawable5(0x60000000);
        //设置刷新接口，View中已经实现
        mRippleDrawable.setCallback(this);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //设置Drawable绘制和刷新的区域
        mRippleDrawable.setBounds(0,0,getWidth(),getHeight());
    }

    @Override
    protected boolean verifyDrawable(@NonNull Drawable who) {
        return who == mRippleDrawable || super.verifyDrawable(who);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mRippleDrawable.draw(canvas);
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mRippleDrawable.onTouch(event);
//        invalidate();
        super.onTouchEvent(event);
        return true;
    }
}
