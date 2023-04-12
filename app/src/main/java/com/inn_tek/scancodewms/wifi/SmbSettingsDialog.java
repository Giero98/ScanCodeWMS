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

import androidx.appcompat.app.AlertDialog;

import com.inn_tek.scancodewms.Constants;
import com.inn_tek.scancodewms.R;

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
        final CharSequence[] OPTIONS = {context.getString(R.string.show_smb),
                                        context.getString(R.string.change_smb),
                                        context.getString(R.string.use_smb)};

        AlertDialog.Builder optionsForExistsCredentials = new AlertDialog.Builder(context);
        optionsForExistsCredentials.setTitle(context.getString(R.string.choose));

        optionsForExistsCredentials.setNegativeButton(context.getString(R.string.cancel), (dialog, which) -> dialog.dismiss());
        optionsForExistsCredentials.setItems(OPTIONS, (dialog, which) -> {
            switch (which) {
                case 0:
                    showSmbCredentials();
                    break;
                case 1:
                    createSmbCredentials(true);
                    break;
                case 2:
                    useSmbCredentials();
                    break;
            }
        });

        optionsForExistsCredentials.show();
    }

    void showSmbCredentials() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.smb_credentials));
        builder.setMessage(context.getString(R.string.host_ip) + ": " + getHost()
                + "\n" + context.getString(R.string.username) + ": " + getUsername()
                + "\n" + context.getString(R.string.password) + ": "  + getPassword());
        builder.setPositiveButton(context.getString(R.string.back), (dialog, which) -> {
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
        builder.setTitle(context.getString(R.string.smb_credentials));
        LinearLayout layout = createLayoutSmbCredentials(HOST,USERNAME, PASSWORD);
        builder.setView(layout);

        builder.setPositiveButton(context.getString(R.string.save), (dialog, which) -> {
            saveDataSmbCredentials(HOST,USERNAME, PASSWORD);
            optionsForExistSmbCredentials();
        });

        setNegativeButtonForBuilder(builder,dataExists);
        builder.show();
    }

    void setHint(EditText HOST, EditText USERNAME, EditText PASSWORD) {
        HOST.setHint(context.getString(R.string.host_ip));
        USERNAME.setHint(context.getString(R.string.username));
        PASSWORD.setHint(context.getString(R.string.password));
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
            builder.setNegativeButton(context.getString(R.string.back), (dialog, which) -> {
                dialog.dismiss();
                optionsForExistSmbCredentials();
            });
        }
        else {
            builder.setNegativeButton(context.getString(R.string.cancel), (dialog, which) -> dialog.dismiss());
        }
    }

    void useSmbCredentials() {
        //WifiSmb wifiSmb = new WifiSmb(context);
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
