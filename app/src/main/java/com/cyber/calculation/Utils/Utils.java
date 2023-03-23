package com.cyber.calculation.Utils;

import com.google.android.material.textfield.TextInputLayout;

public class Utils {
    public static boolean hasText(TextInputLayout textInputLayout){
        String text = textInputLayout.getEditText().getText().toString().toString();
        textInputLayout.getEditText().setError(null);

        if(text.length() == 0){
            textInputLayout.getEditText().setError("Fill all input");
            return false;
        }
        return true;
    }
}
