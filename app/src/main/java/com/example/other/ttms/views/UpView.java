package com.example.other.ttms.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.other.ttms.R;

public class UpView extends View {

    private Paint mPaint;
    private Path mPath;


    private int width;//控件宽

    private int height;//控件的高度
    public UpView(Context context) {
        this(context,null);
    }

    public UpView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public UpView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint=new Paint();
        mPaint.setColor(context.getResources().getColor(R.color.white));
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

        Log.e("height:"+height+"------","width:"+width+"----");

        int temp=width/26;

        int flag=width/2;
        mPath.moveTo(flag-temp,height);
        mPath.lineTo(flag,0);
        mPath.lineTo(flag+temp,height);
        mPath.close();

        canvas.drawPath(mPath,mPaint);
    }


}