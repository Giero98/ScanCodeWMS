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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
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
            Toast.makeText(this, getString(R.string.no_camera), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(this, getString(R.string.enter_target_number_of_scans), Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, getString(R.string.enter_file_name), Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.about_author:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.about_author))
                        .setMessage(getString(R.string.text_about_author))
                        .setPositiveButton("OK", null);
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.change_language:
                ChangeLanguage changeLanguage = new ChangeLanguage(this,this);
                changeLanguage.chooseLanguage();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}