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

package com.inn_tek.scancodewms;

import android.content.Context;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.io.File;

public class DeleteFiles {
    Context context;
    File[] files;
    public DeleteFiles(Context context) {
        this.context = context;
    }

    public void requestToDeleteFilesIfExist() {
        files = Constants.appFolder.listFiles();
        if(!folderIsEmpty()) {
            requestToDeleteFiles();
        }
    }

    boolean folderIsEmpty() {
        return files.length == 0;
    }

    void requestToDeleteFiles() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getString(R.string.request_delete_files))
                .setPositiveButton(context.getString(R.string.yes), (dialog, id) -> deleteFilesFromAppFolder())
                .setNegativeButton(context.getString(R.string.no), (dialog, id) -> dialog.cancel());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    void deleteFilesFromAppFolder() {
        for(File file : files) {
            if(!file.delete()) {
                Toast.makeText(context, context.getString(R.string.error_delete_files), Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Toast.makeText(context, context.getString(R.string.deleted_files), Toast.LENGTH_SHORT).show();
    }
}
