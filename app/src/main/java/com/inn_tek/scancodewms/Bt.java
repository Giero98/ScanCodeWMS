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

package com.inn_tek.scancodewms;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class Bt {
    Context context;

    public Bt(Context context){
        this.context = context;
        //first connect or not
        startSendingFiles();
    }

    void startSendingFiles()
    {
        File[] files = Constants.appFolder.listFiles();

        assert files != null;
        if(folderIsEmpty(files))
        {
            ((Activity) context).runOnUiThread(() ->
                    Toast.makeText(context, "There are no files to send", Toast.LENGTH_SHORT).show());
        } else {
            sendFiles(files);
        }
    }

    boolean folderIsEmpty(File[] files)
    {
        return files.length == 0;
    }

    void sendFiles(File[] files)
    {
        ArrayList<Uri> filesList = new ArrayList<>();
        for (File file : files) {
            if (file.isFile()) {
                Uri uri = Uri.fromFile(file);
                filesList.add(uri);
            }
        }

        ((Activity) context).runOnUiThread(() ->
                Toast.makeText(context, "Sending will start here", Toast.LENGTH_SHORT).show());

    }
}
