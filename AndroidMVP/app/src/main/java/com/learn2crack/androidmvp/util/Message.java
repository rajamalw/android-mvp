package com.learn2crack.androidmvp.util;

import android.support.design.widget.Snackbar;
import android.view.View;

public class Message {

    public static void showMessage(View view, String message) {

        if (view != null)

        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }
}
