package com.fitwebappclient.interfaces;

import android.graphics.Bitmap;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageButton;

import com.fitwebappclient.R;
import com.google.android.material.textfield.TextInputLayout;

public interface Checker {
    default String checkValueInTextInputLayout(TextInputLayout textInputLayout, View root) {
        String value = textInputLayout.getEditText().getText().toString();
        if (value.isEmpty()) {
            textInputLayout.setError(root.getResources().getString(R.string.error_empty_field));
            textInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    textInputLayout.setError(null);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            return null;
        } else {
            return value;
        }
    }
}
