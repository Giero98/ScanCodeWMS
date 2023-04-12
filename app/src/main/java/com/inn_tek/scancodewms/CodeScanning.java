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

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CodeScanning extends AppCompatActivity {
    ArrayList<String> codeList = new ArrayList<>();
    CodeScanner codeScanner;
    String fileName, initialPrefix;
    File appFolder = Constants.appFolder;
    TextView currentNumberOfScans;
    int numberOfScans, currentNumberOfScan = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanning);

        createTheAppFolder();
        runQueryForFileDeletions();
        getVarFromMainActivity();

        currentNumberOfScans = findViewById(R.id.currentNumberOfScans);
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        codeScanner = new CodeScanner(this, scannerView);

        scannerView.setOnClickListener(view -> codeScanner.startPreview());
        codeScanner.setDecodeCallback(result -> runOnUiThread(() -> {
            String code = result.getText();
            checkCodeWithInitialPrefix(code);
        }));
    }

    void createTheAppFolder() {
        if(!appFolder.exists()){
            appFolder.mkdir();
        }
    }

    void runQueryForFileDeletions() {
        DeleteFiles deleteFiles = new DeleteFiles(this);
        deleteFiles.requestToDeleteFilesIfExist();
    }

    void getVarFromMainActivity() {
        Bundle main = getIntent().getExtras();
        fileName = main.getString("fileName");
        numberOfScans = main.getInt("numberOfScans");
        initialPrefix = main.getString("initialPrefix");
    }

    void checkCodeWithInitialPrefix(String code) {
        if(checkPrefixTheScannedCode(code)) {
            addCodeToTheListAndIncreasingNumberOfScan(code);
            Toast.makeText(this, getString(R.string.code_scanned), Toast.LENGTH_SHORT).show();
            checkIfEnoughCodesScanned();
        }
        else {
            Toast.makeText(this, getString(R.string.not_valid_prefix), Toast.LENGTH_SHORT).show();
        }
    }

    boolean checkPrefixTheScannedCode(String code) {
        if(isInitialPrefixEmpty()) return true;
        if(code.length() >= initialPrefix.length()) {
            for (int i = 0; i < initialPrefix.length(); i++) {
                if (code.charAt(i) != initialPrefix.charAt(i)) {
                    return false;
                }
            }
            return true;
        } else return false;
    }

    boolean isInitialPrefixEmpty() {
        return initialPrefix.isEmpty();
    }

    void addCodeToTheListAndIncreasingNumberOfScan(String code) {
        codeList.add(code);
        currentNumberOfScans.setText(String.valueOf(++currentNumberOfScan));
    }

    void checkIfEnoughCodesScanned() {
        if(codeList.size() == numberOfScans) {
            saveCodesToFile();
            setNumberOfScanToDefaultValue();
        }
    }

    void setNumberOfScanToDefaultValue() {
        currentNumberOfScan = 0;
        currentNumberOfScans.setText(String.valueOf(currentNumberOfScan));
    }

    void saveCodesToFile() {
        File file = new File(appFolder,setFileName() + ".csv");
        proceduresForSavingCodesToFile(file);
        clearCodeList();
        Toast.makeText(this,getString(R.string.data_saved_to_file),Toast.LENGTH_SHORT).show();
    }

    String setFileName() {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.dateFormatToFileName);
        Date currentDate = new Date();
        String currentTime = dateFormat.format(currentDate);
        return fileName + "_" + currentTime;
    }

    void proceduresForSavingCodesToFile(File file) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            saveCodeListToFile(osw);
            closeStreams(fos, osw);
        }
        catch (FileNotFoundException e) {
            Toast.makeText(this, getString(R.string.error_open_file_to_save_data), Toast.LENGTH_SHORT).show();
        }
    }

    void saveCodeListToFile(OutputStreamWriter osw) {
        try {
            for (String data : codeList) {
                osw.write(data);
                osw.write("\n");
            }
        }
        catch (IOException e) {
            Toast.makeText(this, getString(R.string.error_saved_data_to_file), Toast.LENGTH_SHORT).show();
        }
    }

    void closeStreams(FileOutputStream fos, OutputStreamWriter osw) {
        try {
            osw.close();
            fos.close();
        }
        catch (IOException e) {
            Toast.makeText(this, getString(R.string.error_close_streams_to_file), Toast.LENGTH_SHORT).show();
        }
    }

    void clearCodeList() {
        if (!codeList.isEmpty()) {
            codeList.clear();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        codeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        codeScanner.releaseResources();
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem    sendFile = menu.findItem(R.id.send_file),
                    aboutAuthor = menu.findItem(R.id.about_author),
                    changeLanguage = menu.findItem(R.id.change_language);
        sendFile.setVisible(true);
        aboutAuthor.setVisible(false);
        changeLanguage.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.send_file) {
            startTransferOption();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void startTransferOption() {
        TransferOption transferOption = new TransferOption();
        transferOption.select(this);
    }
}