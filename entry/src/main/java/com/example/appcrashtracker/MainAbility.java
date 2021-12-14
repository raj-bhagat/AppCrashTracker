package com.example.appcrashtracker;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.bundle.IBundleManager;
import com.example.appcrashtracker.slice.MainAbilitySlice;
import com.org.appcrashtracker.ACT;

/** This is where we ask permission and call ACT and the main ability for sample application. */
public class MainAbility extends Ability {
    private String writePermission =  "ohos.permission.WRITE_USER_STORAGE";

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        if (verifySelfPermission(writePermission) != IBundleManager.PERMISSION_GRANTED
                && canRequestPermission(writePermission)) {
            requestPermissionsFromUser(new String[]{writePermission}, 101);
        }
        ACT.init(this);

        super.setMainRoute(MainAbilitySlice.class.getName());
    }
}
