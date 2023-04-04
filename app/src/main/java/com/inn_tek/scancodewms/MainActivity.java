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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText fileName, numberOfScans, initialPrefix;
    Button startScan;
    Permissions permissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        implementationOfLayoutVar();
        if(checkCameraHardwareAvailability()) {
            permissions = new Permissions(this);
            startScan.setOnClickListener(v -> checkVariableAndOpenActivityScanning());
        }
    }

    void implementationOfLayoutVar() {
        fileName = findViewById(R.id.fileName);
        numberOfScans = findViewById(R.id.numberOfScans);
        initialPrefix = findViewById(R.id.initialPrefix);
        startScan = findViewById(R.id.startScan);
    }

    boolean checkCameraHardwareAvailability() {
        if (this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)){
            return true;
        }
        else {
            Toast.makeText(this, "No camera on this device", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    void checkVariableAndOpenActivityScanning() {
        if(checkIfEmpty(fileName)) {
            String fName = getStringVar(fileName);
            if (checkIfEmpty(numberOfScans)) {
                int nScans = getIntegerVar(numberOfScans);
                String iPrefix = getStringVar(initialPrefix);
                openActivityScanning(fName, nScans, iPrefix);
            }
            else {
                Toast.makeText(this, "Enter the number of scans", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "Enter the name of the file", Toast.LENGTH_SHORT).show();
        }
    }

    boolean checkIfEmpty(EditText var) {
        return !TextUtils.isEmpty(var.getText());
    }

    String getStringVar(EditText var) {
        return String.valueOf(var.getText());
    }

    int getIntegerVar(EditText var) {
        return Integer.parseInt(String.valueOf(var.getText()));
    }

    void openActivityScanning(String fName, int nScans, String iPrefix) {
        Intent intent = new Intent(this, CodeScanning.class);
        intent.putExtra("fileName", fName);
        intent.putExtra("numberOfScans", nScans);
        intent.putExtra("initialPrefix", iPrefix);
        startActivity(intent);
    }

    //Reactions to permission response received
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] perms, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, perms, grantResults);
        if(Constants.requestBasicCodes.contains(requestCode)) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permissions.checkPermissions();
            }
        }
        else if(Constants.requestBtCodes.contains(requestCode)) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permissions.checkBtPermissions(this);
            }
        }
        else if(Constants.requestWifiCodes.contains(requestCode)) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permissions.checkWifiPermissions();
            }
        }
    }
}