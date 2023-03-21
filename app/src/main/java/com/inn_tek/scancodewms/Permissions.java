package com.inn_tek.scancodewms;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Permissions {
    @SuppressLint("StaticFieldLeak")
    static Context context;

    public Permissions (Context context)
    {
        Permissions.context = context;
        checkPermissions();
    }

    public static void checkPermissions()
    {
        if(!checkPermissionCamera())
        {
            getPermissionCamera();
        }
        else if(!checkPermissionWriteExternalStorage())
        {
            getPermissionWriteExternalStorage();
        }
        else if(!checkPermissionReadExternalStorage())
        {
            getPermissionReadExternalStorage();
        }
    }

    static boolean checkPermissionCamera() {
        return ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    static boolean checkPermissionWriteExternalStorage() {
        return ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    static boolean checkPermissionReadExternalStorage() {
        return ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    static void getPermissionCamera() {
        ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.CAMERA}, Constants.REQUEST_CAMERA);
    }

    static void getPermissionWriteExternalStorage() {
        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_WRITE_EXTERNAL_STORAGE);
    }

    static void getPermissionReadExternalStorage() {
        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.REQUEST_READ_EXTERNAL_STORAGE);
    }
}
