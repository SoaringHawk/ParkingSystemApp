package com.example.parkingreservationapp.utils;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    public static String formatTime(Date date) {
        return timeFormat.format(date);
    }

    public static String formatDate(Date date) {
        return dateFormat.format(date);
    }

    public static String formatDateTime(Date date) {
        return dateFormat.format(date) + " " + timeFormat.format(date);
    }
}