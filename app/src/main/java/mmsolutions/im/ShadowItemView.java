package mmsolutions.im;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class ShadowItemView extends LinearLayout{

    private final Paint mPaint = new Paint();
    private final Rect mBounds = new Rect(); // границы текста
    private LinearLayout layout;

    public ShadowItemView(Context context) {
        super(context);
        initPaint();
    }

    public ShadowItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public ShadowItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initPaint();
    }

    // Параметры кисти для рисования теней
    private void initPaint(){
        mPaint.setAntiAlias(true);
//        mPaint.setTextSize(getTextSize());
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(2.0f);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setShadowLayer(10.0f, 0.0f, 0.0f, Color.BLACK);
    }

    // перерисовка с новыми размерами текста
    public void redraw(){
//        layout = getText().toString();
//        mPaint.setTextSize(getTextSize());
        mPaint.getTextBounds(String.valueOf(layout), 0, VIEW_LOG_TAG.length(), mBounds);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas){
        canvas.drawColor(Color.TRANSPARENT);
        int x = getWidth() / 2;
        int y = (getHeight() + mBounds.height()) / 2;
        canvas.drawText(String.valueOf(layout),  x, y, mPaint);
    }
}
