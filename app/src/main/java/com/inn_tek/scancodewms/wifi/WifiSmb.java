/*
*
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



package com.inn_tek.scancodewms.wifi;

import com.hierynomus.msdtyp.AccessMask;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.share.DiskShare;
import com.inn_tek.scancodewms.Constants;
import com.jcraft.jsch.Session;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.EnumSet;

public class WifiSmb {

    public WifiSmb() {

    }

    public void openConnectionAndSendFiles() {
        String hostname = "example.com";
        String username = "user";
        String password = "password";

        try (Connection connection = new Connection(hostname)) {
            AuthenticationContext ac = new AuthenticationContext(username, password.toCharArray(), "");
            Session session = connection.authenticate(ac);

            try (DiskShare share = (DiskShare) session.connectShare("sharename")) {
                sendFiles();
                closeConnection(connection, session);
            }
        }
    }

    void sendFiles() {
        String remoteDirectoryPath = "/remote/directory/";
        File[] localFiles = Constants.appFolder.listFiles();

        for (File localFile : localFiles) {
            if (localFile.isFile()) {
                String remoteFilePath = remoteDirectoryPath + localFile.getName();

                try (OutputStream outputStream = share.openFile(remoteFilePath,
                        EnumSet.of(AccessMask.GENERIC_WRITE), null, null, null, null)) {
                    byte[] buffer = new byte[1024];
                    int length;
                    try (FileInputStream inputStream = new FileInputStream(localFile)) {
                        while ((length = inputStream.read(buffer)) > 0) {
                            outputStream.write(buffer, 0, length);
                        }
                    }
                }
            }
        }
    }

    void closeConnection(Connection connection, Session session) {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        session.disconnect();
    }
}
*/
