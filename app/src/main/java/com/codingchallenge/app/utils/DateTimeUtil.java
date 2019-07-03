package com.codingchallenge.app.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import timber.log.Timber;

public class DateTimeUtil {

    public static String formatDate(String dateTimeString, String fromDateFormat, String toDateFormat) {
        try {
            SimpleDateFormat inputDateFormat = new SimpleDateFormat(fromDateFormat, AppSettings.LOCALE);
            Date date = inputDateFormat.parse(dateTimeString);
            if (date != null) {
                SimpleDateFormat outputDateFormat = new SimpleDateFormat(toDateFormat, AppSettings.LOCALE);
                return outputDateFormat.format(date);
            }
        } catch (Exception e) {
            Timber.e(e);
        }
        return null;
    }
}
