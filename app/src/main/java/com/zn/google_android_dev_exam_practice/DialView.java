package com.zn.google_android_dev_exam_practice;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Custom view that draws a dial with a number of selections. Color of dial changes depending on
 * what is selected
 * <p>
 * Tutorial: https://google-developer-training.github.io/android-developer-advanced-course-practicals/unit-5-advanced-graphics-and-views/lesson-10-custom-views/10-1b-p-using-custom-views/10-1b-p-using-custom-views.html
 */
public class DialView extends View {

    // required for drawing the custom view
    private static int SELECTION_COUNT = 4; // total # of selections. can be changed if desired
    private float mWidth;
    private float mHeight;
    private Paint mTextPaint;
    private Paint mDialPaint;
    private float mRadius;
    private int mActiveSelection;

    // temp storage for drawing dial labels
    private final StringBuffer mTempLabel = new StringBuffer(8);
    // temp storage for computeXY result
    private final float[] mTempResult = new float[2];

    // default fan on/off colors
    private int mFanOnColor;
    private int mFanOffColor;

    /**
     * FOR JAVA
     *
     * @param context
     */
    public DialView(Context context) {
        super(context);
        initView(null);
    }

    /**
     * FOR XML
     *
     * @param context
     * @param attrs
     */
    public DialView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    /**
     * FOR STYLING
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public DialView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    /**
     * NOT NEEDED, requires API 21
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     * @param defStyleRes
     */
    public DialView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * Helper for initializing paint instances and setting the active selection and click listener
     */
    private void initView(AttributeSet attrs) {
        // Initializing paint styles here is much more efficient than doing it at render time
        // in onDraw(), since onDraw() is called frequently
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(40f);

        mFanOnColor = Color.CYAN;
        mFanOffColor = Color.GRAY;

        // get the custom attributes
        if (attrs != null) {
            TypedArray typedArray = getContext()
                    .obtainStyledAttributes(attrs, R.styleable.DialView, 0, 0);
            // set the fan on and off colors from the attrs. default color is what is currently set
            mFanOffColor = typedArray.getColor(R.styleable.DialView_fanOffColor, mFanOffColor);
            mFanOnColor = typedArray.getColor(R.styleable.DialView_fanOnColor, mFanOnColor);
            typedArray.recycle();
        }

        mDialPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDialPaint.setColor(mFanOffColor);

        mActiveSelection = 0;

        // set the click listener
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // Rotate selection to the next valid choice.
                mActiveSelection = (mActiveSelection + 1) % SELECTION_COUNT;
                // Set dial background color to green if selection is >= 1.
                if (mActiveSelection >= 1) {
                    mDialPaint.setColor(mFanOnColor);
                    setContentDescription("This is a dial. Fan is currently at speed " +
                            mActiveSelection + ". Double tap on it to change the fan speed");
                } else {
                    mDialPaint.setColor(mFanOffColor);
                    setContentDescription("This is a dial. Fan is currently off. Double tap on it to turn on the fan");
                }

                // Redraw the view.
                invalidate();
            }
        });
    }

    /**
     * This is called when the layout is inflated, so this is a good chance to store the current
     * width and height of the view for later processing
     *
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // get the radius from width and height, just an arbitrary size within the bounds
        mWidth = w;
        mHeight = h;
        mRadius = (float) (Math.min(mWidth, mHeight) / 2 * 0.8);
    }

    /**
     * This is where you will draw your view
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /// Draw the dial.
        canvas.drawCircle(mWidth / 2, mHeight / 2, mRadius, mDialPaint);
        // Draw the text labels.
        final float labelRadius = mRadius + 20;
        StringBuffer label = mTempLabel;
        for (int i = 0; i < SELECTION_COUNT; i++) {
            float[] xyData = computeXYForPosition(i, labelRadius);
            float x = xyData[0];
            float y = xyData[1];
            label.setLength(0);
            label.append(i);
            canvas.drawText(label, 0, label.length(), x, y, mTextPaint);
        }
        // Draw the indicator mark.
        final float markerRadius = mRadius - 35;
        float[] xyData = computeXYForPosition(mActiveSelection,
                markerRadius);
        float x = xyData[0];
        float y = xyData[1];
        canvas.drawCircle(x, y, 20, mTextPaint);
    }

    /**
     * Computes x and y coordinates for the text label and indicator (0, 1, 2, 3) of the chosen
     * selection, given the position number (starting from 0) and radius
     *
     * @param pos
     * @param radius
     * @return A 2-element array, with the x in first and y in second positions
     */
    private float[] computeXYForPosition(final int pos, final float radius) {
        float[] result = mTempResult;
        Double startAngle = Math.PI * (9 / 8d);   // Angles are in radians.
        Double angle = startAngle + (pos * (Math.PI / 4));
        result[0] = (float) (radius * Math.cos(angle)) + (mWidth / 2);
        result[1] = (float) (radius * Math.sin(angle)) + (mHeight / 2);
        return result;
    }
}
