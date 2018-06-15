package com.example.other.ttms.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Environment;

import java.util.Map;


public class NetUtil {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

       if (cm!=null && cm.getActiveNetworkInfo().isAvailable()){
            return true;
        }
        return false;
    }


    public static String getDir(){
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();

    }



}
