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
import android.content.res.Configuration;

import androidx.appcompat.app.AlertDialog;

import java.util.Locale;

public class ChangeLanguage {
    Context context;
    Activity activity;
    public ChangeLanguage(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;
    }

    public void chooseLanguage() {
        String en = context.getString(R.string.english);
        String pl = context.getString(R.string.polish);
        final String[] LANGUAGES = {en, pl};

        AlertDialog.Builder chooseLanguage = new AlertDialog.Builder(context);
        chooseLanguage.setTitle(context.getString(R.string.choose_language));

        chooseLanguage.setNegativeButton(context.getString(R.string.back), (dialog, which) -> dialog.dismiss());
        chooseLanguage.setItems(LANGUAGES, (dialog, which) -> switchLanguage(which));

        chooseLanguage.show();
    }

    void switchLanguage(int which) {
        Configuration config = new Configuration();
        switch (which) {
            case 0:
                config.locale = new Locale("en");
                break;
            case 1:
                config.locale = new Locale("pl");
                break;
        }
        context.getResources().updateConfiguration(config, null);
        activity.recreate();
    }
}
