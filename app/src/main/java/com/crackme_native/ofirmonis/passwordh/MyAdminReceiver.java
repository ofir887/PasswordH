package com.crackme_native.ofirmonis.passwordh;

import android.annotation.TargetApi;
import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

/**
 * Created by ofirmonis on 08/07/2017.
 */

//Admin receiver for getting admin permisions
public class MyAdminReceiver extends DeviceAdminReceiver {


    static String PREF_PASSWORD_QUALITY = "password_quality";
    static String PREF_PASSWORD_LENGTH = "password_length";
    static String PREF_MAX_FAILED_PW = "max_failed_pw";

    void showToast(Context context, CharSequence msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEnabled(Context context, Intent intent) {
    }

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        return "";
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onPasswordChanged(Context context, Intent intent) {
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onPasswordFailed(Context context, Intent intent) {
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onPasswordSucceeded(Context context, Intent intent) {
        showToast(context, "Your Password have been changed !!!");
    }

}
