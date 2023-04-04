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
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.widget.Toast;

public class WifiMethodsSelect {

    Context context;
    WifiManager wifiManager;

    public WifiMethodsSelect(Context context) {
        this.context = context;
    }

    public void checkWifiStatus() {
        wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            startListenForWifiStateChange();
            launchIntentToEnableWifi();
        } else {
            selectMethod();
        }
    }

    void startListenForWifiStateChange() {
        IntentFilter filter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
        context.getApplicationContext().registerReceiver(wifiStateReceiver, filter);
    }

    void launchIntentToEnableWifi() {
        Intent intent = new Intent(Settings.Panel.ACTION_WIFI);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.getApplicationContext().startActivity(intent);
    }

    final BroadcastReceiver wifiStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
                final int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);

                if (state == WifiManager.WIFI_STATE_ENABLED) {
                    context.unregisterReceiver(this);
                    selectMethod();
                }
            }
        }
    };

    void selectMethod() {
        final CharSequence[] PROTOCOLS = {Constants.ftp, Constants.smb};

        AlertDialog.Builder selectTransferMethodOnWifi = new AlertDialog.Builder(context);
        selectTransferMethodOnWifi.setTitle(Constants.titleViewOnTransferMethodOnWifi);

        selectTransferMethodOnWifi.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        selectTransferMethodOnWifi.setItems(PROTOCOLS, (dialog, which) -> {
            String selectedProtocol = PROTOCOLS[which].toString();

            switch (selectedProtocol) {
                case Constants.ftp:
                    Toast.makeText(context, "FTP", Toast.LENGTH_SHORT).show();
                    break;
                case Constants.smb:
                    Toast.makeText(context, "SMB", Toast.LENGTH_SHORT).show();
                    break;
            }
        });

        selectTransferMethodOnWifi.show();
    }
}
