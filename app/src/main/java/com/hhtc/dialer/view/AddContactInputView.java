package com.hhtc.dialer.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.LinearLayout;

import com.hhtc.dialer.R;

public class AddContactInputView extends TextInputLayout {

    private TextInputEditText editText;

    public AddContactInputView(Context context) {
        super(context);
        init(null, 0);
    }

    public AddContactInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public AddContactInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.AddContactInputView, defStyleAttr, 0);

        float textSize = typedArray.getDimensionPixelSize(R.styleable.AddContactInputView_textSize, 0);

        int textColor = typedArray.getColor(R.styleable.AddContactInputView_textColor, Color.WHITE);
        int hintColor = typedArray.getColor(R.styleable.AddContactInputView_textColorHint, Color.WHITE);
        int input = typedArray.getInt(R.styleable.AddContactInputView_android_inputType, 0);
        String hint = typedArray.getString(R.styleable.AddContactInputView_hint);
        boolean enableEdit=typedArray.getBoolean(R.styleable.AddContactInputView_enableEdit,true);
        typedArray.recycle();

        editText = new TextInputEditText(getContext());
        editText.setHint(hint);
        editText.setInputType(input);
        editText.setHintTextColor(hintColor);
        editText.setEnabled(enableEdit);
        editText.setTextColor(textColor);
        editText.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize);

        addView(editText, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
    }


    @Nullable
    @Override
    public TextInputEditText getEditText() {
        return editText;
    }
}
