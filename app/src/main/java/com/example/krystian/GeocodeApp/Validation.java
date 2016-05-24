package com.example.krystian.GeocodeApp;

import android.widget.EditText;

/**
 * Created by: Krystian Klimek
 * Date: 26.02.2016.
 */
public class Validation {
    public static boolean test(EditText editText, int min, String error) {
        if( editText.getText().toString().trim().length() <= min ) { //editText.getText().length()
            editText.setError(error);
            return false;
        }
        return true;
    }
}
