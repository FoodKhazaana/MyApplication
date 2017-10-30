package com.bhojnalya.vikas.app.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by RaviAgrawal on 02-09-2017.
 */

public class NetworkHelper {
    public static boolean isNetworkAvailable(Context act)
    {
        NetworkInfo localNetworkInfo = ((ConnectivityManager)act.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if ((localNetworkInfo != null) && (localNetworkInfo.isConnected()))
        {
            Log.e("Network Testing", "***Available***");
            return true;
        }
        Log.e("Network Testing", "***Not Available***");
        return false;
    }
}
