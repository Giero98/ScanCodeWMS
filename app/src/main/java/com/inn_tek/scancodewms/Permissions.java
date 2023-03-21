/**
 * Copyright © 2023 Bartosz Gieras
 *
 * This file is part of ScanCodeWMS.
 *
 * ScanCodeWMS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * ScanCodeWMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */

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
