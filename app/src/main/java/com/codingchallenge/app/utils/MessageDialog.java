package com.codingchallenge.app.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import androidx.appcompat.app.AlertDialog;

import timber.log.Timber;

public class MessageDialog {

    public static AlertDialog show(Activity activity, String title, String message,
                                   String positiveButton, String negativeButton, String neutralButton,
                                   DialogInterface.OnClickListener positiveListener,
                                   DialogInterface.OnClickListener negativeListener,
                                   DialogInterface.OnClickListener neutralListener,
                                   Drawable icon, boolean isCancelable) {

        AlertDialog dialog = null;

        try {
            final AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(activity);

            if (!TextUtils.isEmpty(title))
                builder.setTitle(title);
            if (!TextUtils.isEmpty(message))
                builder.setMessage(message);
            if (!TextUtils.isEmpty(positiveButton) || positiveListener != null)
                builder.setPositiveButton(positiveButton, positiveListener);
            if (!TextUtils.isEmpty(negativeButton) || negativeListener != null)
                builder.setNegativeButton(negativeButton, negativeListener);
            if (!TextUtils.isEmpty(neutralButton) || neutralListener != null)
                builder.setNeutralButton(neutralButton, neutralListener);
            if (icon != null)
                builder.setIcon(icon);

            builder.setCancelable(isCancelable);
            dialog = builder.create();
            dialog.show();
        } catch (Exception e) {
            Timber.e(e);
        }

        return dialog;
    }

    public static AlertDialog show(Activity activity, String title, String message,
                                   String positiveButton, String neutralButton,
                                   DialogInterface.OnClickListener positiveListener,
                                   DialogInterface.OnClickListener neutralListener) {
        return MessageDialog.show(activity, title, message,
                positiveButton, null, neutralButton,
                positiveListener, null, neutralListener,
                null, true);
    }

    public static AlertDialog show(Activity activity, String title, String message, String cancel) {
        DialogInterface.OnClickListener negativeListener = (dialog, which) -> dialog.dismiss();
        return MessageDialog.show(activity, title, message,
                null, cancel, null,
                null, negativeListener, null,
                null, true);
    }
}
