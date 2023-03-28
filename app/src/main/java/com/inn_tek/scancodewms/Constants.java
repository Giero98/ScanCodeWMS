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
            REQUEST_ACCESS_FINE_LOCATION = 7;

    String  folderName = "ScanCodeWMS",
            titleView = "Choose a sending method",
            dateFormatToFileName = "yyyyMMddHHmmss";

    List<Integer>   requestBasicCodes = Arrays.asList(REQUEST_CAMERA, REQUEST_WRITE_EXTERNAL_STORAGE, REQUEST_READ_EXTERNAL_STORAGE),
                    requestBtCodes = Arrays.asList(REQUEST_BT_CONNECT, REQUEST_BT_SCAN, REQUEST_BT_ADVERTISE, REQUEST_ACCESS_FINE_LOCATION);

    File appFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), folderName);

    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
}
