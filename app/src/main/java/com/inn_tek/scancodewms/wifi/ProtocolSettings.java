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

public class ProtocolSettings {
    SharedPreferences sharedPreferences;
    Context context;
    String protocol;

    public ProtocolSettings(Context context, String protocol) {
        this.context = context;
        this.protocol = protocol;
        if(protocol.equals(Constants.sftp)) {
            sharedPreferences = context.getSharedPreferences(Constants.SFTP_PREFS_NAME, MODE_PRIVATE);
        }
        if(protocol.equals(Constants.smb)) {
            sharedPreferences = context.getSharedPreferences(Constants.SMB_PREFS_NAME, MODE_PRIVATE);
        }
    }

    public void checkIfCredentialsSaved() {
        if (checkIfAllSharedPreferencesExist()) {
            optionsForExistCredentials();
        }
        else {
            createCredentials(false);
        }
    }

    boolean checkIfAllSharedPreferencesExist() {
        boolean allKeysExist = true;
        String[] keys = selectedKeys();
        for (String key : keys) {
            if (!sharedPreferences.contains(key)) {
                allKeysExist = false;
                break;
            }
        }
        return allKeysExist;
    }

    String[] selectedKeys() {
        String[] keys = {};
        if(protocol.equals(Constants.sftp)) {
            keys = Constants.sftpKeys;
        }
        else if(protocol.equals(Constants.smb)) {
            keys = Constants.smbKeys;
        }
        return keys;
    }

    void optionsForExistCredentials() {
        CharSequence[] options = selectedTitleOptions();

        AlertDialog.Builder optionsForExistsCredentials = new AlertDialog.Builder(context);
        optionsForExistsCredentials.setTitle(context.getString(R.string.choose));

        optionsForExistsCredentials.setNegativeButton(context.getString(R.string.cancel), (dialog, which) -> dialog.dismiss());
        optionsForExistsCredentials.setItems(options, (dialog, which) -> {
            switch (which) {
                case 0:
                    showCredentials();
                    break;
                case 1:
                    createCredentials(true);
                    break;
                case 2:
                    useCredentials();
                    break;
            }
        });

        optionsForExistsCredentials.show();
    }

    CharSequence[] selectedTitleOptions() {
        CharSequence[] options = {};
        if(protocol.equals(Constants.sftp)) {
            options = new CharSequence[]{context.getString(R.string.show_sftp),
                    context.getString(R.string.change_sftp),
                    context.getString(R.string.use_sftp)};
        }
        else if(protocol.equals(Constants.smb)) {
            options = new CharSequence[]{context.getString(R.string.show_smb),
                    context.getString(R.string.change_smb),
                    context.getString(R.string.use_smb)};
        }
        return options;
    }

    void showCredentials() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        setTitleToBuilder(builder);
        builder.setMessage(context.getString(R.string.address) + ": " + getAddress()
                + "\n" + context.getString(R.string.remote_directory_path) + ": " + getRemoteDirectoryPath()
                + "\n" + context.getString(R.string.port) + ": " + getPort()
                + "\n" + context.getString(R.string.username) + ": " + getUsername()
                + "\n" + context.getString(R.string.password) + ": "  + getPassword());
        builder.setPositiveButton(context.getString(R.string.back), (dialog, which) -> {
            dialog.dismiss();
            optionsForExistCredentials();
        });
        builder.show();
    }

    void setTitleToBuilder(AlertDialog.Builder builder) {
        if(protocol.equals(Constants.sftp)) {
            builder.setTitle(context.getString(R.string.sftp_credentials));
        }
        else if(protocol.equals(Constants.smb)) {
            builder.setTitle(context.getString(R.string.smb_credentials));
        }
    }

    void createCredentials(Boolean dataExists) {
        final EditText  ADDRESS = new EditText(context),
                        REMOTE_DIRECTORY_PATH = new EditText(context),
                        PORT = new EditText(context),
                        USERNAME = new EditText(context),
                        PASSWORD = new EditText(context);

        setHint(ADDRESS, REMOTE_DIRECTORY_PATH, PORT, USERNAME, PASSWORD);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        setTitleToBuilder(builder);
        LinearLayout layout = createLayoutCredentials(ADDRESS, REMOTE_DIRECTORY_PATH, PORT, USERNAME, PASSWORD);
        builder.setView(layout);

        builder.setPositiveButton(context.getString(R.string.save), (dialog, which) -> {
            saveDataCredentials(ADDRESS, REMOTE_DIRECTORY_PATH, PORT, USERNAME, PASSWORD);
            optionsForExistCredentials();
        });

        setNegativeButtonForBuilder(builder,dataExists);
        builder.show();
    }

    void setHint(EditText ADDRESS, EditText REMOTE_DIRECTORY_PATH, EditText PORT, EditText USERNAME, EditText PASSWORD) {
        ADDRESS.setHint(context.getString(R.string.address));
        REMOTE_DIRECTORY_PATH.setHint(context.getString(R.string.remote_directory_path));
        PORT.setHint(context.getString(R.string.port));
        USERNAME.setHint(context.getString(R.string.username));
        PASSWORD.setHint(context.getString(R.string.password));
        PASSWORD.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    LinearLayout createLayoutCredentials(EditText ADDRESS, EditText REMOTE_DIRECTORY_PATH, EditText PORT,
                                             EditText USERNAME, EditText PASSWORD) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(ADDRESS);
        layout.addView(REMOTE_DIRECTORY_PATH);
        layout.addView(PORT);
        layout.addView(USERNAME);
        layout.addView(PASSWORD);

        return layout;
    }

    void saveDataCredentials(EditText ADDRESS, EditText REMOTE_DIRECTORY_PATH, EditText PORT,
                             EditText USERNAME, EditText PASSWORD) {
        String address = ADDRESS.getText().toString();
        String remoteDirectoryPath = REMOTE_DIRECTORY_PATH.getText().toString();
        String port = PORT.getText().toString();
        String username = USERNAME.getText().toString();
        String password = PASSWORD.getText().toString();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.ADDRESS_KEY, address);
        editor.putString(Constants.REMOTE_DIRECTORY_PATH_KEY, remoteDirectoryPath);
        editor.putString(Constants.PORT_KEY, port);
        editor.putString(Constants.USERNAME_KEY, username);
        editor.putString(Constants.PASSWORD_KEY, password);

        editor.apply();
    }

    void setNegativeButtonForBuilder(AlertDialog.Builder builder, Boolean dataExists) {
        if(dataExists) {
            builder.setNegativeButton(context.getString(R.string.back), (dialog, which) -> {
                dialog.dismiss();
                optionsForExistCredentials();
            });
        }
        else {
            builder.setNegativeButton(context.getString(R.string.cancel), (dialog, which) -> dialog.dismiss());
        }
    }

    void useCredentials() {
        if(protocol.equals(Constants.sftp)) {
            WifiSftp wifiSftp = new WifiSftp(context);
            wifiSftp.openConnection();
        }
        else if(protocol.equals(Constants.smb)) {
            WifiSmb wifiSmb = new WifiSmb(context);
            wifiSmb.openConnectionAndSendFiles();
        }
    }

    public String getAddress() {
        return sharedPreferences.getString(Constants.ADDRESS_KEY, "");
    }

    public String getRemoteDirectoryPath() {
        return sharedPreferences.getString(Constants.REMOTE_DIRECTORY_PATH_KEY,"");
    }

    public String getPort() {
        return sharedPreferences.getString(Constants.PORT_KEY,"");
    }

    public String getUsername() {
        return sharedPreferences.getString(Constants.USERNAME_KEY, "");
    }

    public String getPassword() {
        return sharedPreferences.getString(Constants.PASSWORD_KEY, "");
    }
}
