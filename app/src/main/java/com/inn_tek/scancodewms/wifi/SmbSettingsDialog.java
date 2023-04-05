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

package com.inn_tek.scancodewms.wifi;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.inn_tek.scancodewms.Constants;

public class SmbSettingsDialog {

    SharedPreferences sharedPreferences;
    Context context;

    public SmbSettingsDialog(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(Constants.SMB_PREFS_NAME, MODE_PRIVATE);
    }

    public void checkIfSmbCredentialsSaved() {
        if (checkIfAllSharedPreferencesExist()) {
            optionsForExistSmbCredentials();
        }
        else {
            createSmbCredentials(false);
        }
    }

    boolean checkIfAllSharedPreferencesExist() {
        boolean allKeysExist = true;
        for (String key : Constants.smbKeys) {
            if (!sharedPreferences.contains(key)) {
                allKeysExist = false;
                break;
            }
        }
        return allKeysExist;
    }

    void optionsForExistSmbCredentials() {
        final CharSequence[] OPTIONS = {Constants.showSmbCredentials,
                                        Constants.editSmbCredentials,
                                        Constants.useSmbCredentials};

        AlertDialog.Builder optionsForExistsCredentials = new AlertDialog.Builder(context);
        optionsForExistsCredentials.setTitle(Constants.titleViewChoose);

        optionsForExistsCredentials.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        optionsForExistsCredentials.setItems(OPTIONS, (dialog, which) -> {
            String selectedOptions = OPTIONS[which].toString();

            switch (selectedOptions) {
                case Constants.showSmbCredentials:
                    showSmbCredentials();
                    break;
                case Constants.editSmbCredentials:
                    createSmbCredentials(true);
                    break;
                case Constants.useSmbCredentials:
                    useSmbCredentials();
                    break;
            }
        });

        optionsForExistsCredentials.show();
    }

    void showSmbCredentials() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(Constants.titleViewSmbCredentials);
        builder.setMessage(Constants.HOST_KEY + ": " + getHost()
                + "\n" + Constants.USERNAME_KEY + ": " + getUsername()
                + "\n" + Constants.PASSWORD_KEY + ": "  + getPassword());
        builder.setPositiveButton("BACK", (dialog, which) -> {
            dialog.dismiss();
            optionsForExistSmbCredentials();
        });
        builder.show();
    }

    void createSmbCredentials(Boolean dataExists) {
        final EditText HOST = new EditText(context),
                USERNAME = new EditText(context),
                PASSWORD = new EditText(context);

        setHint(HOST,USERNAME,PASSWORD);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(Constants.titleViewSmbCredentials);
        LinearLayout layout = createLayoutSmbCredentials(HOST,USERNAME, PASSWORD);
        builder.setView(layout);

        builder.setPositiveButton("Save", (dialog, which) -> {
            saveDataSmbCredentials(HOST,USERNAME, PASSWORD);
            optionsForExistSmbCredentials();
        });

        setNegativeButtonForBuilder(builder,dataExists);
        builder.show();
    }

    void setHint(EditText HOST, EditText USERNAME, EditText PASSWORD) {
        HOST.setHint(Constants.HOST_KEY);
        USERNAME.setHint(Constants.USERNAME_KEY);
        PASSWORD.setHint(Constants.PASSWORD_KEY);
    }

    LinearLayout createLayoutSmbCredentials(EditText HOST, EditText USERNAME, EditText PASSWORD) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(HOST);
        layout.addView(USERNAME);
        layout.addView(PASSWORD);

        return layout;
    }

    void saveDataSmbCredentials(EditText HOST, EditText USERNAME, EditText PASSWORD) {
        String host = HOST.getText().toString();
        String username = USERNAME.getText().toString();
        String password = PASSWORD.getText().toString();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.HOST_KEY, host);
        editor.putString(Constants.USERNAME_KEY, username);
        editor.putString(Constants.PASSWORD_KEY, password);
        editor.apply();
    }

    void setNegativeButtonForBuilder(AlertDialog.Builder builder, Boolean dataExists) {
        if(dataExists) {
            builder.setNegativeButton("Back", (dialog, which) -> {
                dialog.dismiss();
                optionsForExistSmbCredentials();
            });
        }
        else {
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        }
    }

    void useSmbCredentials() {
        //WifiSmb wifiSmb = new WifiSmb(context);
        Toast.makeText(context, "Start connecting", Toast.LENGTH_SHORT).show();
    }

    String getHost() {
        return sharedPreferences.getString(Constants.HOST_KEY, "");
    }

    String getUsername() {
        return sharedPreferences.getString(Constants.USERNAME_KEY, "");
    }

    String getPassword() {
        return sharedPreferences.getString(Constants.PASSWORD_KEY, "");
    }
}
