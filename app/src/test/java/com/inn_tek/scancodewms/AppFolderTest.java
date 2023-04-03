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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.io.File;

@RunWith(RobolectricTestRunner.class)
public class AppFolderTest {

    String folderName;
    File appFolder;

    void createTheAppFolder() {
        if(!appFolder.exists()){
            appFolder.mkdir();
        }
    }

    @Before
    public void setUp() {
        folderName = "ScanCodeWMS";
        appFolder = RuntimeEnvironment.application.getDir(folderName, Context.MODE_PRIVATE);
    }

    @Test
    public void testFolderExists() {
        // Test that the app folder is created when it does not exist
        appFolder.delete();
        createTheAppFolder();
        assertTrue(appFolder.exists());
    }

    @Test
    public void testFolderNotCreatedIfExists() {
        // Test that the app folder is not created if it already exists
        appFolder.mkdirs();
        createTheAppFolder();
        assertTrue(appFolder.isDirectory());
    }

    @Test
    public void testFolderCreated() {
        // Test that the app folder is created when it does not exist
        appFolder.delete();
        createTheAppFolder();
        assertTrue(appFolder.isDirectory());
    }

    @Test
    public void testFolderCreatedWithCorrectName() {
        // Test that the app folder is created with the correct name
        createTheAppFolder();
        assertTrue(appFolder.exists());
        assertEquals("app_"+folderName, appFolder.getName());
    }

    @Test
    public void testFolderCreatedWithCorrectPermissions() {
        // Test that the app folder is created with the correct permissions
        createTheAppFolder();
        assertTrue(appFolder.exists());
        assertTrue(appFolder.canRead());
        assertTrue(appFolder.canWrite());
    }
}
