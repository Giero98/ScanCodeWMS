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
import com.inn_tek.scancodewms.R;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import java.io.File;

public class WifiSftp {

    Context context;
    String host, username, password, port, remoteDirectoryPath;

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
        remoteDirectoryPath = sftpSettings.getRemoteDirectoryPath();
    }

    public void openConnection() {

        if(checkIfDataIsEmpty()) {
            Toast.makeText(context, context.getString(R.string.insufficient_credentials), Toast.LENGTH_SHORT).show();
            return;
        }

        if(!checkIfPortIsNumber()) {
            Toast.makeText(context, context.getString(R.string.wrong_port), Toast.LENGTH_SHORT).show();
            return;
        }

        Session session = getSession(username, host, Integer.parseInt(port));
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

    boolean checkIfDataIsEmpty() {
        return username.equals("") && host.equals("") && password.equals("") && port.equals("") && remoteDirectoryPath.equals("");
    }

    boolean checkIfPortIsNumber() {
        try {
            Integer.parseInt(port);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    Session getSession(String username, String host, int port) {
        JSch jsch = new JSch();
        Session session;
        try {
            session = jsch.getSession(username, host, port);
        } catch (JSchException e) {
            Toast.makeText(context, context.getString(R.string.failed_create_session), Toast.LENGTH_SHORT).show();
            throw new RuntimeException(e);
        }
        return session;
    }

    void setSessionPassword(Session session, String password) {
        session.setPassword(password);
    }

    void setSessionConfig(Session session) {
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
    }

    void connectSession(Session session) {
        try {
            session.connect();
        } catch (JSchException e) {
            ((Activity) context).runOnUiThread(() ->
                    Toast.makeText(context,context.getString(R.string.failed_connect), Toast.LENGTH_LONG).show());
            throw new RuntimeException(e);
        }
    }

    ChannelSftp openSftpChannel(Session session) {
        ChannelSftp sftpChannel;
        try {
            sftpChannel = (ChannelSftp) session.openChannel("sftp");
        } catch (JSchException e) {
            ((Activity) context).runOnUiThread(() ->
                    Toast.makeText(context,context.getString(R.string.failed_open_channel), Toast.LENGTH_LONG).show());
            throw new RuntimeException(e);
        }
        return sftpChannel;
    }

    void connectSftpChannel(ChannelSftp sftpChannel) {
        try {
            sftpChannel.connect();
        } catch (JSchException e) {
            ((Activity) context).runOnUiThread(() ->
                    Toast.makeText(context,context.getString(R.string.channel_connection_failed), Toast.LENGTH_LONG).show());
            throw new RuntimeException(e);
        }
    }

    void sendFiles(ChannelSftp sftpChannel) {
        File[] localFiles = Constants.appFolder.listFiles();

        ((Activity) context).runOnUiThread(() ->
                Toast.makeText(context, context.getString(R.string.sending_started), Toast.LENGTH_SHORT).show());

        assert localFiles != null;
        for (File localFile : localFiles) {
            if (localFile.isFile()) {
                String remoteFilePath = remoteDirectoryPath + localFile.getName();
                try {
                    sftpChannel.put(localFile.getAbsolutePath(), remoteFilePath);
                } catch (SftpException e) {
                    ((Activity) context).runOnUiThread(() ->
                            Toast.makeText(context,context.getString(R.string.filed_send_file), Toast.LENGTH_LONG).show());
                    throw new RuntimeException(e);
                }
            }
        }

        ((Activity) context).runOnUiThread(() ->
                Toast.makeText(context, context.getString(R.string.upload_complete), Toast.LENGTH_SHORT).show());
    }

    void closeConnection(ChannelSftp sftpChannel, Session session) {
        sftpChannel.disconnect();
        session.disconnect();
    }
}
