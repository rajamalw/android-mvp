package com.learn2crack.androidmvp.util;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.learn2crack.androidmvp.model.common.ErrorResponse;

import java.io.IOException;

import retrofit2.HttpException;

public class ErrorHandler {

    public static final String TAG = ErrorHandler.class.getSimpleName();

    public static String handleHTTPError(Throwable error) {

        String errorMessage = "";

        if (error instanceof HttpException) {

            Gson gson = new GsonBuilder().create();

            try {

                String errorBody = ((HttpException) error).response().errorBody().string();
                Log.d(TAG, "handleHTTPError: " + errorBody);
                ErrorResponse response = gson.fromJson(errorBody,ErrorResponse.class);
                errorMessage = response.getMessage();

            } catch (IOException e) {
                e.printStackTrace();
                errorMessage = e.getLocalizedMessage();
            }
        } else {
                errorMessage = "Network Error !";
        }

        return errorMessage;
    }
}
