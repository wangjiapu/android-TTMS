package com.example.other.ttms.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.other.ttms.R;
import com.example.other.ttms.framgments.LoginFragment;

import java.lang.ref.WeakReference;
import java.util.logging.Handler;

public class MyView extends View {

    private Paint mPaint;
    private Path mPath;



    private int width;//控件宽

    private int height;//控件的高度


    public MyView(Context context) {
        this(context,null);
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint=new Paint();
        mPaint.setColor(context.getResources().getColor(R.color.popcolor));
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);

        mPath=new Path();

    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width=this.getWidth();
        height=this.getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);



        int temp=width/7;

        mPath.moveTo(temp*4,height);
        mPath.lineTo(temp*5,0);
        mPath.lineTo(temp*6,height);
        mPath.close();

        canvas.drawPath(mPath,mPaint);
    }
}
