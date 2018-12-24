package com.hhtc.dialer.plate;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.hhtc.dialer.R;
import com.hhtc.dialer.utils.IntentProvider;
import com.hhtc.dialer.utils.TextWatcherUtil;
import com.hhtc.dialer.utils.intentUnits;

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
        input.setInputType(InputType.TYPE_CLASS_TEXT);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onResume() {
        super.onResume();
        getMainExecutor().execute(() -> showSoftInputFromWindow(PlateActivity.this, input));
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
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {

            if (tab_layout.getSelectedTabPosition() == 0) {
                startBlockCall();
            } else {
                startTraditionCall();
            }

            return true;
        }
        return super.onKeyUp(keyCode, event);

    }

    /**
     * 拨打传统电话
     */
    private void startTraditionCall() {
        String number = input.getText().toString();
        if (TextUtils.isEmpty(number)) {
            Toast.makeText(getApplicationContext(), R.string.plate_inout_not_null, Toast.LENGTH_SHORT).show();
        } else if (IntentProvider.isTelephonyCalling(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), R.string.plate_call_line, Toast.LENGTH_SHORT).show();
        } else {
            intentUnits.startTradition(this, IntentProvider.getTraditionProvider(number).getIntent(getApplicationContext()));
        }

    }

    /**
     * 拨打block电话
     */
    private void startBlockCall() {
        Toast.makeText(getApplicationContext(), "startBlockCall", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
