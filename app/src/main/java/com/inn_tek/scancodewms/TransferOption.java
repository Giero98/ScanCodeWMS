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

import android.app.AlertDialog;
import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class TransferOption {

    public void select(Context context)
    {
        LinearLayout layout = settingLayout(context);

        ImageView imageBluetooth = addingImageToLayout(context, layout, R.drawable.ic_bluetooth);
        ImageView imageWifi = addingImageToLayout(context, layout, R.drawable.ic_wifi);

        AlertDialog.Builder selectTechnology = settingAlertDialog(context, layout);

        selectTechnology.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        imageBluetooth.setOnClickListener(v -> new Bt(context));

        imageWifi.setOnClickListener(v ->
                Toast.makeText(context, "Wi-Fi", Toast.LENGTH_SHORT).show());

        showAlertDialog(selectTechnology);
    }

    LinearLayout settingLayout(Context context)
    {
        LinearLayout layout = new LinearLayout(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, // Width
                LinearLayout.LayoutParams.WRAP_CONTENT  // Height
        );
        layout.setLayoutParams(layoutParams);

        return layout;
    }

    ImageView addingImageToLayout(Context context, LinearLayout layout, int imageReference)
    {
        ImageView image = new ImageView(context);
        image.setImageResource(imageReference);
        layout.addView(image);

        return image;
    }

    AlertDialog.Builder settingAlertDialog(Context context, LinearLayout layout)
    {
        AlertDialog.Builder selectTechnology = new AlertDialog.Builder(context);
        selectTechnology.setView(layout);
        selectTechnology.setTitle(Constants.titleView);

        return selectTechnology;
    }

    void showAlertDialog(AlertDialog.Builder selectTechnology)
    {
        AlertDialog dialog = selectTechnology.create();
        dialog.show();
    }
}