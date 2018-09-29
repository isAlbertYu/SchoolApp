package com.example.albert.myWidget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ImageView;

@SuppressWarnings(value = "all") //压制ImageView发出的讨厌的警告
public class BoxImageView extends ImageView {
    private Paint paint;

    public BoxImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();//初始化画笔
    }

    private void initPaint() {
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeJoin(Paint.Join.ROUND);//圆形画笔
        paint.setStrokeCap(Paint.Cap.ROUND);//圆角
        paint.setStrokeWidth(3);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(new Rect(100, 200, 400, 500), paint);//绘制矩形
    }


}
