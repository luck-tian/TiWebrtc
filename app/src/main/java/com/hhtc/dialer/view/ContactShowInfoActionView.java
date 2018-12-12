package com.hhtc.dialer.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hhtc.dialer.R;

/**
 *
 */
public class ContactShowInfoActionView extends LinearLayout {

    private static final int MARGIN_TOP = 4;

    private TextView showInfo;

    public ContactShowInfoActionView(Context context) {
        super(context);
        init(null, 0);
    }

    public ContactShowInfoActionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ContactShowInfoActionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ContactShowInfoActionView, defStyle, 0);
        String showActionHint = typedArray.getString(R.styleable.ContactShowInfoActionView_showActionHint);
        int showActionHintColor = typedArray.getColor(R.styleable.ContactShowInfoActionView_showActionHintColor, Color.GRAY);
        float showActionHintSize = typedArray.getDimension(R.styleable.ContactShowInfoActionView_showActionHintSize, 0);

        String showActionText = typedArray.getString(R.styleable.ContactShowInfoActionView_showActionText);
        int showActionTextColor = typedArray.getColor(R.styleable.ContactShowInfoActionView_showActionTextColor, Color.BLACK);
        float showActionTextSize = typedArray.getDimension(R.styleable.ContactShowInfoActionView_showActionTextSize, 0);

        typedArray.recycle();

        setOrientation(LinearLayout.VERTICAL);
        TextView hint = new TextView(getContext());
        hint.setText(showActionHint);
        hint.setTextSize(TypedValue.COMPLEX_UNIT_PX, showActionHintSize);
        hint.setTextColor(showActionHintColor);
        addView(hint, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, MARGIN_TOP, getResources().getDisplayMetrics());
        showInfo = new TextView(getContext());
        if (!TextUtils.isEmpty(showActionText))
            showInfo.setText(showActionText);
        showInfo.setTextColor(showActionTextColor);
        showInfo.setTextSize(TypedValue.COMPLEX_UNIT_PX, showActionTextSize);
        addView(showInfo);
    }


    public void setText(String tips) {
        showInfo.setText(tips);
    }
}
