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

import ohos.app.Context;
import ohos.net.NetCapabilities;
import ohos.net.NetManager;
import ohos.telephony.RadioInfoManager;
import ohos.telephony.SignalInformation;
import ohos.telephony.SimInfoManager;
import ohos.telephony.TelephonyConstants;
import java.util.List;

/**Utility class for obtaining network related information.
 *
 * @author Kanak Sony
 * @Version 1.0.0
 */
public class NetworkUtils {

    private NetworkUtils() {
    }

    public static String getNetworkMode(Context con) {
        NetManager netManager = NetManager.getInstance(con);
        NetCapabilities netCapabilities = netManager.getNetCapabilities(netManager.getDefaultNet());
        if (netCapabilities.hasCap(NetCapabilities.NET_CAPABILITY_VALIDATED)
                && netCapabilities.hasBearer(NetCapabilities.BEARER_WIFI)
                || netCapabilities.hasBearer(NetCapabilities.BEARER_WIFI_AWARE)) {
            return "Wifi";
        } else if (netCapabilities.hasCap(NetCapabilities.NET_CAPABILITY_VALIDATED)
                && netCapabilities.hasBearer(NetCapabilities.BEARER_CELLULAR)) {
            return getRadioInfo(con);
        } else {
            return "No Network";
        }
    }

    public static String getRadioInfo(Context con) {
        SimInfoManager sm = SimInfoManager.getInstance(con);
        int maxSim = sm.getMaxSimCount();
        String simMode = "";
        for (int i = 0; i < maxSim; i++) {
            if (sm.hasSimCard(i)) {
                RadioInfoManager radioInfoManager = RadioInfoManager.getInstance(con);
                List<SignalInformation> signalList = radioInfoManager.getSignalInfoList(i);
                for (SignalInformation signal : signalList) {
                    int signalNetworkType = signal.getNetworkType();
                    switch (signalNetworkType) {
                        case TelephonyConstants.NETWORK_TYPE_CDMA:
                            return simMode + (" " + "Sim " + (i + 1) + ": CDMA");
                        case TelephonyConstants.NETWORK_TYPE_GSM:
                            return simMode + (" " + "Sim " + (i + 1) + ": GSM");
                        case TelephonyConstants.NETWORK_TYPE_UNKNOWN:
                            return simMode + (" " + "Sim " + (i + 1) + ": UNKNOWN");
                        case TelephonyConstants.NETWORK_TYPE_WCDMA:
                            return simMode + (" " + "Sim " + (i + 1) + ": WCDMA");
                        case TelephonyConstants.NETWORK_TYPE_TDSCDMA:
                            return simMode + (" " + "Sim " + (i + 1) + ": TDSCDMA");
                        case TelephonyConstants.NETWORK_TYPE_LTE:
                            return simMode + (" " + "Sim " + (i + 1) + ": LTE");
                        case TelephonyConstants.NETWORK_TYPE_NR:
                            return simMode + (" " + "Sim " + (i + 1) + ": NR");
                        default:
                            return simMode;
                    }
                }

            }
        }
        return simMode;
    }
}
