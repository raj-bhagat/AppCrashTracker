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
package com.org.appcrashtracker.data;

import ohos.aafwk.ability.Ability;
import ohos.bundle.BundleInfo;
import ohos.global.resource.NotExistException;
import ohos.global.resource.ResourceManager;
import ohos.global.resource.WrongTypeException;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.system.DeviceInfo;
import ohos.utils.zson.ZSONObject;
import static com.example.appcrashtracker.ResourceTable.*;
import static com.org.appcrashtracker.utils.MemoryUtils.*;
import com.org.appcrashtracker.utils.ApplicationInfoUtils;
import com.org.appcrashtracker.utils.BatteryUtils;
import com.org.appcrashtracker.utils.NetworkUtils;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;
/**
 * Data class to handle all the data
 * @author Raj Bhagat
 * @version 1.0.0
 */
public class ExceptionData {

    private Ability ability;
    private final HiLogLabel label = new HiLogLabel(HiLog.LOG_APP, 0x00201, getClass().getName());
    private boolean className = false;
    private boolean message = false;
    private boolean localizedMessage = false;
    private boolean causes = false;
    private boolean stackTraceBoolean = false;
    private boolean brandName = false;
    private boolean deviceName = false;
    private boolean sdkVersion = false;
    private boolean release = false;
    private boolean height = false;
    private boolean width = false;
    private boolean appVersion = false;
    private boolean tablet = false;
    private boolean deviceOrientation = false;
    private boolean screenLayout = false;
    private boolean vmHeapSize = false;
    private boolean allocatedVmSize = false;
    private boolean vmMaxHeapSize = false;
    private boolean vmFreeHeapSize = false;
    private boolean nativeAllocatedSize = false;
    private boolean batteryPercentage = false;
    private boolean batteryCharging = false;
    private boolean batteryChargingVia = false;
    private boolean sdCardStatus = false;
    private boolean internalMemorySize = false;
    private boolean externalMemorySize = false;
    private boolean internalFreeSpace = false;
    private boolean externalFreeSpace = false;
    private boolean packageName = false;
    private boolean networkMode = false;
    private boolean country = false;
    private ZSONObject jsonObjectData;

    public  ExceptionData(Ability ability) {
        this.ability = ability;
        ResourceManager resources = ability.getResourceManager();
        String postUrl = ability.getString(String_url);
        if (postUrl.equals("default_url"))
            HiLog.error(label, "Post url not specified");
        else {
            try {
                className = resources.getElement(Boolean_class_name).getBoolean();
                message = resources.getElement(Boolean_message).getBoolean();
                localizedMessage = resources.getElement(Boolean_localized_message).getBoolean();
                causes = resources.getElement(Boolean_causes).getBoolean();
                stackTraceBoolean = resources.getElement(Boolean_stack_trace).getBoolean();
                brandName = resources.getElement(Boolean_brand_name).getBoolean();
                deviceName = resources.getElement(Boolean_device_name).getBoolean();
                sdkVersion = resources.getElement(Boolean_sdk_version).getBoolean();
                release = resources.getElement(Boolean_release).getBoolean();
                height = resources.getElement(Boolean_height).getBoolean();
                width = resources.getElement(Boolean_width).getBoolean();
                appVersion = resources.getElement(Boolean_app_version).getBoolean();
                tablet = resources.getElement(Boolean_tablet).getBoolean();
                deviceOrientation = resources.getElement(Boolean_device_orientation).getBoolean();
                screenLayout = resources.getElement(Boolean_screen_layout).getBoolean();
                vmHeapSize = resources.getElement(Boolean_vm_heap_size).getBoolean();
                allocatedVmSize = resources.getElement(Boolean_allocated_vm_size).getBoolean();
                vmMaxHeapSize = resources.getElement(Boolean_vm_max_heap_size).getBoolean();
                vmFreeHeapSize = resources.getElement(Boolean_vm_free_heap_size).getBoolean();
                nativeAllocatedSize = resources.getElement(Boolean_native_allocated_size).getBoolean();
                batteryPercentage = resources.getElement(Boolean_battery_percentage).getBoolean();
                batteryCharging = resources.getElement(Boolean_battery_charging).getBoolean();
                batteryChargingVia = resources.getElement(Boolean_battery_charging_via).getBoolean();
                sdCardStatus = resources.getElement(Boolean_sd_card_status).getBoolean();
                internalMemorySize = resources.getElement(Boolean_internal_memory_size).getBoolean();
                externalMemorySize = resources.getElement(Boolean_external_memory_size).getBoolean();
                internalFreeSpace = resources.getElement(Boolean_internal_free_space).getBoolean();
                externalFreeSpace = resources.getElement(Boolean_external_free_space).getBoolean();
                packageName = resources.getElement(Boolean_package_name).getBoolean();
                networkMode = resources.getElement(Boolean_network_mode).getBoolean();
                country = resources.getElement(Boolean_country).getBoolean();
            }catch (IOException|NotExistException|WrongTypeException e) {
                HiLog.error(label, "IOException|NotExistException|WrongTypeException in Exception Data");
            }
        }
    }


    /** writing the details of the requested conditions to the json file */
    public void populateJSONObject( Throwable exception, StringWriter stackTrace) {
        jsonObjectData = new ZSONObject();
        if (packageName) {
            jsonObjectData.put("Package_Name", ability.getBundleName());
        }
        if (className) {
            jsonObjectData.put("Class", ability.getClass().getSimpleName());
        }
        if (message) {
            jsonObjectData.put("Message", exception.getMessage());
        }
        if (localizedMessage) {
            jsonObjectData.put("Localized_Message", exception.getLocalizedMessage());
        }
        if (causes) {
            jsonObjectData.put("Cause", exception.getCause());
        }
        if (stackTraceBoolean) {
            jsonObjectData.put("Stack_Trace", stackTrace.toString());
        }
        setDeviceInfo(jsonObjectData);
        setScreenOrientationAndLayout(jsonObjectData);
        setInternalExternalMemoryValues(jsonObjectData);
        setBatteryValues(jsonObjectData);
        setMemoryValues(jsonObjectData);
    }

    /** setting the device info in json file */
    public void setDeviceInfo(ZSONObject jsonObjectData) {
        BundleInfo bi = new BundleInfo();
        if (brandName) {
            jsonObjectData.put("Brand", "Huawei");
        }
        if (deviceName) {
            jsonObjectData.put("Device", DeviceInfo.getName());
        }
        if (sdkVersion) {
            jsonObjectData.put("SDK", bi.getMaxSdkVersion() + "");
        }
        if (release) {
            jsonObjectData.put("Release", bi.getVersionName());
        }
        if (height) {
            jsonObjectData.put("Height", ability.getResourceManager().getDeviceCapability().height + "");
        }
        if (width) {
            jsonObjectData.put("Width", ability.getResourceManager().getDeviceCapability().width);
        }
        if (appVersion) {
            jsonObjectData.put("App_Version", ApplicationInfoUtils.getAppVersion(ability));
        }
        if (tablet) {
            jsonObjectData.put("Tablet", " " + ApplicationInfoUtils.isTablet(ability));
        }
        if (networkMode) {
            jsonObjectData.put("Network_Mode", NetworkUtils.getNetworkMode(ability));
        }
        if (country) {
            jsonObjectData.put("Country", new Locale("", ability.getResourceManager().getConfiguration().getFirstLocale().getCountry()).getDisplayCountry());
        }
    }

    /** setting the screen details in json file */
    public void setScreenOrientationAndLayout(ZSONObject jsonObjectData) {
        if (deviceOrientation) {
            jsonObjectData.put("Device_Orientation", ApplicationInfoUtils.getScreenOrientation(ability));
        }
        if (screenLayout) {
            jsonObjectData.put("Screen_Layout", ApplicationInfoUtils.getScreenLayout(ability));
        }
        if (vmHeapSize) {
            jsonObjectData.put("VM_Heap_Size", getConvertSize(Runtime.getRuntime().totalMemory()));
        }
        if (allocatedVmSize) {
            jsonObjectData.put("Allocated_VM_Size", getConvertSize(Runtime.getRuntime().freeMemory()));
        }
        if (vmMaxHeapSize) {
            jsonObjectData.put("VM_Max_Heap_Size", getConvertSize((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())));
        }
        if (vmFreeHeapSize) {
            jsonObjectData.put("VM_free_Heap_Size", getConvertSize(Runtime.getRuntime().maxMemory()));
        }
    }

    /** setting the Battery info in json file */
    public void setBatteryValues(ZSONObject jsonObjectData) {
        if (batteryPercentage) {
            jsonObjectData.put("Battery_Percentage", BatteryUtils.getBatteryChargeLevel());
        }
        if (batteryCharging) {
            jsonObjectData.put("Battery_Charging_Status", BatteryUtils.getBatteryStatus());
        }
        if (batteryChargingVia) {
            jsonObjectData.put("Battery_Charging_Via", BatteryUtils.getBatteryChargingMode());
        }
    }

    /** setting the internal external Memory info in json file */
    public void setInternalExternalMemoryValues(ZSONObject jsonObjectData) {
        if (sdCardStatus) {
            jsonObjectData.put("SDCard_Status", getSDCardStatus());
        }
        if (internalMemorySize) {
            jsonObjectData.put("Internal_Memory_Size", getTotalInternalMemorySize(ability));
        }
        if (externalMemorySize) {
            jsonObjectData.put("External_Memory_Size", getTotalExternalMemorySize(ability));
        }
        if (internalFreeSpace) {
            jsonObjectData.put("Internal_Free_Space", getAvailableInternalMemorySize(ability));
        }
        if (externalFreeSpace) {
            jsonObjectData.put("External_Free_Space", getAvailableExternalMemorySize(ability));
        }
        if (networkMode) {
            jsonObjectData.put("Network_Mode", NetworkUtils.getNetworkMode(ability));
        }
    }

    /** setting the Memory info in json file */
    public void setMemoryValues(ZSONObject jsonObjectData) {
        if (vmHeapSize) {
            jsonObjectData.put("VM_Heap_Size", getConvertSize(Runtime.getRuntime().totalMemory()));
        }
        if (country) {
            jsonObjectData.put("Country", new Locale("", ability.getResourceManager().getConfiguration().getFirstLocale().getCountry()).getDisplayCountry());
            if (allocatedVmSize) {
                jsonObjectData.put("Allocated_VM_Size", getConvertSize(Runtime.getRuntime().freeMemory()));
            }
            if (vmMaxHeapSize) {
                jsonObjectData.put("VM_Max_Heap_Size",
                        getConvertSize((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())));
            }
            if (vmFreeHeapSize) {
                jsonObjectData.put("VM_free_Heap_Size", getConvertSize(Runtime.getRuntime().maxMemory()));
            }
            if (nativeAllocatedSize) {
                jsonObjectData.put("Native_Allocated_Size", getHeapSize());
            }
        }
    }

    /** returning the json file */
    public ZSONObject getJsonObjectData(){
        return jsonObjectData;
    }
}




