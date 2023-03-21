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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        implementationOfLayoutVar();
        if(checkCameraHardwareAvailability())
        {
            new Permissions(this);
            startScan.setOnClickListener(v -> checkVariableAndOpenActivityScanning());
        }
    }

    void implementationOfLayoutVar()
    {
        fileName = findViewById(R.id.fileName);
        numberOfScans = findViewById(R.id.numberOfScans);
        initialPrefix = findViewById(R.id.initialPrefix);
        startScan = findViewById(R.id.startScan);
    }

    boolean checkCameraHardwareAvailability() {
        if (this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)){
            return true;
        } else {
            Toast.makeText(getApplicationContext(), "No camera on this device", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    void checkVariableAndOpenActivityScanning()
    {
        if(checkIfEmpty(fileName))
        {
            String fName = getStringVar(fileName);
            if (checkIfEmpty(numberOfScans))
            {
                int nScans = getIntegerVar(numberOfScans);
                String iPrefix = getStringVar(initialPrefix);
                openActivityScanning(fName, nScans, iPrefix);
            } else {
                Toast.makeText(getApplicationContext(), "Enter the number of scans", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Enter the name of the file", Toast.LENGTH_SHORT).show();

        }
    }

    boolean checkIfEmpty(EditText var)
    {
        return !TextUtils.isEmpty(var.getText());
    }

    String getStringVar(EditText var)
    {
        return String.valueOf(var.getText());
    }

    int getIntegerVar(EditText var)
    {
        return Integer.parseInt(String.valueOf(var.getText()));
    }

    void openActivityScanning(String fName, int nScans, String iPrefix)
    {
        Intent intent = new Intent(this, Scanning.class);
        intent.putExtra("fileName", fName);
        intent.putExtra("numberOfScans", nScans);
        intent.putExtra("initialPrefix", iPrefix);
        startActivity(intent);
    }

    //Reactions to permission response received
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.REQUEST_CAMERA:
            case Constants.REQUEST_WRITE_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Permissions.checkPermissions();
                }
                break;
        }
    }
}