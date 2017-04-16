package home.liqi.lianyi;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by liqi on 2017/4/16.
 */

public class RippleButton extends Button {

    private RippleDrawable mRippleDrawable;

    public RippleButton(Context context) {
        this(context,null);
    }

    public RippleButton(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RippleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRippleDrawable = new RippleDrawable(BitmapFactory.decodeResource(getResources(),R.mipmap.timg));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mRippleDrawable.draw(canvas);
        super.onDraw(canvas);
    }
}
