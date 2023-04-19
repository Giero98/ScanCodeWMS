/**
 * Copyright © 2023 Bartosz Gieras

 * This file is part of ScanCodeWMS.

 * ScanCodeWMS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.

 * ScanCodeWMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.inn_tek.scancodewms;

import android.bluetooth.BluetoothAdapter;
import android.os.Environment;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public interface Constants {
    int     REQUEST_CAMERA = 1,
            REQUEST_WRITE_EXTERNAL_STORAGE = 2,
            REQUEST_READ_EXTERNAL_STORAGE = 3,
            REQUEST_BT_CONNECT = 4,
            REQUEST_BT_SCAN = 5,
            REQUEST_BT_ADVERTISE = 6,
            REQUEST_ACCESS_FINE_LOCATION = 7,
            REQUEST_ACCESS_WIFI_STATE = 8,
            REQUEST_CHANGE_WIFI_STATE = 9,
            REQUEST_INTERNET = 10;

    int     bufferSize = 512 * 1024; //KB

    float   textSize = 18f;

    String  SFTP_PREFS_NAME = "SFTP_PREFS",
            SMB_PREFS_NAME = "SMB_PREFS",
            ADDRESS_KEY = "Address",
            REMOTE_DIRECTORY_PATH_KEY = "Remote directory path",
            PORT_KEY = "Port",
            USERNAME_KEY = "Username",
            PASSWORD_KEY = "Password";

    String  folderName = "ScanCodeWMS",
            sftp = "SFTP",
            smb = "SMB",
            dateFormatToFileName = "yyyyMMddHHmmss",
            link_github = "<a href=\"https://github.com/Giero98\">GitHub</a>";

    String[]    sftpKeys = {ADDRESS_KEY, REMOTE_DIRECTORY_PATH_KEY, PORT_KEY, USERNAME_KEY, PASSWORD_KEY},
                smbKeys = {ADDRESS_KEY, REMOTE_DIRECTORY_PATH_KEY, PORT_KEY, USERNAME_KEY, PASSWORD_KEY};
    List<Integer>   requestBasicCodes = Arrays.asList(REQUEST_CAMERA, REQUEST_WRITE_EXTERNAL_STORAGE, REQUEST_READ_EXTERNAL_STORAGE),
                    requestBtCodes = Arrays.asList(REQUEST_BT_CONNECT, REQUEST_BT_SCAN, REQUEST_BT_ADVERTISE, REQUEST_ACCESS_FINE_LOCATION),
                    requestWifiCodes = Arrays.asList(REQUEST_ACCESS_WIFI_STATE, REQUEST_CHANGE_WIFI_STATE, REQUEST_INTERNET);

    File appFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), folderName);

    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
}
