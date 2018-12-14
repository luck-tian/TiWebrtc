package com.hhtc.dialer.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hhtc.dialer.R;

public class RemoveContentView extends LinearLayout {

    private ImageView icon;


    public RemoveContentView(Context context) {
        super(context);
        init(null, 0);
    }

    public RemoveContentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public RemoveContentView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.RemoveContentView, defStyle, 0);
        String showInfoText = typedArray.getString(R.styleable.RemoveContentView_removeText);
        int defaultSrc = typedArray.getResourceId(R.styleable.RemoveContentView_removeSrc, 0);
        int color = typedArray.getColor(R.styleable.RemoveContentView_removeTextColor, Color.BLACK);
        float showInfoIconSize = typedArray.getDimension(R.styleable.RemoveContentView_removeIconSize, 0);
        float showInfoTextSize = typedArray.getDimension(R.styleable.RemoveContentView_removeTextSize, 0);
        typedArray.recycle();

        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER);
        icon = new ImageView(getContext());
        icon.setImageResource(defaultSrc);
        addView(icon, new LinearLayout.LayoutParams((int) showInfoIconSize, (int) showInfoIconSize));

        TextView tips = new TextView(getContext());
        tips.setText(showInfoText);
        tips.setTextColor(color);
        tips.setTextSize(TypedValue.COMPLEX_UNIT_PX, showInfoTextSize);
        addView(tips, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    }
}