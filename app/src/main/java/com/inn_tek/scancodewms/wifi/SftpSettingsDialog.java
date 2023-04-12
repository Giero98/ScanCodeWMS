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
import com.inn_tek.scancodewms.R;

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
        final CharSequence[] OPTIONS = {context.getString(R.string.show_sftp),
                                        context.getString(R.string.change_sftp),
                                        context.getString(R.string.use_sftp)};

        AlertDialog.Builder optionsForExistsCredentials = new AlertDialog.Builder(context);
        optionsForExistsCredentials.setTitle(context.getString(R.string.choose));

        optionsForExistsCredentials.setNegativeButton(context.getString(R.string.cancel), (dialog, which) -> dialog.dismiss());
        optionsForExistsCredentials.setItems(OPTIONS, (dialog, which) -> {
            switch (which) {
                case 0:
                    showSftpSettings();
                    break;
                case 1:
                    createSftpCredentials(true);
                    break;
                case 2:
                    useSftpCredentials();
                    break;
            }
        });

        optionsForExistsCredentials.show();
    }

    void showSftpSettings() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.sftp_credentials));
        builder.setMessage(context.getString(R.string.host_ip) + ": " + getHost()
                + "\n" + context.getString(R.string.username) + ": " + getUsername()
                + "\n" + context.getString(R.string.password) + ": "  + getPassword()
                + "\n" + context.getString(R.string.port) + ": " + getPort()
                + "\n" + context.getString(R.string.remote_directory_path) + ": " + getRemoteDirectoryPath());
        builder.setPositiveButton(context.getString(R.string.back), (dialog, which) -> {
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
        builder.setTitle(context.getString(R.string.sftp_credentials));
        LinearLayout layout = createLayoutSftpCredentials(HOST,USERNAME, PASSWORD, PORT, REMOTE_DIRECTORY_PATH);
        builder.setView(layout);

        builder.setPositiveButton(context.getString(R.string.save), (dialog, which) -> {
            saveDataSftpCredentials(HOST, USERNAME, PASSWORD, PORT, REMOTE_DIRECTORY_PATH);
            optionsForExistSftpCredentials();
        });

        setNegativeButtonForBuilder(builder,dataExists);
        builder.show();
    }

    void setHint(EditText HOST, EditText USERNAME, EditText PASSWORD, EditText PORT, EditText REMOTE_DIRECTORY_PATH) {
        HOST.setHint(context.getString(R.string.host_ip));
        USERNAME.setHint(context.getString(R.string.username));
        PASSWORD.setHint(context.getString(R.string.password));
        PASSWORD.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        PORT.setHint(context.getString(R.string.port));
        REMOTE_DIRECTORY_PATH.setHint(context.getString(R.string.remote_directory_path));
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
            builder.setNegativeButton(context.getString(R.string.back), (dialog, which) -> {
                dialog.dismiss();
                optionsForExistSftpCredentials();
            });
        }
        else {
            builder.setNegativeButton(context.getString(R.string.cancel), (dialog, which) -> dialog.dismiss());
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
