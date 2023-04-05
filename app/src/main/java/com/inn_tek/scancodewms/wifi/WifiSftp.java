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

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.inn_tek.scancodewms.Constants;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import java.io.File;

public class WifiSftp {

    Context context;
    String host, username, password, privateKeyPath, passphrase, port;

    public WifiSftp(Context context) {
        this.context = context;
        assignmentCredentials();
    }

    void assignmentCredentials() {
        SftpSettingsDialog sftpSettings = new SftpSettingsDialog(context);
        host = sftpSettings.getHost();
        username = sftpSettings.getUsername();
        password = sftpSettings.getPassword();
        port = sftpSettings.getPort();
        privateKeyPath = sftpSettings.getPrivateKeyPath();
        passphrase = sftpSettings.getPassphrase();
    }

    public void openConnection() {
        JSch jsch = new JSch();

        if(!privateKeyPath.equals("") && !passphrase.equals("")) {
            setPrivateKeyWithPassphrase(jsch, privateKeyPath, passphrase);
        }

        if(!username.equals("") && !host.equals("") && !password.equals("") && !port.equals("")) {
            try {
                Integer.parseInt(port);
            } catch (NumberFormatException e) {
                Toast.makeText(context, "Wrong port", Toast.LENGTH_SHORT).show();
                return;
            }
            Session session = getSessionWithJSch(jsch, username, host, Integer.parseInt(port));
            setSessionPassword(session, password);
            setSessionConfig(session);
            new Thread(() -> {
                connectSession(session);

                ChannelSftp sftpChannel = openSftpChannel(session);
                connectSftpChannel(sftpChannel);

                sendFiles(sftpChannel);

                closeConnection(sftpChannel, session);
            }).start();
        }
        else {
            Toast.makeText(context, "Insufficient credentials", Toast.LENGTH_SHORT).show();
        }
    }

    void setPrivateKeyWithPassphrase(JSch jsch, String privateKeyPath, String passphrase) {
        try {
            jsch.addIdentity(privateKeyPath, passphrase);
        } catch (JSchException e) {
            Log.e("jsch.addIdentity",e.getMessage());
            throw new RuntimeException(e);
        }
    }

    Session getSessionWithJSch(JSch jsch, String username, String host, int port) {
        Session session;
        try {
            session = jsch.getSession(username, host, port);
        } catch (JSchException e) {
            Log.e("session = jsch.getSession",e.getMessage());
            throw new RuntimeException(e);
        }
        return session;
    }

    void setSessionPassword(Session session, String password) {
        session.setPassword(password);
    }

    void setSessionConfig(Session session) {
        session.setConfig("StrictHostKeyChecking", "yes");
    }

    void connectSession(Session session) {
        try {
            session.connect();
        } catch (JSchException e) {
            Log.e("session.connect()",e.getMessage());
            ((Activity) context).runOnUiThread(() ->
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show());
            throw new RuntimeException(e);
        }
    }

    ChannelSftp openSftpChannel(Session session) {
        ChannelSftp sftpChannel;
        try {
            sftpChannel = (ChannelSftp) session.openChannel("sftp");
        } catch (JSchException e) {
            Log.e("sftpChannel = session.openChannel",e.getMessage());
            throw new RuntimeException(e);
        }
        return sftpChannel;
    }

    void connectSftpChannel(ChannelSftp sftpChannel) {
        try {
            sftpChannel.connect();
        } catch (JSchException e) {
            Log.e("sftpChannel.connect()",e.getMessage());
            throw new RuntimeException(e);
        }
    }

    void sendFiles(ChannelSftp sftpChannel) {
        String remoteDirectoryPath = "/remote/directory/";
        File[] localFiles = Constants.appFolder.listFiles();

        assert localFiles != null;
        for (File localFile : localFiles) {
            if (localFile.isFile()) {
                String remoteFilePath = remoteDirectoryPath + localFile.getName();
                try {
                    sftpChannel.put(localFile.getAbsolutePath(), remoteFilePath);
                } catch (SftpException e) {
                    Log.e("sftpChannel.put",e.getMessage());
                    throw new RuntimeException(e);
                }
            }
        }
    }

    void closeConnection(ChannelSftp sftpChannel, Session session) {
        sftpChannel.disconnect();
        session.disconnect();
    }
}
