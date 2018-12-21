package com.hhtc.dialer.plate;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.hhtc.dialer.R;
import com.hhtc.dialer.utils.TextWatcherUtil;

public class PlateActivity extends AppCompatActivity implements TabLayout.BaseOnTabSelectedListener {

    private EditText input;

    private TabLayout tab_layout;

    private String block, tradition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plate);
        initView();
        setData();
    }

    private void initView() {
        input = findViewById(R.id.input);
        tab_layout = findViewById(R.id.tab_layout);
    }


    private void setData() {
        tab_layout.addOnTabSelectedListener(this);
    }

    public void callTips(View view) {
        showSoftInputFromWindow(this, input);
    }

    public static void showSoftInputFromWindow(Activity activity, EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, 0);
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        switch (tab.getPosition()) {
            case 0:

                tradition = input.getText().toString();

                input.setText(block);
                input.setBackground(null);
                input.setSelection(input.getText().length());

                input.setInputType(InputType.TYPE_CLASS_TEXT);
                showSoftInputFromWindow(this, input);

                TextWatcherUtil.removePhoneNumberTextWatcher(input);
                break;
            case 1:

                block = input.getText().toString();
                input.setText(tradition);
                input.setBackground(null);
                input.setSelection(input.getText().length());

                input.setInputType(InputType.TYPE_CLASS_PHONE);
                showSoftInputFromWindow(this, input);

                TextWatcherUtil.addPhoneNumberTextWatcher(input);
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
