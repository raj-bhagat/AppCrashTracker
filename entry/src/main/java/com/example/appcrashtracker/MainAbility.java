package com.example.appcrashtracker;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.bundle.IBundleManager;
import ohos.global.resource.NotExistException;
import ohos.global.resource.WrongTypeException;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import com.example.appcrashtracker.slice.MainAbilitySlice;
import com.org.appcrashtracker.ACT;
import java.io.IOException;

/** This is where we ask permission and call ACT and the main ability for sample application. */
public class MainAbility extends Ability {
    private static final String CONST = "Exception";
    private String writePermission =  "ohos.permission.WRITE_USER_STORAGE";

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        if (verifySelfPermission(writePermission) != IBundleManager.PERMISSION_GRANTED
                && canRequestPermission(writePermission)) {
            requestPermissionsFromUser(new String[]{writePermission}, 101);
        }
        try {
            ACT.init(this);
        } catch (NotExistException e) {
            HiLog.error(new HiLogLabel(HiLog.LOG_APP, 0x00201, "" + CONST), "NotExistException");
        } catch (WrongTypeException e) {
            HiLog.error(new HiLogLabel(HiLog.LOG_APP, 0x00201, "" + CONST), "WrongTypeException");
        } catch (IOException e) {
            HiLog.error(new HiLogLabel(HiLog.LOG_APP, 0x00201, "" + CONST), "IOException");
        }
        super.setMainRoute(MainAbilitySlice.class.getName());
    }
}
