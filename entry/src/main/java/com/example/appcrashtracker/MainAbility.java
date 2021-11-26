package com.example.appcrashtracker;

import com.example.appcrashtracker.slice.MainAbilitySlice;
import com.org.appcrashtracker.ACT;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.bundle.IBundleManager;
import ohos.global.resource.NotExistException;
import ohos.global.resource.WrongTypeException;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

import java.io.IOException;

public class MainAbility extends Ability {
    private static final String CONST = "Exception";
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        if (verifySelfPermission("ohos.permission.WRITE_USER_STORAGE") != IBundleManager.PERMISSION_GRANTED) {
            if (canRequestPermission("ohos.permission.WRITE_USER_STORAGE")) {
                requestPermissionsFromUser(
                        new String[]{"ohos.permission.WRITE_USER_STORAGE"}, 101);
            }
        }
        try {
            ACT.init(this, MainAbility2.class);
        } catch (NotExistException e) {
            HiLog.error(new HiLogLabel(HiLog.LOG_APP, 0x00201 , ""+CONST), "NotExistException");
        } catch (WrongTypeException e) {
            HiLog.error(new HiLogLabel(HiLog.LOG_APP, 0x00201 , ""+CONST), "WrongTypeException");
        } catch (IOException e) {
            HiLog.error(new HiLogLabel(HiLog.LOG_APP, 0x00201 , ""+CONST), "IOException");
        }
        super.setMainRoute(MainAbilitySlice.class.getName());
    }
}
