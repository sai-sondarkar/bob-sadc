package xyz.nvrsettle.bobsmartaadhar.Utility;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

/**
 * Created by saiso on 03-02-2017.
 */

public class MarshMallowPermission {


    public static final int CALL_Phone_PERMISSION_REQUEST_CODE = 4;
    public static final int INTERNET_PERMISSION_REQUEST_CODE = 5;
    Activity activity;

    public MarshMallowPermission(Activity activity) {
        this.activity = activity;
    }


    public boolean checkPermissionForPhone() {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkPermissionForCamera() {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public void requestPermissionForPhone(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CALL_PHONE)){
            Toast.makeText(activity, "Calling permission needed. Please allow in App Settings for calling veggiefactory.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.CALL_PHONE},CALL_Phone_PERMISSION_REQUEST_CODE);
        }
    }

    public void requestPermissionForCamera(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.INTERNET)){
            Toast.makeText(activity, "Internet permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.CAMERA},INTERNET_PERMISSION_REQUEST_CODE);
        }
    }
}
