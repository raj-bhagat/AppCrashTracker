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

import static com.example.appcrashtracker.ResourceTable.Boolean_allocated_vm_size;
import static com.example.appcrashtracker.ResourceTable.Boolean_app_version;
import static com.example.appcrashtracker.ResourceTable.Boolean_battery_charging;
import static com.example.appcrashtracker.ResourceTable.Boolean_battery_charging_via;
import static com.example.appcrashtracker.ResourceTable.Boolean_battery_percentage;
import static com.example.appcrashtracker.ResourceTable.Boolean_brand_name;
import static com.example.appcrashtracker.ResourceTable.Boolean_causes;
import static com.example.appcrashtracker.ResourceTable.Boolean_class_name;
import static com.example.appcrashtracker.ResourceTable.Boolean_country;
import static com.example.appcrashtracker.ResourceTable.Boolean_device_name;
import static com.example.appcrashtracker.ResourceTable.Boolean_device_orientation;
import static com.example.appcrashtracker.ResourceTable.Boolean_external_free_space;
import static com.example.appcrashtracker.ResourceTable.Boolean_external_memory_size;
import static com.example.appcrashtracker.ResourceTable.Boolean_height;
import static com.example.appcrashtracker.ResourceTable.Boolean_internal_free_space;
import static com.example.appcrashtracker.ResourceTable.Boolean_internal_memory_size;
import static com.example.appcrashtracker.ResourceTable.Boolean_localized_message;
import static com.example.appcrashtracker.ResourceTable.Boolean_message;
import static com.example.appcrashtracker.ResourceTable.Boolean_native_allocated_size;
import static com.example.appcrashtracker.ResourceTable.Boolean_network_mode;
import static com.example.appcrashtracker.ResourceTable.Boolean_package_name;
import static com.example.appcrashtracker.ResourceTable.Boolean_release;
import static com.example.appcrashtracker.ResourceTable.Boolean_screen_layout;
import static com.example.appcrashtracker.ResourceTable.Boolean_sd_card_status;
import static com.example.appcrashtracker.ResourceTable.Boolean_sdk_version;
import static com.example.appcrashtracker.ResourceTable.Boolean_stack_trace;
import static com.example.appcrashtracker.ResourceTable.Boolean_tablet;
import static com.example.appcrashtracker.ResourceTable.Boolean_vm_free_heap_size;
import static com.example.appcrashtracker.ResourceTable.Boolean_vm_heap_size;
import static com.example.appcrashtracker.ResourceTable.Boolean_vm_max_heap_size;
import static com.example.appcrashtracker.ResourceTable.Boolean_width;
import static com.example.appcrashtracker.ResourceTable.String_key_allocated_vm_size;
import static com.example.appcrashtracker.ResourceTable.String_key_app_version;
import static com.example.appcrashtracker.ResourceTable.String_key_battery_charging_status;
import static com.example.appcrashtracker.ResourceTable.String_key_battery_charging_via;
import static com.example.appcrashtracker.ResourceTable.String_key_battery_percentage;
import static com.example.appcrashtracker.ResourceTable.String_key_brand;
import static com.example.appcrashtracker.ResourceTable.String_key_cause;
import static com.example.appcrashtracker.ResourceTable.String_key_class;
import static com.example.appcrashtracker.ResourceTable.String_key_country;
import static com.example.appcrashtracker.ResourceTable.String_key_device_orientation;
import static com.example.appcrashtracker.ResourceTable.String_key_devive;
import static com.example.appcrashtracker.ResourceTable.String_key_external_free_space;
import static com.example.appcrashtracker.ResourceTable.String_key_external_memory_size;
import static com.example.appcrashtracker.ResourceTable.String_key_height;
import static com.example.appcrashtracker.ResourceTable.String_key_internal_free_space;
import static com.example.appcrashtracker.ResourceTable.String_key_internal_memory_size;
import static com.example.appcrashtracker.ResourceTable.String_key_localised_message;
import static com.example.appcrashtracker.ResourceTable.String_key_message;
import static com.example.appcrashtracker.ResourceTable.String_key_native_allocated_size;
import static com.example.appcrashtracker.ResourceTable.String_key_network_mode;
import static com.example.appcrashtracker.ResourceTable.String_key_package_name;
import static com.example.appcrashtracker.ResourceTable.String_key_release;
import static com.example.appcrashtracker.ResourceTable.String_key_screen_layout;
import static com.example.appcrashtracker.ResourceTable.String_key_sdcard_status;
import static com.example.appcrashtracker.ResourceTable.String_key_sdk;
import static com.example.appcrashtracker.ResourceTable.String_key_stack_trace;
import static com.example.appcrashtracker.ResourceTable.String_key_tablet;
import static com.example.appcrashtracker.ResourceTable.String_key_vm_heap_size;
import static com.example.appcrashtracker.ResourceTable.String_key_vm_max_heap_size;
import static com.example.appcrashtracker.ResourceTable.String_key_width;
import static com.example.appcrashtracker.ResourceTable.String_url;
import static com.example.appcrashtracker.ResourceTable.String_value_brand;
import static com.org.appcrashtracker.utils.MemoryUtils.getAvailableExternalMemorySize;
import static com.org.appcrashtracker.utils.MemoryUtils.getAvailableInternalMemorySize;
import static com.org.appcrashtracker.utils.MemoryUtils.getConvertSize;
import static com.org.appcrashtracker.utils.MemoryUtils.getHeapSize;
import static com.org.appcrashtracker.utils.MemoryUtils.getSDCardStatus;
import static com.org.appcrashtracker.utils.MemoryUtils.getTotalExternalMemorySize;
import static com.org.appcrashtracker.utils.MemoryUtils.getTotalInternalMemorySize;
import ohos.aafwk.ability.Ability;
import ohos.bundle.BundleInfo;
import ohos.global.resource.NotExistException;
import ohos.global.resource.ResourceManager;
import ohos.global.resource.WrongTypeException;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.system.DeviceInfo;
import ohos.utils.zson.ZSONObject;
import com.org.appcrashtracker.utils.ApplicationInfoUtils;
import com.org.appcrashtracker.utils.BatteryUtils;
import com.org.appcrashtracker.utils.NetworkUtils;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;

/** Data class to handle all the data. */
public class ExceptionData {

    private final Ability ability;

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

    /**public ExceptionData constructor.
     *
     * @param ability Ability of exception handler
     */
    public ExceptionData(Ability ability) {
        this.ability = ability;
        ResourceManager resources = ability.getResourceManager();
        HiLogLabel label = new HiLogLabel(HiLog.LOG_APP, 0x00201, getClass().getName());
        String postUrl = ability.getString(String_url);
        if (postUrl.equals("default_url")) {
            HiLog.error(label, "Post url not specified");
        } else {
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
            } catch (IOException | NotExistException | WrongTypeException e) {
                HiLog.error(label, "IOException|NotExistException|WrongTypeException in Exception Data");
            }
        }
    }

    /**
     * writing the details of the requested conditions to the json file.
     *
     * @param exception Throwable exception from unhandled exception
     * @param stackTrace StringWriter it is a stack trace in string format
     */
    public void populateJsonObject(Throwable exception, StringWriter stackTrace) {
        jsonObjectData = new ZSONObject();
        if (packageName) {
            jsonObjectData.put(ability.getString(String_key_package_name),
                        ability.getBundleName());
        }
        if (className) {
            jsonObjectData.put(ability.getString(String_key_class),
                        ability.getClass().getSimpleName());
        }
        if (message) {
            jsonObjectData.put(ability.getString(String_key_message),
                        exception.getMessage());
        }
        if (localizedMessage) {
            jsonObjectData.put(ability.getString(String_key_localised_message),
                        exception.getLocalizedMessage());
        }
        if (causes) {
            jsonObjectData.put(ability.getString(String_key_cause),
                    exception.getCause());
        }
        if (stackTraceBoolean) {
            jsonObjectData.put(ability.getString(String_key_stack_trace),
                    stackTrace.toString());
        }
        setDeviceInfo();
        setScreenOrientationAndLayout();
        setInternalExternalMemoryValues();
        setBatteryValues();
        setMemoryValues();

    }


    /** setting the device info in json file. */
    private void setDeviceInfo() {
        BundleInfo bi = new BundleInfo();
        if (brandName) {
            jsonObjectData.put(ability.getString(String_key_brand),
                    ability.getString(String_value_brand));
        }
        if (deviceName) {
            jsonObjectData.put(ability.getString(String_key_devive),
                    DeviceInfo.getName());
        }
        if (sdkVersion) {
            jsonObjectData.put(ability.getString(String_key_sdk),
                    bi.getMaxSdkVersion());
        }
        if (release) {
            jsonObjectData.put(ability.getString(String_key_release),
                    bi.getVersionName());
        }
        if (height) {
            jsonObjectData.put(ability.getString(String_key_height),
                    ability.getResourceManager().getDeviceCapability().height);
        }
        if (width) {
            jsonObjectData.put(ability.getString(String_key_width),
                    ability.getResourceManager().getDeviceCapability().width);
        }
        if (appVersion) {
            jsonObjectData.put(ability.getString(String_key_app_version),
                    ApplicationInfoUtils.getAppVersion(ability));
        }
        if (tablet) {
            jsonObjectData.put(ability.getString(String_key_tablet),
                    ApplicationInfoUtils.isTablet(ability));
        }
        if (networkMode) {
            jsonObjectData.put(ability.getString(String_key_network_mode),
                    NetworkUtils.getNetworkMode(ability));
        }
        if (country) {
            jsonObjectData.put(ability.getString(String_key_country),
                    new Locale("",
                            ability.getResourceManager()
                                    .getConfiguration()
                                    .getFirstLocale()
                                    .getCountry())
                            .getDisplayCountry());
        }
    }

    /** setting the screen details in json file. */
    private void setScreenOrientationAndLayout() {
        if (deviceOrientation) {
            jsonObjectData.put(ability.getString(String_key_device_orientation),
                    ApplicationInfoUtils.getScreenOrientation(ability));
        }
        if (screenLayout) {
            jsonObjectData.put(ability.getString(String_key_screen_layout),
                    ApplicationInfoUtils.getScreenLayout(ability));
        }
    }

    /** setting the Battery info in json file. */
    private void setBatteryValues() {
        if (batteryPercentage) {
            jsonObjectData.put(ability.getString(String_key_battery_percentage),
                    BatteryUtils.getBatteryChargeLevel());
        }
        if (batteryCharging) {
            jsonObjectData.put(ability.getString(String_key_battery_charging_status),
                    BatteryUtils.getBatteryStatus());
        }
        if (batteryChargingVia) {
            jsonObjectData.put(ability.getString(String_key_battery_charging_via),
                    BatteryUtils.getBatteryChargingMode());
        }
    }

    /** setting the internal external Memory info in json file. */
    private void setInternalExternalMemoryValues() {
        if (sdCardStatus) {
            jsonObjectData.put(ability.getString(String_key_sdcard_status),
                    getSDCardStatus());
        }
        if (internalMemorySize) {
            jsonObjectData.put(ability.getString(String_key_internal_memory_size),
                    getTotalInternalMemorySize(ability));
        }
        if (externalMemorySize) {
            jsonObjectData.put(ability.getString(String_key_external_memory_size),
                    getTotalExternalMemorySize(ability));
        }
        if (internalFreeSpace) {
            jsonObjectData.put(ability.getString(String_key_internal_free_space),
                    getAvailableInternalMemorySize(ability));
        }
        if (externalFreeSpace) {
            jsonObjectData.put(ability.getString(String_key_external_free_space),
                    getAvailableExternalMemorySize(ability));
        }
    }

    /** setting the Memory info in json file. */
    private void setMemoryValues() {
        if (vmHeapSize) {
            jsonObjectData.put(ability.getString(String_key_vm_heap_size),
                    getConvertSize(Runtime.getRuntime().totalMemory()));
        }
        if (allocatedVmSize) {
            jsonObjectData.put(ability.getString(String_key_allocated_vm_size),
                    getConvertSize(Runtime.getRuntime().freeMemory()));
        }
        if (vmMaxHeapSize) {
            jsonObjectData.put(ability.getString(String_key_vm_max_heap_size),
                    getConvertSize((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())));
        }
        if (vmFreeHeapSize) {
            jsonObjectData.put(ability.getString(String_key_vm_max_heap_size),
                    getConvertSize(Runtime.getRuntime().maxMemory()));
        }
        if (nativeAllocatedSize) {
            jsonObjectData.put(ability.getString(String_key_native_allocated_size),
                    getHeapSize());
        }
    }

    /** returning the json file. */
    public ZSONObject getJsonObjectData() {
        return jsonObjectData;
    }
}




