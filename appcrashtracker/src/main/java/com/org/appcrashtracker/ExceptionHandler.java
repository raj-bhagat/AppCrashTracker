package com.org.appcrashtracker;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.app.Context;
import ohos.app.Environment;
import ohos.batterymanager.BatteryInfo;
import ohos.bundle.BundleInfo;
import ohos.bundle.IBundleManager;
import ohos.data.usage.MountState;
import ohos.data.usage.StatVfs;
import ohos.global.configuration.Configuration;
import ohos.global.configuration.DeviceCapability;
import ohos.global.resource.NotExistException;
import ohos.global.resource.ResourceManager;
import ohos.global.resource.WrongTypeException;
import ohos.hiviewdfx.Debug;
import ohos.hiviewdfx.HiChecker;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.net.NetCapabilities;
import ohos.net.NetManager;
import ohos.rpc.RemoteException;
import ohos.system.DeviceInfo;
import ohos.telephony.RadioInfoManager;
import ohos.telephony.SignalInformation;
import ohos.telephony.SimInfoManager;
import ohos.telephony.TelephonyConstants;
import ohos.utils.zson.ZSONException;
import ohos.utils.zson.ZSONObject;

import java.io.*;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

import static com.example.appcrashtracker.ResourceTable.*;


public class ExceptionHandler implements java.lang.Thread.UncaughtExceptionHandler {
	private final Ability ability;
	Intent intent;
	ZSONObject jObjectData;
	String abilityName;
	Class<?> name;
	String postUrl;
	private static final String UNKNOWN = "Unknown";


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
	private boolean batteryChargingVia= false;
	private boolean sdCardStatus= false;
	private boolean internalMemorySize=false;
	private boolean externalMemorySize= false;
	private boolean internalFreeSpace= false;
	private boolean externalFreeSpace= false;
	private boolean packageName= false;
	private boolean networkMode= false;
	private boolean country= false;
	HiLogLabel label = new HiLogLabel(HiLog.LOG_APP, 0x00201 , getClass().getName());

	public ExceptionHandler(Ability ability,Class<?> name)throws NotExistException, WrongTypeException, IOException {
		this.ability = ability;
		this.name=name;
		abilityName=ability.getClass().getSimpleName();
        ResourceManager resources = ability.getResourceManager();
		this.postUrl=ability.getString(String_url);
		if(postUrl.equals("default_url"))
			HiLog.error(label, "Post url not specified");
		else
		{
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
			deviceOrientation=resources.getElement(Boolean_device_orientation).getBoolean();
			screenLayout=resources.getElement(Boolean_screen_layout).getBoolean();
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

	public void uncaughtException(Thread thread, Throwable exception)  {
		StringWriter stackTrace = new StringWriter();
		exception.printStackTrace(new PrintWriter(stackTrace));

		HiChecker.removeAllRules();
		jObjectData = new ZSONObject();
		try {
				populateJSONObject(jObjectData, exception, stackTrace);
		} catch (ZSONException e) {
			HiLog.error(label, "JSON Exception");
		}
		HiLog.info(new HiLogLabel(HiLog.LOG_APP, 0x00201 ,""), ">>>>>>>>>>>>>>>>>>"+getNetworkOperatorName(ability));
		writeToDocuments(ability, jObjectData);

		intent = new Intent();
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
		try( FileOutputStream fileOutputStream = new FileOutputStream(file)) {
		    fileOutputStream.write(data.toString().getBytes());
		} catch (Exception e) {
			HiLog.error(label, "Text file Error ");
		}
	}


	private void populateJSONObject(ZSONObject jObjectData, Throwable exception,StringWriter stackTrace) {
		BundleInfo bi = new BundleInfo();
		if(packageName) {
			jObjectData.put("Package_Name", ability.getBundleName());
		}
		if(className) {
			jObjectData.put("Class", abilityName);
		}
		if(message) {
			jObjectData.put("Message", exception.getMessage());
		}
		if(localizedMessage) {
			jObjectData.put("Localized_Message", exception.getLocalizedMessage());
		}
		if(causes) {
			jObjectData.put("Cause", exception.getCause());
		}
		if(stackTraceBoolean) {
			jObjectData.put("Stack_Trace", stackTrace.toString());
		}
		if(brandName) {
			jObjectData.put("Brand", "Huawei");
		}
		if(deviceName) {
			jObjectData.put("Device", DeviceInfo.getName());
		}
		if(sdkVersion) {
			jObjectData.put("SDK", bi.getMaxSdkVersion()+"");
		}
		if(release) {
			jObjectData.put("Release", bi.getVersionName());
		}
		if(height) {
			jObjectData.put("Height", ability.getResourceManager().getDeviceCapability().height+"");
		}
		if(width) {
			jObjectData.put("Width", ability.getResourceManager().getDeviceCapability().width);
		}
		if(appVersion) {
			jObjectData.put("App_Version", getAppVersion(ability));
		}
		if(tablet) {
			jObjectData.put("Tablet", " "+isTablet(ability));
		}
		if(deviceOrientation) {
			jObjectData.put("Device_Orientation", getScreenOrientation(ability));
		}
		if(screenLayout) {
			jObjectData.put("Screen_Layout", getScreenLayout(ability));
		}
		if(vmHeapSize) {
			jObjectData.put("VM_Heap_Size", getConvertSize(Runtime.getRuntime().totalMemory()));
		}
		if(allocatedVmSize) {
			jObjectData.put("Allocated_VM_Size", getConvertSize(Runtime.getRuntime().freeMemory()));
		}
		if(vmMaxHeapSize) {
			jObjectData.put("VM_Max_Heap_Size", getConvertSize((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())));
		}
		if(vmFreeHeapSize) {
			jObjectData.put("VM_free_Heap_Size", getConvertSize(Runtime.getRuntime().maxMemory()));
		}
		if(nativeAllocatedSize) {
			jObjectData.put("Native_Allocated_Size", getHeapSize());
		}
		if(batteryPercentage) {
			jObjectData.put("Battery_Percentage", getBatteryChargeLevel());
		}
		if(batteryCharging) {
			jObjectData.put("Battery_Charging_Status", getBatteryStatus());
		}
		if(batteryChargingVia) {
			jObjectData.put("Battery_Charging_Via", getBatteryChargingMode());
		}
		if(sdCardStatus) {
			jObjectData.put("SDCard_Status", getSDCardStatus());
		}
		if(internalMemorySize) {
			jObjectData.put("Internal_Memory_Size", getTotalInternalMemorySize(ability));
		}
		if(externalMemorySize) {
			jObjectData.put("External_Memory_Size", getTotalExternalMemorySize(ability));
		}
		if(internalFreeSpace) {
			jObjectData.put("Internal_Free_Space", getAvailableInternalMemorySize(ability));
		}
		if(externalFreeSpace) {
			jObjectData.put("External_Free_Space", getAvailableExternalMemorySize(ability));
		}
		if(networkMode) {
			jObjectData.put("Network_Mode", getNetworkMode(ability));
		}
		if(country) {
			jObjectData.put("Country", new Locale("", ability.getResourceManager().getConfiguration().getFirstLocale().getCountry()).getDisplayCountry());
		}
	}

    private String getNetworkOperatorName(Context con) {
		String operator = "";
		SimInfoManager sm = SimInfoManager.getInstance(con);
		int maxSim = sm.getMaxSimCount();
		for(int i=0; i<maxSim; i++) {
			if (sm.hasSimCard(i)) {
				RadioInfoManager radioInfoManager = RadioInfoManager.getInstance(con);
				operator+= " "+"Sim "+(i+1)+": "+radioInfoManager.getOperatorName(i);
			}
		}
		return operator;
	}

	private String getBatteryChargeLevel() {
		BatteryInfo bi = new BatteryInfo();
		BatteryInfo.BatteryLevel level = bi.getBatteryLevel();
		switch (level){
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

	private String getHeapSize() {
		Debug.HeapInfo heapInfo = new Debug.HeapInfo();
		Debug.getNativeHeapInfo(heapInfo);
		long heapSize = heapInfo.nativeHeapSize;
		return getConvertSize(heapSize);
	}

	public String getAppVersion(Context con)
	{
		IBundleManager manager = con.getBundleManager();
		BundleInfo info = null;
		try {
			info = manager.getBundleInfo(con.getBundleName(), 0);
		} catch ( RemoteException e) {
			HiLog.error(label, "Name not found Exception");
			return "Version not found";
		}
		return info.getVersionName();
	}

	public boolean isTablet(Context con) {
		int booltablet = con.getResourceManager().getDeviceCapability().deviceType;
		return booltablet == DeviceCapability.DEVICE_TYPE_TABLET;

	}


	public String getScreenOrientation(Ability abl)
	{

		int screenOrientation =  abl.getDisplayOrientation();
		switch (screenOrientation){
			case (Configuration.DIRECTION_HORIZONTAL):
				return "LandScape";
			case (Configuration.DIRECTION_VERTICAL):
				return "Portrait";
			default:
				return "Not Defined";
		}

	}
	public String getScreenLayout(Ability abl)
	{
		int screenSize = abl.getResourceManager().getDeviceCapability().screenDensity;
		switch (screenSize) {
			case DeviceCapability.SCREEN_LDPI:
				return "Large Screen";
			case DeviceCapability.SCREEN_MDPI:
				return  "Medium Screen";
			case DeviceCapability.SCREEN_SDPI:
				return  "Small Screen";
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

	public String getConvertSize(long size) {
		if(size <= 0) return "0";
		final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
		int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
		return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}

	public String getBatteryStatus()
	{
		BatteryInfo bi = new BatteryInfo();
		BatteryInfo.BatteryChargeState status = bi.getChargingStatus();
		switch (status){
			case DISABLE:
				return "Not Charging";
			case ENABLE:
				return "Charging";
			case FULL:
				return "Battery Full";
			case RESERVED:
				return"Reserved";
			default:
				return UNKNOWN;
		}
	}

	public String getBatteryChargingMode()
	{
		BatteryInfo bi = new BatteryInfo();
		BatteryInfo.BatteryPluggedType type = bi.getPluggedType();
		switch (type){
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

	public String getSDCardStatus()
	{
		boolean storage = ohos.data.usage.DataUsage.isSupported();
		if (storage){
			return "External Storage Not Supported";
		}
		else{
			MountState isSDPresent = ohos.data.usage.DataUsage.getDiskMountedStatus();
			switch (isSDPresent) {
				case DISK_MOUNTED:
					return "Mounted";
				case DISK_REMOVED:
					return "Removed";
				case DISK_UNMOUNTED:
					return "Unmounted";
				default:
					return UNKNOWN;
			}
		}
	}

	public String getAvailableInternalMemorySize(Context con ) {
		File path = FilePath.getInternalStorage(con);
		StatVfs stat = new StatVfs(path.getPath());
		long availableBlocks = stat.getFreeSpace();
		return getConvertSize(availableBlocks);
	}

	public String getTotalInternalMemorySize(Context con) {
		File path = FilePath.getInternalStorage(con);
		StatVfs stat = new StatVfs(path.getPath());
		long totalBlocks = stat.getSpace();
		return getConvertSize(totalBlocks);
	}

	public String getAvailableExternalMemorySize(Context con) {
		if (ohos.data.usage.DataUsage.getDiskMountedStatus().equals(MountState.DISK_MOUNTED)) {
			File path = FilePath.getExternalStorage(con);
			StatVfs stat = new StatVfs(path.getPath());
			long availableBlocks = stat.getFreeSpace();
			return getConvertSize(availableBlocks);
		} else {
			return "SDCard not present";
		}
	}

	public String getTotalExternalMemorySize(Context con) {
		if (ohos.data.usage.DataUsage.getDiskMountedStatus().equals(MountState.DISK_MOUNTED)) {
			File path = FilePath.getExternalStorage(con);
			StatVfs stat = new StatVfs(path.getPath());
			long totalBlocks = stat.getSpace();
			return getConvertSize(totalBlocks);
		} else {
			return "SDCard not present";
		}
	}

	public String getNetworkMode(Context con) {
		NetManager netManager = NetManager.getInstance(con);
		NetCapabilities netCapabilities = netManager.getNetCapabilities(netManager.getDefaultNet());
		if (netCapabilities.hasCap(NetCapabilities.NET_CAPABILITY_VALIDATED) &&
				netCapabilities.hasBearer(NetCapabilities.BEARER_WIFI) ||
				netCapabilities.hasBearer(NetCapabilities.BEARER_WIFI_AWARE)) {
			return "Wifi";
		}
		else if (netCapabilities.hasCap(NetCapabilities.NET_CAPABILITY_VALIDATED) &&
				netCapabilities.hasBearer(NetCapabilities.BEARER_CELLULAR)) {
			return getRadioInfo(con);
		}
		else
		{
			return "No Network";
		}
	}
	public String getRadioInfo(Context con){
		SimInfoManager sm = SimInfoManager.getInstance(con);
		int maxSim = sm.getMaxSimCount();
		String simMode="";
		for(int i=0; i<maxSim; i++){
			if(sm.hasSimCard(i)){
				RadioInfoManager radioInfoManager = RadioInfoManager.getInstance(con);
				List<SignalInformation> signalList = radioInfoManager.getSignalInfoList(i);
				for(SignalInformation signal : signalList){
					int signalNetworkType = signal.getNetworkType();
					switch(signalNetworkType) {
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