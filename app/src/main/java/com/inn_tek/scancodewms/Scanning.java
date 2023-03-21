package com.inn_tek.scancodewms;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Scanning extends AppCompatActivity {
    ArrayList<String> codeList = new ArrayList<>();
    CodeScanner codeScanner;
    String fileName, initialPrefix;
    int numberOfScans;
    File appFolder;
    TextView currentNumberOfScans;
    int currentNumberOfScan = 0;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanning);

        createTheAppFolder();
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

    void createTheAppFolder()
    {
        appFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                Constants.folderName);
        if(!appFolder.exists()){
            appFolder.mkdir();
        }
    }

    void getVarFromMainActivity()
    {
        Bundle main = getIntent().getExtras();
        fileName = main.getString("fileName");
        numberOfScans = main.getInt("numberOfScans");
        initialPrefix = main.getString("initialPrefix");
    }

    void checkCodeWithInitialPrefix(String code)
    {
        if(checkPrefixTheScannedCode(code))
        {
            addCodeToTheListAndIncreasingNumberOfScan(code);
            Toast.makeText(this, "Code scanned", Toast.LENGTH_SHORT).show();
            checkIfEnoughCodesScanned();
        } else {
            Toast.makeText(this, "Not valid prefix", Toast.LENGTH_SHORT).show();
        }
    }

    boolean checkPrefixTheScannedCode(String code)
    {
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

    boolean isInitialPrefixEmpty()
    {
        return initialPrefix.isEmpty();
    }

    void addCodeToTheListAndIncreasingNumberOfScan(String code)
    {
        codeList.add(code);
        currentNumberOfScans.setText(String.valueOf(++currentNumberOfScan));
    }

    void checkIfEnoughCodesScanned()
    {
        if(codeList.size() == numberOfScans)
        {
            saveCodesToFile();
            setNumberOfScanToDefaultValue();
        }
    }

    void setNumberOfScanToDefaultValue()
    {
        currentNumberOfScan = 0;
        currentNumberOfScans.setText(String.valueOf(currentNumberOfScan));
    }

    void saveCodesToFile()
    {
        File file = new File(appFolder,setFileName() + ".csv");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fos);

            for (String data : codeList) {
                osw.write(data);
                osw.write("\n");
            }

            osw.close();
            fos.close();

            clearCodeList();
            Toast.makeText(this, "Data saved to a file", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("Write Data","Error saved data to a file: " + e.getMessage());
            Toast.makeText(this, "Error saved data to a file", Toast.LENGTH_SHORT).show();
        }
    }

    String setFileName()
    {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date currentDate = new Date();
        String currentTime = dateFormat.format(currentDate);
        return fileName + "_" + currentTime;
    }

    void clearCodeList()
    {
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
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.send_file) {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}