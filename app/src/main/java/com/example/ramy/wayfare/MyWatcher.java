package com.example.ramy.wayfare;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by Yahia on 12/24/2016.
 */

class MyWatcher implements TextWatcher {
    public static String mPrev;
    private EditText mEditText;

    public MyWatcher(EditText editText, String username){
        mEditText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int i, int i2, int i3) {

    }

    @Override
    public void onTextChanged(CharSequence s, int i, int i2, int i3) {
//        Log.e("username",s+"");
        mPrev = s+"";
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
