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

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.hierynomus.msdtyp.AccessMask;
import com.hierynomus.msfscc.FileAttributes;
import com.hierynomus.mssmb2.SMBApiException;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.common.SmbPath;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskShare;
import com.inn_tek.scancodewms.Constants;
import com.inn_tek.scancodewms.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashSet;

public class WifiSmb {

    Context context;
    String address, remoteDirectoryPath, port, username, password;

    public WifiSmb(Context context) {
        this.context = context;
        assignmentCredentials();
    }

    void assignmentCredentials() {
        ProtocolSettings protocolSettings = new ProtocolSettings(context, Constants.smb);
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

        connectAndSendByThread();
    }


    boolean checkIfDataIsEmpty() {
        return address.equals("") || remoteDirectoryPath.equals("") || port.equals("");
    }

    boolean checkIfPortIsNumber() {
        try {
            Integer.parseInt(port);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    void connectAndSendByThread() {
        new Thread (() -> {
            SMBClient client = new SMBClient();

            Connection connection = connectWithClient(client);
            Session session = getAuthenticateToSession(connection);
            DiskShare diskShare = (DiskShare) session.connectShare(remoteDirectoryPath);

            sendFiles(diskShare);

            closeConnection(connection,session,diskShare);
        }).start();
    }

    Connection connectWithClient(SMBClient client) {
        Connection connection;
        try {
            connection = client.connect(address,Integer.parseInt(port));
        } catch (IOException e) {
            ((Activity) context).runOnUiThread(() ->
                    Toast.makeText(context,context.getString(R.string.failed_connect), Toast.LENGTH_LONG).show());
            throw new RuntimeException(e);
        }
        return connection;
    }

    Session getAuthenticateToSession(Connection connection) {
        AuthenticationContext auth = new AuthenticationContext(username, password.toCharArray(), null);
        return connection.authenticate(auth);
    }

    void sendFiles(DiskShare diskShare) {
        File[] localFiles = Constants.appFolder.listFiles();

        ((Activity) context).runOnUiThread(() ->
                Toast.makeText(context,context.getString(R.string.sending_started),Toast.LENGTH_SHORT).show());

        for(File file : localFiles != null ? localFiles : new File[0]) {
            if (file.isFile()) {
                SmbPath smbPath = new SmbPath(diskShare.getSmbPath(), file.getName());

                try (InputStream inputStream = Files.newInputStream(file.toPath())) {
                    com.hierynomus.smbj.share.File remoteFile = diskShare.openFile(smbPath.getPath(),
                            new HashSet<>(Collections.singletonList(AccessMask.GENERIC_WRITE)),
                            new HashSet<>(Collections.singletonList(FileAttributes.FILE_ATTRIBUTE_NORMAL)), null, null, null);
                    try (OutputStream outputStream = remoteFile.getOutputStream()) {
                        byte[] buffer = new byte[Constants.bufferSize];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                    }
                } catch (IOException | SMBApiException e) {
                    ((Activity) context).runOnUiThread(() ->
                            Toast.makeText(context,context.getString(R.string.filed_send_file), Toast.LENGTH_LONG).show());
                    throw new RuntimeException(e);
                }
            }
        }

        ((Activity) context).runOnUiThread(() ->
                Toast.makeText(context, context.getString(R.string.upload_complete), Toast.LENGTH_SHORT).show());
    }

    void closeConnection(Connection connection, Session session, DiskShare diskShare) {
        try {
            diskShare.close();
        } catch (IOException e) {
            ((Activity) context).runOnUiThread(() ->
                    Toast.makeText(context,context.getString(R.string.failed_close_diskShare),Toast.LENGTH_SHORT).show());
            throw new RuntimeException(e);
        }
        try {
            session.close();
        } catch (IOException e) {
            ((Activity) context).runOnUiThread(() ->
                    Toast.makeText(context,context.getString(R.string.failed_close_session),Toast.LENGTH_SHORT).show());
            throw new RuntimeException(e);
        }
        try {
            connection.close();
        } catch (IOException e) {
            ((Activity) context).runOnUiThread(() ->
                    Toast.makeText(context,context.getString(R.string.failed_close_connection),Toast.LENGTH_SHORT).show());
            throw new RuntimeException(e);
        }
    }
}
