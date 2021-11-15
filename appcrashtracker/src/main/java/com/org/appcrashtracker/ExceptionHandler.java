package com.org.appcrashtracker;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.app.Context;
import ohos.batterymanager.BatteryInfo;
import ohos.bundle.BundleInfo;
import ohos.bundle.IBundleManager;
import ohos.data.usage.MountState;
import ohos.data.usage.StatVfs;
import ohos.global.configuration.Configuration;
import ohos.global.configuration.DeviceCapability;
import ohos.global.resource.NotExistException;
import ohos.global.resource.WrongTypeException;
import ohos.hiviewdfx.Debug;
import ohos.hiviewdfx.HiChecker;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.net.NetCapabilities;
import ohos.net.NetManager;
import ohos.os.ProcessManager;
import ohos.rpc.RemoteException;
import ohos.security.SystemPermission;
import ohos.system.DeviceInfo;
import ohos.telephony.RadioInfoManager;
import ohos.telephony.SignalInformation;
import ohos.telephony.SimInfoManager;
import ohos.telephony.TelephonyConstants;
import ohos.utils.zson.ZSONException;
import ohos.utils.zson.ZSONObject;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.*;

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
import static com.example.appcrashtracker.ResourceTable.String_url;



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
	HiLogLabel LABEL = new HiLogLabel(HiLog.LOG_APP, 0x00201 , getClass().getName());

	public ExceptionHandler(Ability ability,Class<?> name)throws NotExistException, WrongTypeException, IOException {
		this.ability = ability;
		this.name=name;
		abilityName=ability.getClass().getSimpleName();
		HiLog.debug(new HiLogLabel(HiLog.LOG_APP, 0x00201 ,""+ability.getBundleName()), "Entered method");

		this.postUrl=ability.getString(String_url);
		if(postUrl.equals("default_url"))
			HiLog.error(LABEL, "Post url not specified");
		else
		{
			className = ability.getResourceManager().getElement(Boolean_class_name).getBoolean();
			message = ability.getResourceManager().getElement(Boolean_message).getBoolean();
			localizedMessage = ability.getResourceManager().getElement(Boolean_localized_message).getBoolean();
			causes = ability.getResourceManager().getElement(Boolean_causes).getBoolean();
			stackTraceBoolean = ability.getResourceManager().getElement(Boolean_stack_trace).getBoolean();
			brandName = ability.getResourceManager().getElement(Boolean_brand_name).getBoolean();
			deviceName = ability.getResourceManager().getElement(Boolean_device_name).getBoolean();
			sdkVersion = ability.getResourceManager().getElement(Boolean_sdk_version).getBoolean();
			release = ability.getResourceManager().getElement(Boolean_release).getBoolean();
			height = ability.getResourceManager().getElement(Boolean_height).getBoolean();
			width = ability.getResourceManager().getElement(Boolean_width).getBoolean();
			appVersion = ability.getResourceManager().getElement(Boolean_app_version).getBoolean();
			tablet = ability.getResourceManager().getElement(Boolean_tablet).getBoolean();
			deviceOrientation=ability.getResourceManager().getElement(Boolean_device_orientation).getBoolean();
			screenLayout=ability.getResourceManager().getElement(Boolean_screen_layout).getBoolean();
			vmHeapSize = ability.getResourceManager().getElement(Boolean_vm_heap_size).getBoolean();
			allocatedVmSize = ability.getResourceManager().getElement(Boolean_allocated_vm_size).getBoolean();
			vmMaxHeapSize = ability.getResourceManager().getElement(Boolean_vm_max_heap_size).getBoolean();
			vmFreeHeapSize = ability.getResourceManager().getElement(Boolean_vm_free_heap_size).getBoolean();
			nativeAllocatedSize = ability.getResourceManager().getElement(Boolean_native_allocated_size).getBoolean();
			batteryPercentage = ability.getResourceManager().getElement(Boolean_battery_percentage).getBoolean();
			batteryCharging = ability.getResourceManager().getElement(Boolean_battery_charging).getBoolean();
			batteryChargingVia = ability.getResourceManager().getElement(Boolean_battery_charging_via).getBoolean();
			sdCardStatus = ability.getResourceManager().getElement(Boolean_sd_card_status).getBoolean();
			internalMemorySize = ability.getResourceManager().getElement(Boolean_internal_memory_size).getBoolean();
			externalMemorySize = ability.getResourceManager().getElement(Boolean_external_memory_size).getBoolean();
			internalFreeSpace = ability.getResourceManager().getElement(Boolean_internal_free_space).getBoolean();
			externalFreeSpace = ability.getResourceManager().getElement(Boolean_external_free_space).getBoolean();
			packageName = ability.getResourceManager().getElement(Boolean_package_name).getBoolean();
			networkMode = ability.getResourceManager().getElement(Boolean_network_mode).getBoolean();
			country = ability.getResourceManager().getElement(Boolean_country).getBoolean();
			HiLog.debug(new HiLogLabel(HiLog.LOG_APP, 0x00201 ,""+ability.getBundleName()), "packagename"+packageName);
		}
	}
	@SuppressWarnings("deprecation")
	public void uncaughtException(Thread thread, Throwable exception)  {
		StringWriter stackTrace = new StringWriter();
		exception.printStackTrace(new PrintWriter(stackTrace));

		HiChecker.removeAllRules();
		HiLog.debug(LABEL, "Inside Exception "+packageName);
		jObjectData = new ZSONObject();
		try {
				populateJSONObject(jObjectData, exception);//, stackTrace);
			HiLog.debug(LABEL, "Exit after writing json files");
		} catch (ZSONException e) {
			HiLog.error(new HiLogLabel(HiLog.LOG_APP, 0x00201 ,""+ability.getBundleName()), "JSON Exception");
		}
		HiLog.info(new HiLogLabel(HiLog.LOG_APP, 0x00201 ,""), ">>>>>>>>>>>>>>>>>>"+getNetworkOperatorName(ability));
		HiLog.debug(LABEL, "Entering upload to net");

		uploadToNet();

		HiLog.debug(LABEL, "Exiting upload to net");

		intent = new Intent();
		Operation operation = new Intent.OperationBuilder()
				.withBundleName(ability.getBundleName())
				.withAbilityName(ability.getAbilityName())
				.build();
		intent.setOperation(operation);
		ability.startAbility(intent);
		//ProcessManager.kill(ProcessManager.getPid());
		//System.exit(10);

	}

	private void populateJSONObject(ZSONObject jObjectData, Throwable exception){// stackTrace) {
		BundleInfo bi = new BundleInfo();
		HiLog.debug(LABEL, "Inside Exception "+packageName +" # populateJSONObject");
		if(packageName) {
			jObjectData.put("Package_Name", ability.getBundleName());
		}
		if(className) {
			jObjectData.put("Class", abilityName);
		}
		if(message) {
			jObjectData.put("Message", exception.getMessage());
			HiLog.debug(LABEL,"Message: "+exception.getMessage() );
		}
		if(localizedMessage) {
			jObjectData.put("Localized_Message", exception.getLocalizedMessage());
            HiLog.debug(LABEL,"Localized: message"+exception.getLocalizedMessage());
		}
		if(causes) {
			jObjectData.put("Cause", exception.getCause());
            HiLog.debug(LABEL,"Causes: "+exception.getCause());
		}
		if(stackTraceBoolean) {
			//stackTrace.write("java.lang.NumberFormatException: For input string: \"asdf\"\n\tat java.lang.Integer.parseInt(Integer.java:615)\n\tat java.lang.Integer.parseInt(Integer.java:650)");
			jObjectData.put("Stack_Trace", "java.lang.NumberFormatException");//stackTrace.toString());
            HiLog.debug(LABEL,"Stacktrace: "+"java.lang.NumberFormatException");//stackTrace.toString());
		}
		if(brandName) {
			jObjectData.put("Brand", "Huawei");
		}
		if(deviceName) {
			jObjectData.put("Device", DeviceInfo.getName());
            HiLog.debug(LABEL,"Device Name: "+DeviceInfo.getName());
		}
		if(sdkVersion) {
			jObjectData.put("SDK", bi.getMaxSdkVersion()+"");
            HiLog.debug(LABEL,"SDK version: "+bi.getMaxSdkVersion()+"");
		}
		if(release) {
			jObjectData.put("Release", bi.getVersionName());
            HiLog.debug(LABEL,"Release: "+bi.getVersionName());
		}
		if(height) {
			jObjectData.put("Height", ability.getResourceManager().getDeviceCapability().height+"");
            HiLog.debug(LABEL,"device height: "+ability.getResourceManager().getDeviceCapability().height+"");
		}
		if(width) {
			jObjectData.put("Width", ability.getResourceManager().getDeviceCapability().width);
            HiLog.debug(LABEL,"Device width: "+ability.getResourceManager().getDeviceCapability().width);
		}
		if(appVersion) {
			jObjectData.put("App_Version", getAppVersion(ability));
            HiLog.debug(LABEL,"App version: "+getAppVersion(ability));
		}
		if(tablet) {
			jObjectData.put("Tablet","true");//, isTablet(ability));
            HiLog.debug(LABEL,"istablet: true");//+isTablet(ability));
		}
		if(deviceOrientation) {
			jObjectData.put("Device_Orientation", getScreenOrientation(ability));
            HiLog.debug(LABEL,"orientation: "+getScreenOrientation(ability));
		}
		if(screenLayout) {
			jObjectData.put("Screen_Layout", getScreenLayout(ability));
            HiLog.debug(LABEL,"Screen lauout: "+getScreenLayout(ability));
		}
		if(vmHeapSize) {
			jObjectData.put("VM_Heap_Size", getConvertSize(Runtime.getRuntime().totalMemory()));
            HiLog.debug(LABEL,"VM size: "+getConvertSize(Runtime.getRuntime().totalMemory()));
		}
		if(allocatedVmSize) {
			jObjectData.put("Allocated_VM_Size", getConvertSize(Runtime.getRuntime().freeMemory()));
            HiLog.debug(LABEL,"Allocated VM size: "+getConvertSize(Runtime.getRuntime().freeMemory()));
		}
		if(vmMaxHeapSize) {
			jObjectData.put("VM_Max_Heap_Size", getConvertSize((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())));
            HiLog.debug(LABEL,"Max VM size: "+getConvertSize((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())));
		}
		if(vmFreeHeapSize) {
			jObjectData.put("VM_free_Heap_Size", getConvertSize(Runtime.getRuntime().maxMemory()));
            HiLog.debug(LABEL,"Free VM heap size: "+getConvertSize(Runtime.getRuntime().maxMemory()));
		}
		if(nativeAllocatedSize) {
			jObjectData.put("Native_Allocated_Size", getHeapSize());
            HiLog.debug(LABEL,"Heapsize: "+getHeapSize());
		}
		if(batteryPercentage) {
			jObjectData.put("Battery_Percentage", getBatteryChargeLevel());
            HiLog.debug(LABEL,"Battery percentage: "+getBatteryChargeLevel());
		}
		if(batteryCharging) {
			jObjectData.put("Battery_Charging_Status", getBatteryStatus());
            HiLog.debug(LABEL,"Battery status: "+getBatteryStatus());
		}
		if(batteryChargingVia) {
			jObjectData.put("Battery_Charging_Via", getBatteryChargingMode());
            HiLog.debug(LABEL,"Charging mode: "+getBatteryChargingMode());
		}
		if(sdCardStatus) {
			jObjectData.put("SDCard_Status", getSDCardStatus());
            HiLog.debug(LABEL,"SD card status: "+getSDCardStatus());
		}
		if(internalMemorySize) {
			jObjectData.put("Internal_Memory_Size", getTotalInternalMemorySize(ability));
            HiLog.debug(LABEL,"Total internal memory: "+getTotalInternalMemorySize(ability));
		}
		if(externalMemorySize) {
			jObjectData.put("External_Memory_Size", getTotalExternalMemorySize(ability));
            HiLog.debug(LABEL,"Total External Memory: "+getTotalExternalMemorySize(ability));
		}
		if(internalFreeSpace) {
			jObjectData.put("Internal_Free_Space", getAvailableInternalMemorySize(ability));
            HiLog.debug(LABEL,"Free Internal Memory: "+getAvailableInternalMemorySize(ability));
		}
		if(externalFreeSpace) {
			jObjectData.put("External_Free_Space", getAvailableExternalMemorySize(ability));
            HiLog.debug(LABEL,"Free External Memory: "+getAvailableExternalMemorySize(ability));
		}
		if(networkMode) {
			jObjectData.put("Network_Mode", getNetworkMode(ability));
            HiLog.debug(LABEL,"NetworkMode: "+getNetworkMode(ability));
		}
		if(country) {
			jObjectData.put("Country", new Locale("", ability.getResourceManager().getConfiguration().getFirstLocale().getCountry()).getDisplayCountry());
            HiLog.debug(LABEL,""+new Locale("Country: ", ability.getResourceManager().getConfiguration().getFirstLocale().getCountry()).getDisplayCountry());
		}
		HiLog.debug(LABEL, "Inside Exception "+packageName +" # populateJSONObject # jObjectData = "+jObjectData.toString());
	}

	private void uploadToNet() {
		if(ability.getBundleManager().checkPermission( ability.getBundleName() , SystemPermission.INTERNET) == 0)
		{
			HiLog.debug(new HiLogLabel(HiLog.LOG_APP, 0x00201 ,"1 "+ability.getBundleName()), "Internet Permission");
			if((ability.getBundleManager().checkPermission(ability.getBundleName() , SystemPermission.GET_NETWORK_INFO) ==0 ))
			{
				HiLog.debug(new HiLogLabel(HiLog.LOG_APP, 0x00201 ,"1 "+ability.getBundleName()), "NetworkInfo Permission");
				if(isConnectingToInternet(ability))
				{
					HiLog.debug(new HiLogLabel(HiLog.LOG_APP, 0x00201 ,"1 "+ability.getBundleName()), "Is connected to Internet");
					if (className || message || localizedMessage || causes
							|| stackTraceBoolean || brandName || deviceName
							|| sdkVersion || release  || height || width
							|| appVersion || tablet || deviceOrientation
							|| screenLayout || vmHeapSize
							|| allocatedVmSize || vmMaxHeapSize
							|| vmFreeHeapSize || nativeAllocatedSize
							|| batteryPercentage || batteryCharging
							|| batteryChargingVia || sdCardStatus
							|| internalMemorySize || externalMemorySize
							|| internalFreeSpace || externalFreeSpace
							|| packageName || networkMode || country) {
						HiLog.debug(new HiLogLabel(HiLog.LOG_APP, 0x00201 ,""+ability.getBundleName()), "URL "+postUrl);
						new Timer().schedule(
                                new TimerTask() {
                                    @Override
                                    public void run() {
                                        ability.getUITaskDispatcher().asyncDispatch((() -> {
                                            URL url = null;
                                            try {
                                                url = new URL(postUrl);
                                                HiLog.debug(new HiLogLabel(HiLog.LOG_APP, 0x00201 ,""+ability.getBundleName()), "URL "+url);
                                            } catch (MalformedURLException e1) {
                                                HiLog.error(new HiLogLabel(HiLog.LOG_APP, 0x00201 ,""+ability.getBundleName()), "MalformedURLException");
                                            }
                                            HttpURLConnection conn = null;
                                            try {
                                                HiLog.debug(new HiLogLabel(HiLog.LOG_APP, 0x00201 , "urlname"), ""+postUrl);
                                                conn = (HttpURLConnection)url.openConnection();
												HiLog.debug(new HiLogLabel(HiLog.LOG_APP, 0x00201 , "Conn Debug"), "Connection: "+conn);

                                            } catch (IOException e1) {
                                                HiLog.error(new HiLogLabel(HiLog.LOG_APP, 0x00201 ,""+ability.getBundleName()), "IOException");
												HiLog.debug(new HiLogLabel(HiLog.LOG_APP, 0x00201 , "Conn Debug"), "Connection error from url");
                                            }
                                            try {
                                                conn.setRequestMethod("POST");

                                            } catch (ProtocolException e1) {
                                                HiLog.error(new HiLogLabel(HiLog.LOG_APP, 0x00201 ,""+ability.getBundleName()), "ProtocolException");
												HiLog.debug(new HiLogLabel(HiLog.LOG_APP, 0x00201 , "Post Debug"), "Post Error");
                                            }
                                            HiLog.debug(new HiLogLabel(HiLog.LOG_APP, 0x00201 ,""+ability.getBundleName()), "Sending request");
                                            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                                            conn.setDoInput(true);
                                            conn.setDoOutput(true);
                                            List<PostValuesPair> params1 = new ArrayList<>();
                                            params1.add(new PostValuesPair("error_report", jObjectData.toString()));
                                            try{
                                                HiLog.info(new HiLogLabel(HiLog.LOG_APP, 0x00202 ,"Debugger"), "checking ");
                                                OutputStream os = conn.getOutputStream();
                                                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
                                                writer.write(getQuery(params1));
                                                writer.flush();
                                                writer.close();
                                                os.close();
                                                HiLog.debug(new HiLogLabel(HiLog.LOG_APP, 0x00201 ,""+ability.getBundleName()), "writting files");
                                            }
                                            catch(Exception ee)
                                            {
                                                HiLog.error(new HiLogLabel(HiLog.LOG_APP, 0x00201 ,""+ability.getBundleName()), "Buffer Write Exception");
												HiLog.debug(new HiLogLabel(HiLog.LOG_APP, 0x00201 , "Buffer debug"), "Buffer writter error");
                                            }
                                            try {
                                                conn.connect();
                                            } catch (IOException e1) {
                                                HiLog.error(new HiLogLabel(HiLog.LOG_APP, 0x00201 ,""+ability.getBundleName()), "IOException");
												HiLog.debug(new HiLogLabel(HiLog.LOG_APP, 0x00201 , "Connect Debug"), "Could not Connect");
                                            }
                                        }));
                                    }
                                },10);
					}
					else
						HiLog.error(new HiLogLabel(HiLog.LOG_APP, 0x00201 ,""+ability.getBundleName()), "Not configured. Set configuration in string.json");
				}
				else
					HiLog.error(new HiLogLabel(HiLog.LOG_APP, 0x00201 ,""+ability.getBundleName()), "Network not found");
			}
			else
				HiLog.error(new HiLogLabel(HiLog.LOG_APP, 0x00201 ,""+ability.getBundleName()), "Need to add Access network state permission");
		}
		else
			HiLog.error(new HiLogLabel(HiLog.LOG_APP, 0x00201 ,""+ability.getBundleName()), "Need to add internet permission");
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
			HiLog.error(new HiLogLabel(HiLog.LOG_APP, 0x00201 ,""+ability.getBundleName()), "Name not found Exception");
			return "Version not found";
		}
		return info.getVersionName();
	}

	public boolean isTablet(Context con) {
		int booltablet = con.getResourceManager().getDeviceCapability().deviceType;
		return booltablet == DeviceCapability.DEVICE_TYPE_TABLET;

	}

	public String getQuery(List<PostValuesPair> params) throws UnsupportedEncodingException
	{
		StringBuilder result = new StringBuilder();
		boolean first = true;

		for (PostValuesPair pair : params)
		{
			if (first)
				first = false;
			else
				result.append("&");

			result.append(URLEncoder.encode(pair.getKey(), "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
		}

		return result.toString();
	}

	public boolean isConnectingToInternet(Context con){
		NetManager netManager = NetManager.getInstance(con);
		NetCapabilities netCapabilities = netManager.getNetCapabilities(netManager.getDefaultNet());
		return netCapabilities.hasCap(NetCapabilities.NET_CAPABILITY_VALIDATED);
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