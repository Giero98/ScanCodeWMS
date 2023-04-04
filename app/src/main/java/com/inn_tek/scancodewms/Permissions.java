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
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Permissions {
    Context context;

    public Permissions (Context context) {
        this.context = context;
        checkPermissions();
    }

    boolean checkAPI31() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.S;
    }

    public void checkPermissions() {
        if(!checkPermissionCamera()) {
            getPermissionCamera();
        }
        else if(!checkPermissionWriteExternalStorage()) {
            getPermissionWriteExternalStorage();
        }
        else if(!checkPermissionReadExternalStorage()) {
            getPermissionReadExternalStorage();
        }
        else checkBtPermissions(context);
    }

    public void checkBtPermissions(Context context) {
        if(checkSupportBt(context)) {
            if (!checkAccessFineLocation()) {
                getPermissionAccessFineLocation();
            }
            else if (!checkBtConnect()) {
                getPermissionBtConnect();
            }
            else if (!checkBtScan()) {
                getPermissionBtScan();
            }
            else if (!checkBtAdvertise()) {
                getPermissionBtAdvertise();
            }
            else checkWifiPermissions();
        }
    }

    public void checkWifiPermissions() {
        if(!checkAccessWiFiState()) {
            getPermissionAccessWiFiState();
        }
        else if(!checkChangeWiFiState()) {
            getPermissionChangeWiFiState();
        }
        else if(!checkInternet()) {
            getPermissionInternet();
        }
    }

    boolean checkSupportBt(Context context) {
        if (Constants.bluetoothAdapter == null) {
            Toast.makeText(context, "Device doesn't support Bluetooth", Toast.LENGTH_SHORT).show();
            return false;
        } else return true;
    }

    //region checkPermission

    boolean checkPermissionCamera() {
        return ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    boolean checkPermissionWriteExternalStorage() {
        return ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    boolean checkPermissionReadExternalStorage() {
        return ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    boolean checkAccessFineLocation() {
        return ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    boolean checkBtConnect() {
        if (checkAPI31()) {
            return ContextCompat.checkSelfPermission(context, android.Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED;
        }
        else return true;
    }

    boolean checkBtScan() {
        if (checkAPI31()) {
            return ContextCompat.checkSelfPermission(context, android.Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED;
        }
        else return true;
    }

    boolean checkBtAdvertise() {
        if (checkAPI31()) {
            return ContextCompat.checkSelfPermission(context, android.Manifest.permission.BLUETOOTH_ADVERTISE) == PackageManager.PERMISSION_GRANTED;
        }
        else return true;
    }

    boolean checkAccessWiFiState() {
        return ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED;
    }

    boolean checkChangeWiFiState() {
        return ContextCompat.checkSelfPermission(context, android.Manifest.permission.CHANGE_WIFI_STATE) == PackageManager.PERMISSION_GRANTED;
    }

    boolean checkInternet() {
        return ContextCompat.checkSelfPermission(context, android.Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED;
    }

    //endregion

    //region getPermission

    void getPermissionCamera() {
        ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.CAMERA}, Constants.REQUEST_CAMERA);
    }

    void getPermissionWriteExternalStorage() {
        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_WRITE_EXTERNAL_STORAGE);
    }

    void getPermissionReadExternalStorage() {
        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.REQUEST_READ_EXTERNAL_STORAGE);
    }

    void getPermissionAccessFineLocation() {
        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constants.REQUEST_ACCESS_FINE_LOCATION);
    }

    void getPermissionBtConnect() {
        if (checkAPI31()) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, Constants.REQUEST_BT_CONNECT);
        }
    }

    void getPermissionBtScan() {
        if (checkAPI31()) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.BLUETOOTH_SCAN}, Constants.REQUEST_BT_SCAN);
        }
    }
    void getPermissionBtAdvertise() {
        if (checkAPI31()) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.BLUETOOTH_ADVERTISE}, Constants.REQUEST_BT_ADVERTISE);
        }
    }

    void getPermissionAccessWiFiState() {
        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_WIFI_STATE}, Constants.REQUEST_ACCESS_WIFI_STATE);
    }

    void getPermissionChangeWiFiState() {
        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CHANGE_WIFI_STATE}, Constants.REQUEST_CHANGE_WIFI_STATE);
    }

    void getPermissionInternet() {
        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.INTERNET}, Constants.REQUEST_INTERNET);
    }

    //endregion
}
