package com.hhtc.dialer.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.hhtc.dialer.view.NumberTextWatcher;

public class TextWatcherUtil {

    public interface Callback {
        void callback();
    }

    private static NumberTextWatcher numberTextWatcher;

    public static void addPhoneNumberTextWatcher(EditText editText) {
        numberTextWatcher = new NumberTextWatcher(editText);
        editText.addTextChangedListener(numberTextWatcher);

    }

    public static void removePhoneNumberTextWatcher(EditText editText) {
        if (numberTextWatcher != null)
            editText.removeTextChangedListener(numberTextWatcher);

    }

    public static void addPasswordTextWatcher(final EditText editText, final Callback callback) {

        TextWatcher textWatcher = new TextWatcher() {

            private int start = 0;
            private int count = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                this.start = start;
                this.count = count;
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    String temp = s.toString();
                    char[] arr = temp.toCharArray();
                    boolean isMatch = true;

                    if (count > 0) {
                        for (int i = start; i < start + count; i++) {
                            int c = arr[i];
                            if (!((c >= 48 && c <= 57) || (c >= 65 && c <= 90) || (c >= 97 && c <= 122))) {//不是数字 && 不是小写字母 && 不是大写字母
                                isMatch = false;
                                break;
                            }
                        }
                    }

                    if (isMatch) {
                        if (callback != null)
                            callback.callback();
                    } else {
                        s.delete(start, start + count);
                    }
                } catch (Exception e) {
                }
            }
        };

        editText.addTextChangedListener(textWatcher);

    }
}
