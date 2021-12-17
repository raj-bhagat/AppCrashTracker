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

import ohos.aafwk.ability.Ability;
import ohos.app.Context;
import ohos.bundle.BundleInfo;
import ohos.bundle.IBundleManager;
import ohos.global.configuration.Configuration;
import ohos.global.configuration.DeviceCapability;
import ohos.rpc.RemoteException;

/**
 * Utility class for obtaining application related information
 * @author Kanak Sony
 * @Version 1.0.0
 */
public class ApplicationInfoUtils {

    private ApplicationInfoUtils() {
    }


    public static String getAppVersion(Context con) {
        IBundleManager manager = con.getBundleManager();
        BundleInfo info;
        try {
            info = manager.getBundleInfo(con.getBundleName(), 0);
        } catch (RemoteException e) {
            LogUtils.e("ApplicationInfoUtils", "Name not found Exception");
            return "Version not found";
        }
        return info.getVersionName();
    }

    public static boolean isTablet(Context con) {
        return (con.getResourceManager().getDeviceCapability().deviceType == DeviceCapability.DEVICE_TYPE_TABLET);
    }

    public static String getScreenOrientation(Ability abl) {
        int screenOrientation = abl.getDisplayOrientation();
        switch (screenOrientation) {
            case (Configuration.DIRECTION_HORIZONTAL):
                return "LandScape";
            case (Configuration.DIRECTION_VERTICAL):
                return "Portrait";
            default:
                return "Not Defined";
        }
    }

    public static String getScreenLayout(Ability abl) {
        int screenSize = abl.getResourceManager().getDeviceCapability().screenDensity;
        switch (screenSize) {
            case DeviceCapability.SCREEN_LDPI:
                return "Large Screen";
            case DeviceCapability.SCREEN_MDPI:
                return "Medium Screen";
            case DeviceCapability.SCREEN_SDPI:
                return "Small Screen";
            case DeviceCapability.SCREEN_XLDPI:
                return "Extra Large Screen";
            case DeviceCapability.SCREEN_XXLDPI:
                return "Extra Extra Large Screen";
            case DeviceCapability.SCREEN_XXXLDPI:
                return "Extra Extra Extra Large Screen";
            default:
                return "Screen size is not defined";
        }
    }
}
