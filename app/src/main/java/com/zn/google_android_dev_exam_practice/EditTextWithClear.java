package com.zn.google_android_dev_exam_practice;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * A custom edit text class that includes a clear ('x') button at the end of the view that
 * clears text when pressed. Supports RTL layouts
 * <p>
 * Tutorial: https://google-developer-training.github.io/android-developer-advanced-course-practicals/unit-5-advanced-graphics-and-views/lesson-10-custom-views/10-1a-p-using-custom-views/10-1a-p-using-custom-views.html
 */
public class EditTextWithClear extends AppCompatEditText {

    Drawable mClearButtonImage;

    /**
     * FOR JAVA CONSTRUCTORS
     * This is for creating this edit text programmatically
     *
     * @param context
     */
    public EditTextWithClear(Context context) {
        super(context);
        initClearButton();
    }

    /**
     * FOR XML LAYOUTS
     * This is required to inflate the view from an XML layout and apply XML attributes
     *
     * @param context
     * @param attrs
     */
    public EditTextWithClear(Context context, AttributeSet attrs) {
        super(context, attrs);
        initClearButton();
    }

    /**
     * FOR STYLING
     * This is for applying a default style to all UI elements without having to specify it in every
     * layout file
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public EditTextWithClear(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initClearButton();
    }

    /**
     * Gets the clear button from resources
     */
    private void initClearButton() {
        mClearButtonImage = ResourcesCompat
                .getDrawable(getResources(), R.drawable.ic_clear_transparent_24dp, null);

        // If the text changes, show or hide the clear button
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Only use this method for looking at when text changes and doing something with it
                showClearButton(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // If the clear button is tapped, clear the text
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (getCompoundDrawablesRelative()[2] != null) {
                    // looks for the clear button image in the second position of the array,
                    // matching to "end". if the clear button is not there, then this will not run
                    float clearButtonStart; // for LTR, clear button starts on the left side of the 'x'
                    float clearButtonEnd; // for RTL, clear button starts on the right side of the 'x'
                    boolean isClearButtonClicked = false;

                    if (getLayoutDirection() == LAYOUT_DIRECTION_LTR) {
                        // if LTR, start is on left, end is on right. so tap needs to be after
                        // this point
                        clearButtonStart = (getWidth() - getPaddingEnd() - mClearButtonImage.getIntrinsicWidth());
                        if (motionEvent.getX() > clearButtonStart) {
                            isClearButtonClicked = true;
                        }
                    } else {
                        // if RTL, end is on left and start is on right. so tap needs to be before
                        // this point
                        clearButtonEnd = mClearButtonImage.getIntrinsicWidth() + getPaddingStart();
                        if (motionEvent.getX() < clearButtonEnd) {
                            isClearButtonClicked = true;
                        }
                    }

                    if (isClearButtonClicked) {
                        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                            // if user is tapping, then change 'x' to full black
                            mClearButtonImage = ResourcesCompat.getDrawable(
                                    getResources(), R.drawable.ic_clear_black_24dp, null);
                            showClearButton(true);
                            return true;
                        }
                        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                            // if user is lifting, then clear the text and reset and hide the
                            // clear button
                            mClearButtonImage = ResourcesCompat.getDrawable(
                                    getResources(), R.drawable.ic_clear_transparent_24dp, null);
                            getText().clear();
                            showClearButton(false);
                            return true;
                        }
                    } else {
                        return false;
                    }
                }
                return false;
            }
        });
    }

    /**
     * Shows or hides the clear button
     *
     * @param show
     */
    private void showClearButton(boolean show) {
        if (show) {
            // This adds drawables to the specific bounds of the edit text
            // "end" supports RTL layout, so it will always be shown wherever "end" is on the edit
            // text, using the exact sizes of the drawables
            setCompoundDrawablesRelativeWithIntrinsicBounds(
                    null, null, mClearButtonImage, null
            );
        } else {
            setCompoundDrawablesRelativeWithIntrinsicBounds(
                    null, null, null, null
            );
        }
    }
}
