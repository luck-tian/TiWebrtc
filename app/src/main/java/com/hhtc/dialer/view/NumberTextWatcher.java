package com.hhtc.dialer.view;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class NumberTextWatcher implements TextWatcher {
    private int start = 0;
    private int before = 0;
    private int count = 0;
    private EditText editText;

    public NumberTextWatcher(EditText editText) {
        this.editText = editText;
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        this.start = start;
        this.before = before;
        this.count = count;
    }

    @Override
    public void afterTextChanged(Editable s) {
        try {
            filterPhoneNumber(s);
        } catch (Exception e) {
        }
    }

    public void filterPhoneNumber(Editable s) throws Exception {
        String temp = s.toString();
        int selection = editText.getSelectionStart();
        char[] arr = temp.toCharArray();
        boolean isMatch = true;

        if (count > 0) {
            for (int i = start; i < start + count; i++) {
                int c = arr[i];
                if (!(c == 32 || (c >= 48 && c <= 57))) {//不是空格 并且 不是数字
                    isMatch = false;
                    break;
                }
            }
        }

        if (isMatch) {

            boolean needReset = false;

            if (arr.length >= 4) {
                if (arr[3] != 32) {
                    needReset = true;
                }
            }
            if (!needReset && arr.length >= 9) {
                if (arr[8] != 32) {
                    needReset = true;
                }
            }

            if (!needReset) {
                for (int i = 0; i < arr.length; i++) {
                    if (arr[i] == 32) {
                        if (i != 3 && i != 8) {
                            needReset = true;
                            break;
                        }
                    }
                }
            }

            if (needReset) {
                temp = temp.replace(" ", "");
                if (count > 0) {
                    if (temp.length() >= 7) {
                        s.clear();
                        s.append(temp.substring(0, 3) + " " + temp.substring(3, 7) + " " + temp.substring(7, temp.length()));
                        editText.setSelection(selection - 1);
                    } else if (temp.length() >= 3) {
                        s.clear();
                        s.append(temp.substring(0, 3) + " " + temp.substring(3, temp.length()));
                        editText.setSelection(selection - 1);
                    }
                } else if (before > 0) {
                    if (selection == 3) {
                        s.clear();
                        if (temp.length() >= 8) {
                            s.append(temp.substring(0, 2) + temp.substring(3, 4) + " " + temp.substring(4, 7) + " " + temp.substring(7, temp.length()));
                        } else if (temp.length() >= 4) {
                            s.append(temp.substring(0, 2) + temp.substring(3, 4) + " " + temp.substring(4, temp.length()));
                        }
                        editText.setSelection(selection - 1);
                    } else if (selection == 8) {
                        s.clear();
                        if (temp.length() >= 8) {
                            s.append(temp.substring(0, 3) + " " + temp.substring(3, 6) + " " + temp.substring(7, temp.length()));
                        }
                        editText.setSelection(selection - 1);
                    } else if (temp.length() >= 7) {
                        s.clear();
                        s.append(temp.substring(0, 3) + " " + temp.substring(3, 7) + " " + temp.substring(7, temp.length()));
                        editText.setSelection(selection);
                    } else if (temp.length() >= 3) {
                        s.clear();
                        s.append(temp.substring(0, 3) + " " + temp.substring(3, temp.length()));
                        editText.setSelection(selection);
                    }
                }
            } else {
                if (count > 0) {
                    if (temp.length() == 3 || temp.length() == 8) {
                        s.append(" ");
                    }
                } else if (before > 0) {
                    if (temp.length() == 3 || temp.length() == 8) {
                        s.delete(temp.length() - 1, temp.length());
                    }
                }
            }

        } else {
            s.delete(start, start + count);
        }
    }
}
