package com.org.appcrashtracker;

import com.org.appcrashtracker.utils.ApplicationInfoUtils;
import com.org.appcrashtracker.utils.BatteryUtils;
import com.org.appcrashtracker.utils.NetworkUtils;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.app.Context;
import ohos.app.Environment;
import ohos.bundle.BundleInfo;
import ohos.global.resource.NotExistException;
import ohos.global.resource.ResourceManager;
import ohos.global.resource.WrongTypeException;
import ohos.hiviewdfx.HiChecker;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.system.DeviceInfo;
import ohos.utils.zson.ZSONException;
import ohos.utils.zson.ZSONObject;

import java.io.*;
import java.util.Locale;

import static com.example.appcrashtracker.ResourceTable.*;
import static com.org.appcrashtracker.utils.MemoryUtils.*;

public class ExceptionHandler implements java.lang.Thread.UncaughtExceptionHandler {
    private final Ability ability;
    private ZSONObject jObjectData;
    private String abilityName;
    private final HiLogLabel LABEL = new HiLogLabel(HiLog.LOG_APP, 0x00201, getClass().getName());
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

    public ExceptionHandler(Ability ability) throws NotExistException, WrongTypeException, IOException {
        this.ability = ability;
        abilityName = ability.getClass().getSimpleName();
        ResourceManager resources = ability.getResourceManager();
        String postUrl = ability.getString(String_url);
        if (postUrl.equals("default_url"))
            HiLog.error(LABEL, "Post url not specified");
        else {
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
        }
    }

    public void uncaughtException(Thread thread, Throwable exception) {
        StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));

        HiChecker.removeAllRules();
        jObjectData = new ZSONObject();
        try {
            populateJSONObject(jObjectData, exception, stackTrace);
        } catch (ZSONException e) {
            HiLog.error(LABEL, "JSON Exception");
        }

        /* To-Do: Add UploadCrashToServer() method here */

        writeToDocuments(ability, jObjectData);

        Intent intent = new Intent();
        Operation operation = new Intent.OperationBuilder()
                .withBundleName(ability.getBundleName())
                .withAbilityName(ability.getAbilityName())
                .build();
        intent.setOperation(operation);
        ability.startAbility(intent);
    }

    private void writeToDocuments(Context context, ZSONObject data) {
        File storageFolder = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(storageFolder, "crash.txt");
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            fileOutputStream.write(data.toString().getBytes());
        } catch (Exception e) {
            HiLog.error(LABEL, "Text file Error ");
        }
    }


    private void populateJSONObject(ZSONObject jObjectData, Throwable exception, StringWriter stackTrace) {
        if (packageName) {
            jObjectData.put("Package_Name", ability.getBundleName());
        }
        if (className) {
            jObjectData.put("Class", abilityName);
        }
        if (message) {
            jObjectData.put("Message", exception.getMessage());
        }
        if (localizedMessage) {
            jObjectData.put("Localized_Message", exception.getLocalizedMessage());
        }
        if (causes) {
            jObjectData.put("Cause", exception.getCause());
        }
        if (stackTraceBoolean) {
            jObjectData.put("Stack_Trace", stackTrace.toString());
        }
        setDeviceInfo(jObjectData);
        setScreenOrientationAndLayout(ability);
        setInternalExternalMemoryValues(jObjectData);
        setBatteryValues(jObjectData);
        setMemoryValues(jObjectData);
    }

    private void setDeviceInfo(ZSONObject jObjectData) {
        BundleInfo bi = new BundleInfo();
        if (brandName) {
            jObjectData.put("Brand", "Huawei");
        }
        if (deviceName) {
            jObjectData.put("Device", DeviceInfo.getName());
        }
        if (sdkVersion) {
            jObjectData.put("SDK", bi.getMaxSdkVersion() + "");
        }
        if (release) {
            jObjectData.put("Release", bi.getVersionName());
        }
        if (height) {
            jObjectData.put("Height", ability.getResourceManager().getDeviceCapability().height + "");
        }
        if (width) {
            jObjectData.put("Width", ability.getResourceManager().getDeviceCapability().width);
        }
        if (appVersion) {
            jObjectData.put("App_Version", ApplicationInfoUtils.getAppVersion(ability));
        }
        if (tablet) {
            jObjectData.put("Tablet", " " + ApplicationInfoUtils.isTablet(ability));
        }
        if (networkMode) {
            jObjectData.put("Network_Mode", NetworkUtils.getNetworkMode(ability));
        }
        if (country) {
            jObjectData.put("Country", new Locale("", ability.getResourceManager().getConfiguration().getFirstLocale().getCountry()).getDisplayCountry());
        }
    }

    private void setScreenOrientationAndLayout(Ability ability) {
        if (deviceOrientation) {
            jObjectData.put("Device_Orientation", ApplicationInfoUtils.getScreenOrientation(ability));
        }
        if (screenLayout) {
            jObjectData.put("Screen_Layout", ApplicationInfoUtils.getScreenLayout(ability));
        }
        if (vmHeapSize) {
            jObjectData.put("VM_Heap_Size", getConvertSize(Runtime.getRuntime().totalMemory()));
        }
        if (allocatedVmSize) {
            jObjectData.put("Allocated_VM_Size", getConvertSize(Runtime.getRuntime().freeMemory()));
        }
        if (vmMaxHeapSize) {
            jObjectData.put("VM_Max_Heap_Size", getConvertSize((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())));
        }
        if (vmFreeHeapSize) {
            jObjectData.put("VM_free_Heap_Size", getConvertSize(Runtime.getRuntime().maxMemory()));
        }
        if (nativeAllocatedSize) {
            jObjectData.put("Native_Allocated_Size", getHeapSize());
        }
    }

    private void setBatteryValues(ZSONObject jObjectData) {
        if (batteryPercentage) {
            jObjectData.put("Battery_Percentage", BatteryUtils.getBatteryChargeLevel());
        }
        if (batteryCharging) {
            jObjectData.put("Battery_Charging_Status", BatteryUtils.getBatteryStatus());
        }
        if (batteryChargingVia) {
            jObjectData.put("Battery_Charging_Via", BatteryUtils.getBatteryChargingMode());
        }
    }

    private void setInternalExternalMemoryValues(ZSONObject jObjectData) {
        if (sdCardStatus) {
            jObjectData.put("SDCard_Status", getSDCardStatus());
        }
        if (internalMemorySize) {
            jObjectData.put("Internal_Memory_Size", getTotalInternalMemorySize(ability));
        }
        if (externalMemorySize) {
            jObjectData.put("External_Memory_Size", getTotalExternalMemorySize(ability));
        }
        if (internalFreeSpace) {
            jObjectData.put("Internal_Free_Space", getAvailableInternalMemorySize(ability));
        }
        if (externalFreeSpace) {
            jObjectData.put("External_Free_Space", getAvailableExternalMemorySize(ability));
        }
        if (networkMode) {
            jObjectData.put("Network_Mode", NetworkUtils.getNetworkMode(ability));
        }
    }

    private void setMemoryValues(ZSONObject jObjectData) {
        if (vmHeapSize) {
            jObjectData.put("VM_Heap_Size", getConvertSize(Runtime.getRuntime().totalMemory()));
        }
        if (country) {
            jObjectData.put("Country", new Locale("", ability.getResourceManager().getConfiguration().getFirstLocale().getCountry()).getDisplayCountry());
            if (allocatedVmSize) {
                jObjectData.put("Allocated_VM_Size", getConvertSize(Runtime.getRuntime().freeMemory()));
            }
            if (vmMaxHeapSize) {
                jObjectData.put("VM_Max_Heap_Size", getConvertSize((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())));
            }
            if (vmFreeHeapSize) {
                jObjectData.put("VM_free_Heap_Size", getConvertSize(Runtime.getRuntime().maxMemory()));
            }
            if (nativeAllocatedSize) {
                jObjectData.put("Native_Allocated_Size", getHeapSize());
            }
        }
    }
}