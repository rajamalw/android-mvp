package com.learn2crack.androidmvp.util;

import android.text.TextUtils;
import android.util.Patterns;

public class Validation {

    public static boolean validateFields(String field){

        if (TextUtils.isEmpty(field)) {

            return false;

        } else {

            return true;
        }
    }

    public static boolean validateEmail(String string) {

        if (TextUtils.isEmpty(string) || !Patterns.EMAIL_ADDRESS.matcher(string).matches()) {

            return false;

        } else {

            return  true;
        }
    }
}