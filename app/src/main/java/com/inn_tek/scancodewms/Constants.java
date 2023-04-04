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

    String  PREFS_NAME = "SFTP_PREFS",
            HOST_KEY = "Host",
            USERNAME_KEY = "Username",
            PASSWORD_KEY = "Password",
            PRIVATE_KEY_PATH_KEY = "Private key path",
            PASSPHRASE_KEY = "Passphrase";

    String  folderName = "ScanCodeWMS",
            titleViewOnSelectTechnology = "Choose a sending method",
            titleViewOnTransferMethodOnWifi = "Select a protocol",
            titleViewSftpCredentials = "SFTP Credentials",
            titleViewChoose = "Choose",
            showSftpSettings = "Show SFTP Credentials",
            editSftpSettings = "Change SFTP Credentials",
            useSftpCredentials = "Use SFTP Credentials",
            sftp = "SFTP",
            smb = "SMB",
            dateFormatToFileName = "yyyyMMddHHmmss",
            requestToDeleteFiles = "Do you want to delete the existing files in the application folder before scanning?";

    String[] keys = {HOST_KEY, USERNAME_KEY, PASSWORD_KEY, PRIVATE_KEY_PATH_KEY, PASSPHRASE_KEY};
    List<Integer>   requestBasicCodes = Arrays.asList(REQUEST_CAMERA, REQUEST_WRITE_EXTERNAL_STORAGE, REQUEST_READ_EXTERNAL_STORAGE),
                    requestBtCodes = Arrays.asList(REQUEST_BT_CONNECT, REQUEST_BT_SCAN, REQUEST_BT_ADVERTISE, REQUEST_ACCESS_FINE_LOCATION),
                    requestWifiCodes = Arrays.asList(REQUEST_ACCESS_WIFI_STATE, REQUEST_CHANGE_WIFI_STATE, REQUEST_INTERNET);

    File appFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), folderName);

    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
}
