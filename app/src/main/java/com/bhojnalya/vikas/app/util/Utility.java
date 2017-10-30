package com.bhojnalya.vikas.app.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * Created by RaviAgrawal on 23-05-2017.
 */

public class Utility {
   public static final String TEL_PREFIX="tel:+91";
   public static final String CONTACT=TEL_PREFIX+"9827297890";
   //Permision code that will be checked in the method onRequestPermissionsResult
   public static final  int CALL_PHONE_PERMISSION_CODE = 101;

   public static boolean isCallingAllowed(Activity activity) {

      int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE);
      if (result == PackageManager.PERMISSION_GRANTED)
         return true;
      //If permission is not granted returning false
      return false;
   }
}
