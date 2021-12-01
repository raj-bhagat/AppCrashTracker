/*
 * Copyright (C) 2020-21 Application Library Engineering Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.org.appcrashtracker.utils;

import ohos.batterymanager.BatteryInfo;

/**
 * Utility class for obtaining battery related information
 * @author Kanak Sony
 * @Version 1.0.0
 */
public class BatteryUtils {
    private static final String UNKNOWN = "Unknown";

    private BatteryUtils() {
    }

    public static String getBatteryStatus() {
        BatteryInfo bi = new BatteryInfo();
        BatteryInfo.BatteryChargeState status = bi.getChargingStatus();
        switch (status) {
            case DISABLE:
                return "Not Charging";
            case ENABLE:
                return "Charging";
            case FULL:
                return "Battery Full";
            case RESERVED:
                return "Reserved";
            default:
                return UNKNOWN;
        }
    }

    public static String getBatteryChargingMode() {
        BatteryInfo bi = new BatteryInfo();
        BatteryInfo.BatteryPluggedType type = bi.getPluggedType();
        switch (type) {
            case AC:
                return "AC";
            case USB:
                return "USB";
            case WIRELESS:
                return "WIRELESS";
            default:
                return UNKNOWN;
        }
    }

    public static String getBatteryChargeLevel() {
        BatteryInfo bi = new BatteryInfo();
        BatteryInfo.BatteryLevel level = bi.getBatteryLevel();
        switch (level) {
            case LOW:
                return "LOW";
            case NORMAL:
                return "NORMAL";
            case HIGH:
                return "HIGH";
            case RESERVED:
                return "RESERVED";
            case EMERGENCY:
                return "EMERGENCY";
            default:
                return UNKNOWN;
        }
    }
}
