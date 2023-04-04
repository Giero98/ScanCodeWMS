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

import android.content.Context;

import com.inn_tek.scancodewms.Constants;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import java.io.File;

public class WifiSftp {

    Context context;

    public WifiSftp(Context context) {
        this.context = context;
    }

    public void openConnection() {
        String host = "example.com";
        int port = 22;
        String username = "user";
        String password = "password";
        String privateKeyPath = "/path/to/private/key";
        String passphrase = "passphrase";

        JSch jsch = new JSch();

        setPrivateKeyWithPassphrase(jsch, privateKeyPath, passphrase);
        Session session = getSessionWithJSch(jsch, username, host, port);
        setSessionPassword(session, password);
        setSessionConfig(session);
        connectSession(session);

        ChannelSftp sftpChannel = openSftpChannel(session);
        connectSftpChannel(sftpChannel);

        sendFiles(sftpChannel);

        closeConnection(sftpChannel, session);
    }

    void setPrivateKeyWithPassphrase(JSch jsch, String privateKeyPath, String passphrase) {
        try {
            jsch.addIdentity(privateKeyPath, passphrase);
        } catch (JSchException e) {
            throw new RuntimeException(e);
        }
    }

    Session getSessionWithJSch(JSch jsch, String username, String host, int port) {
        Session session;
        try {
            session = jsch.getSession(username, host, port);
        } catch (JSchException e) {
            throw new RuntimeException(e);
        }
        return session;
    }

    void setSessionPassword(Session session, String password) {
        session.setPassword(password);
    }

    void setSessionConfig(Session session) {
        session.setConfig("StrictHostKeyChecking", "no");
    }

    void connectSession(Session session) {
        try {
            session.connect();
        } catch (JSchException e) {
            throw new RuntimeException(e);
        }
    }

    ChannelSftp openSftpChannel(Session session) {
        ChannelSftp sftpChannel;
        try {
            sftpChannel = (ChannelSftp) session.openChannel("sftp");
        } catch (JSchException e) {
            throw new RuntimeException(e);
        }
        return sftpChannel;
    }

    void connectSftpChannel(ChannelSftp sftpChannel) {
        try {
            sftpChannel.connect();
        } catch (JSchException e) {
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
