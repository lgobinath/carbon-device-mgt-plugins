/*
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.iot.android.sense.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.TelephonyManager;

import org.wso2.carbon.iot.android.sense.event.streams.battery.BatteryDataReceiver;
import org.wso2.carbon.iot.android.sense.event.streams.call.CallDataReceiver;
import org.wso2.carbon.iot.android.sense.event.streams.screen.ScreenDataReceiver;

public class SenseDataReceiverManager {
    private static BroadcastReceiver batteryDataReceiver;

    private static BroadcastReceiver screenDataReceiver;

    private static BroadcastReceiver callDataReceiver;

    private SenseDataReceiverManager() {

    }

    public static void registerBatteryDataReceiver(Context context) {
        if (batteryDataReceiver == null) {
            batteryDataReceiver = new BatteryDataReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Intent.ACTION_BATTERY_LOW);
            intentFilter.addAction(Intent.ACTION_BATTERY_OKAY);
            intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);

            context.registerReceiver(batteryDataReceiver, intentFilter);
        }
    }

    public static void unregisterBatteryDataReceiver(Context context) {
        if (batteryDataReceiver != null) {
            context.unregisterReceiver(batteryDataReceiver);
            batteryDataReceiver = null;
        }
    }

    public static void registerScreenDataReceiver(Context context) {
        if (screenDataReceiver == null) {
            screenDataReceiver = new ScreenDataReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Intent.ACTION_SCREEN_ON);
            intentFilter.addAction(Intent.ACTION_SCREEN_OFF);

            context.registerReceiver(screenDataReceiver, intentFilter);
        }
    }

    public static void unregisterScreenDataReceiver(Context context) {
        if (screenDataReceiver != null) {
            context.unregisterReceiver(screenDataReceiver);
            screenDataReceiver = null;
        }
    }

    public static void registerCallDataReceiver(Context context) {
        if (callDataReceiver == null) {
            callDataReceiver = new CallDataReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
            intentFilter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);

            context.registerReceiver(callDataReceiver, intentFilter);
        }
    }

    public static void unregisterCallDataReceiver(Context context) {
        if (callDataReceiver != null) {
            context.unregisterReceiver(callDataReceiver);
            callDataReceiver = null;
        }
    }
}
