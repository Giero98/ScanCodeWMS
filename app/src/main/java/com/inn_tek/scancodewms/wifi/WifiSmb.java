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
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.*/



package com.inn_tek.scancodewms.wifi;

import android.content.Context;
import android.widget.Toast;

import com.inn_tek.scancodewms.Constants;
import com.inn_tek.scancodewms.R;

public class WifiSmb {

    Context context;
    String address, remoteDirectoryPath, port, username, password;

    public WifiSmb(Context context) {
        this.context = context;
        assignmentCredentials();
    }

    void assignmentCredentials() {
        ProtocolSettingsDialog protocolSettings = new ProtocolSettingsDialog(context, Constants.smb);
        address = protocolSettings.getAddress();
        remoteDirectoryPath = protocolSettings.getRemoteDirectoryPath();
        port = protocolSettings.getPort();
        username = protocolSettings.getUsername();
        password = protocolSettings.getPassword();
    }

    public void openConnectionAndSendFiles() {
        if(checkIfDataIsEmpty()) {
            Toast.makeText(context, context.getString(R.string.insufficient_credentials), Toast.LENGTH_SHORT).show();
            return;
        }

        if(!checkIfPortIsNumber()) {
            Toast.makeText(context, context.getString(R.string.wrong_port), Toast.LENGTH_SHORT).show();
            return;
        }


    }

    boolean checkIfDataIsEmpty() {
        return address.equals("") && remoteDirectoryPath.equals("") && port.equals("");
    }

    boolean checkIfPortIsNumber() {
        try {
            Integer.parseInt(port);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    boolean checkIfUsernameAndPasswordIsEmpty() {
        return username.equals("") && password.equals("");
    }
}
