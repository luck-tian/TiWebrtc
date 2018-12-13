package com.hhtc.dialer.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
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

    private ImageView icon;

    private int normalSrc;
    private int selectSrc;

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
        int defaultSrc = typedArray.getResourceId(R.styleable.ContactShowInfoView_showInfoSrc, 0);
        normalSrc = typedArray.getResourceId(R.styleable.ContactShowInfoView_showInfoNormalSrc, 0);
        selectSrc = typedArray.getResourceId(R.styleable.ContactShowInfoView_showInfoSelectSrc, 0);
        float showInfoIconSize = typedArray.getDimension(R.styleable.ContactShowInfoView_showInfoIconSize, 0);
        float showInfoTextSize = typedArray.getDimension(R.styleable.ContactShowInfoView_showInfoTextSize, 0);
        typedArray.recycle();

        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER);
        icon = new ImageView(getContext());
        icon.setImageResource(defaultSrc);
        addView(icon, new LinearLayout.LayoutParams((int) showInfoIconSize, (int) showInfoIconSize));

        TextView tips = new TextView(getContext());
        tips.setText(showInfoText);
        tips.setTextSize(TypedValue.COMPLEX_UNIT_PX, showInfoTextSize);
        addView(tips, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    }

    public void bindFavorite(boolean favorite) {
        if (favorite) {
            icon.setImageResource(selectSrc);
        } else {
            icon.setImageResource(normalSrc);
        }

    }
}
