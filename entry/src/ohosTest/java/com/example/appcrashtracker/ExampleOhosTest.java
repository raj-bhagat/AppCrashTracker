package com.example.appcrashtracker;

import com.org.appcrashtracker.data.ExceptionData;
import com.org.appcrashtracker.utils.ApplicationInfoUtils;
import com.org.appcrashtracker.utils.BatteryUtils;
import com.org.appcrashtracker.utils.MemoryUtils;
import com.org.appcrashtracker.utils.NetworkUtils;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.delegation.AbilityDelegatorRegistry;
import ohos.app.Context;
import org.junit.Test;

import static org.junit.Assert.*;

public class ExampleOhosTest {
    private final Context context = AbilityDelegatorRegistry.getAbilityDelegator().getAppContext();
    private final Ability ability = AbilityDelegatorRegistry.getAbilityDelegator().getCurrentTopAbility();

    @Test
    public void testBundleName() {
        final String actualBundleName = AbilityDelegatorRegistry.getArguments().getTestBundleName();
        assertEquals("com.example.appcrashtracker", actualBundleName);
    }

    @Test
    public void testIsTablet(){
        final boolean testTablet = ApplicationInfoUtils.isTablet(context);
        assertTrue(testTablet);
    }

    @Test
    public void testAppVersion(){
        final String appVersion = ApplicationInfoUtils.getAppVersion(context);
        assertEquals("1.0.0",appVersion);
    }

    @Test
    public void testScreenOrientation(){
        final String screenOrientation = ApplicationInfoUtils.getScreenOrientation(ability);
        assertEquals("Not Defined",screenOrientation);
    }

    @Test
    public void testScreenLayout(){
        final String screenLayout = ApplicationInfoUtils.getScreenLayout(ability);
        assertEquals("Extra Large Screen",screenLayout);
    }

    @Test
    public void testBatteryStatus(){
        final String batteryStatus = BatteryUtils.getBatteryStatus();
        assertEquals("Battery Full",batteryStatus);
    }

    @Test
    public void testBatteryChargingMode(){
        final String batteryChargingMode = BatteryUtils.getBatteryChargingMode();
        assertEquals("USB",batteryChargingMode);
    }

    @Test
    public void testBatteryChargeLevel(){
        final String batteryChargeLevel = BatteryUtils.getBatteryChargeLevel();
        assertEquals("HIGH", batteryChargeLevel);
    }

    @Test
    public void testSDCardStatus(){
        final String sDCardStatus = MemoryUtils.getSDCardStatus();
        assertEquals("External Storage Not Supported",sDCardStatus);
    }

    @Test
    public void testAvailableInternalMemorySize(){
        final String availableInternalMemorySize = MemoryUtils.getAvailableInternalMemorySize(context);
        assertEquals("6.3 GB",availableInternalMemorySize);
    }

    @Test
    public void testTotalInternalMemorySize(){
        final String totalInternalMemorySize = MemoryUtils.getTotalInternalMemorySize(context);
        assertEquals("7.9 GB",totalInternalMemorySize);
    }

    @Test
    public void testAvailableExternalMemorySize(){
        final String availableExternalMemorySize = MemoryUtils.getAvailableExternalMemorySize(context);
        assertEquals("6.1 GB",availableExternalMemorySize);
    }

    @Test
    public void testTotalExternalMemorySize(){
        final String totalExternalMemorySize = MemoryUtils.getTotalExternalMemorySize(context);
        assertEquals("7.9 GB",totalExternalMemorySize);
    }

    @Test
    public void testHeapSize(){
        final String heapSize =MemoryUtils.getHeapSize();
        assertEquals("26 MB",heapSize);
    }

    @Test
    public void testNetworkMode(){
        final String networkMode = NetworkUtils.getNetworkMode(context);
        assertEquals("Wifi",networkMode);
    }

    /** Since we cannot throw exception and populate the json object.
     * So we are testing for the Json Object to be null
     */
    @Test
    public void testGetJsonObject() {
        ExceptionData data =new ExceptionData(ability);
        assertNull(data.getJsonObjectData());
    }
}