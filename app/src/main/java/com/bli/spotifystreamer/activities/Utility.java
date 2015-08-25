package com.bli.spotifystreamer.activities;

import android.content.Context;
import android.net.ConnectivityManager;

import java.util.concurrent.TimeUnit;

public class Utility {
    public static String convertToMinutes(long millis){
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis),
                TimeUnit.MILLISECONDS.toSeconds(millis)-TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
        );
    }

    public static String convertToMinutes(String ms){
        long millis = Long.getLong(ms);
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis),
                TimeUnit.MILLISECONDS.toSeconds(millis)-TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
        );
    }

    public static String convertToMinutes(int millis){
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis)-TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis));
        return String.format("%02d:%02d", minutes, seconds);
    }

    public static boolean isNetworkAvailable(Context context){
        final ConnectivityManager cm = ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}
