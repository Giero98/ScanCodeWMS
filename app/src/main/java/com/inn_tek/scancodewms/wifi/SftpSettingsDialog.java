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
import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;

import com.inn_tek.scancodewms.Constants;

public class SftpSettingsDialog {
    SharedPreferences sharedPreferences;
    Context context;

    public SftpSettingsDialog(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(Constants.SFTP_PREFS_NAME, MODE_PRIVATE);
    }

    public void checkIfSftpCredentialsSaved() {
        if (checkIfAllSharedPreferencesExist()) {
            optionsForExistSftpCredentials();
        }
        else {
            createSftpCredentials(false);
        }
    }

    boolean checkIfAllSharedPreferencesExist() {
        boolean allKeysExist = true;
        for (String key : Constants.sftpKeys) {
            if (!sharedPreferences.contains(key)) {
                allKeysExist = false;
                break;
            }
        }
        return allKeysExist;
    }

    void optionsForExistSftpCredentials() {
        final CharSequence[] OPTIONS = {Constants.showSftpCredentials,
                                        Constants.editSftpCredentials,
                                        Constants.useSftpCredentials};

        AlertDialog.Builder optionsForExistsCredentials = new AlertDialog.Builder(context);
        optionsForExistsCredentials.setTitle(Constants.titleViewChoose);

        optionsForExistsCredentials.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        optionsForExistsCredentials.setItems(OPTIONS, (dialog, which) -> {
            String selectedOptions = OPTIONS[which].toString();

            switch (selectedOptions) {
                case Constants.showSftpCredentials:
                    showSftpSettings();
                    break;
                case Constants.editSftpCredentials:
                    createSftpCredentials(true);
                    break;
                case Constants.useSftpCredentials:
                    useSftpCredentials();
                    break;
            }
        });

        optionsForExistsCredentials.show();
    }

    void showSftpSettings() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(Constants.titleViewSftpCredentials);
        builder.setMessage(Constants.HOST_KEY + ": " + getHost()
                + "\n" + Constants.USERNAME_KEY + ": " + getUsername()
                + "\n" + Constants.PASSWORD_KEY + ": "  + getPassword()
                + "\n" + Constants.PORT_KEY + ": " + getPort()
                + "\n" + Constants.REMOTE_DIRECTORY_PATH_KEY + ": " + getRemoteDirectoryPath());
        builder.setPositiveButton("BACK", (dialog, which) -> {
            dialog.dismiss();
            optionsForExistSftpCredentials();
        });
        builder.show();
    }

    void createSftpCredentials(Boolean dataExists) {
        final EditText  HOST = new EditText(context),
                        USERNAME = new EditText(context),
                        PASSWORD = new EditText(context),
                        PORT = new EditText(context),
                        REMOTE_DIRECTORY_PATH = new EditText(context);

        setHint(HOST, USERNAME, PASSWORD, PORT, REMOTE_DIRECTORY_PATH);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(Constants.titleViewSftpCredentials);
        LinearLayout layout = createLayoutSftpCredentials(HOST,USERNAME, PASSWORD, PORT, REMOTE_DIRECTORY_PATH);
        builder.setView(layout);

        builder.setPositiveButton("Save", (dialog, which) -> {
            saveDataSftpCredentials(HOST, USERNAME, PASSWORD, PORT, REMOTE_DIRECTORY_PATH);
            optionsForExistSftpCredentials();
        });

        setNegativeButtonForBuilder(builder,dataExists);
        builder.show();
    }

    void setHint(EditText HOST, EditText USERNAME, EditText PASSWORD, EditText PORT, EditText REMOTE_DIRECTORY_PATH) {
        HOST.setHint(Constants.HOST_KEY);
        USERNAME.setHint(Constants.USERNAME_KEY);
        PASSWORD.setHint(Constants.PASSWORD_KEY);
        PASSWORD.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        PORT.setHint(Constants.PORT_KEY);
        REMOTE_DIRECTORY_PATH.setHint(Constants.REMOTE_DIRECTORY_PATH_KEY);
    }

    LinearLayout createLayoutSftpCredentials(EditText HOST, EditText USERNAME, EditText PASSWORD,
                                             EditText PORT, EditText REMOTE_DIRECTORY_PATH) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(HOST);
        layout.addView(USERNAME);
        layout.addView(PASSWORD);
        layout.addView(PORT);
        layout.addView(REMOTE_DIRECTORY_PATH);

        return layout;
    }

    void saveDataSftpCredentials(EditText HOST, EditText USERNAME, EditText PASSWORD,
                                 EditText PORT, EditText REMOTE_DIRECTORY_PATH) {
        String host = HOST.getText().toString();
        String username = USERNAME.getText().toString();
        String password = PASSWORD.getText().toString();
        String port = PORT.getText().toString();
        String remoteDirectoryPath = REMOTE_DIRECTORY_PATH.getText().toString();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.HOST_KEY, host);
        editor.putString(Constants.USERNAME_KEY, username);
        editor.putString(Constants.PASSWORD_KEY, password);
        editor.putString(Constants.PORT_KEY, port);
        editor.putString(Constants.REMOTE_DIRECTORY_PATH_KEY, remoteDirectoryPath);
        editor.apply();
    }

    void setNegativeButtonForBuilder(AlertDialog.Builder builder, Boolean dataExists) {
        if(dataExists) {
            builder.setNegativeButton("Back", (dialog, which) -> {
                dialog.dismiss();
                optionsForExistSftpCredentials();
            });
        }
        else {
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        }
    }

    void useSftpCredentials() {
        WifiSftp wifiSftp = new WifiSftp(context);
        wifiSftp.openConnection();
    }

    public String getHost() {
        return sharedPreferences.getString(Constants.HOST_KEY, "");
    }

    public String getUsername() {
        return sharedPreferences.getString(Constants.USERNAME_KEY, "");
    }

    public String getPassword() {
        return sharedPreferences.getString(Constants.PASSWORD_KEY, "");
    }

    public String getPort() {
        return sharedPreferences.getString(Constants.PORT_KEY,"");
    }

    public String getRemoteDirectoryPath() {
        return sharedPreferences.getString(Constants.REMOTE_DIRECTORY_PATH_KEY,"");
    }
}
