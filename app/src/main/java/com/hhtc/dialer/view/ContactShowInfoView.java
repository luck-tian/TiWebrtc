package com.hhtc.dialer.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hhtc.dialer.R;

/**
 *
 */
public class ContactShowInfoView extends LinearLayout {


    public ContactShowInfoView(Context context) {
        super(context);
        init(null, 0);
    }

    public ContactShowInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ContactShowInfoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ContactShowInfoView, defStyle, 0);
        String showInfoText = typedArray.getString(R.styleable.ContactShowInfoView_showInfoText);
        Drawable showInfoSrc = typedArray.getDrawable(R.styleable.ContactShowInfoView_showInfoSrc);
        float showInfoIconSize = typedArray.getDimension(R.styleable.ContactShowInfoView_showInfoIconSize, 0);
        float showInfoTextSize = typedArray.getDimension(R.styleable.ContactShowInfoView_showInfoTextSize, 0);
        typedArray.recycle();

        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER);
        ImageView icon = new ImageView(getContext());
        icon.setImageDrawable(showInfoSrc);
        addView(icon, new LinearLayout.LayoutParams((int) showInfoIconSize, (int) showInfoIconSize));

        TextView tips = new TextView(getContext());
        tips.setText(showInfoText);
        tips.setTextSize(TypedValue.COMPLEX_UNIT_PX, showInfoTextSize);
        addView(tips, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    }

}
