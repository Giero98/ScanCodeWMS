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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.util.ArrayList;

public class Bt extends AppCompatActivity {
    Context context;

    public Bt(Context context) {
        this.context = context;
        checkBtIsOn();
    }

    void checkBtIsOn() {
        if (!Constants.bluetoothAdapter.isEnabled())
            initiateBtActivation();
        else
            startSendingFiles();
    }

    void initiateBtActivation() {
        startListenForBtStateChange();
        launchIntentToEnableBT();
    }

    void startListenForBtStateChange() {
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        context.getApplicationContext().registerReceiver(bluetoothStateReceiver, filter);
    }

    @SuppressLint("MissingPermission")
    void launchIntentToEnableBT() {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.getApplicationContext().startActivity(intent);
    }

    final BroadcastReceiver bluetoothStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

                switch(state) {
                    case BluetoothAdapter.STATE_ON:
                        Toast.makeText(context, "BT On", Toast.LENGTH_SHORT).show();
                        context.unregisterReceiver(this);
                        startSendingFiles();
                        break;
                    case BluetoothAdapter.STATE_OFF:
                        Toast.makeText(context, "BT Off", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }
    };

    void startSendingFiles() {
        File[] files = Constants.appFolder.listFiles();

        assert files != null;
        if(folderIsEmpty(files)) {
            ((Activity) context).runOnUiThread(() ->
                    Toast.makeText(context, "There are no file to send", Toast.LENGTH_SHORT).show());
        }
        else {
            prepareAndSendFiles(files);
        }
    }

    boolean folderIsEmpty(File[] files) {
        return files.length == 0;
    }

    void prepareAndSendFiles(File[] files) {
        ArrayList<Uri> filesList = prepareFilesListToSend (files);
        sendFilesViaBt(filesList);
    }

    ArrayList<Uri>  prepareFilesListToSend (File[] files) {
        ArrayList<Uri> filesList = new ArrayList<>();
        for (File file : files) {
            if (file.isFile()) {
                Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);
                filesList.add(uri);
            }
        }
        return filesList;
    }

    void sendFilesViaBt(ArrayList<Uri> filesList) {
        Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("*/*");
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, filesList);
        intent.setPackage("com.android.bluetooth");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.getApplicationContext().startActivity(intent);
    }
}
