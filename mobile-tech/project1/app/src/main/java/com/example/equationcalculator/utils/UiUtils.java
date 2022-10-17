package com.example.equationcalculator.utils;

import android.app.Activity;
import android.widget.Toast;

public class UiUtils {

    public static void showErrorToast(String message, Activity activity) {
        Toast errorToast = Toast.makeText(activity, message, Toast.LENGTH_SHORT);
        errorToast.show();
    }

}
