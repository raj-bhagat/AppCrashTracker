package com.example.appcrashtracker;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import com.example.appcrashtracker.slice.MainAbility2Slice;

/** This main abilitly2 created for checking the library. */
public class MainAbility2 extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(MainAbility2Slice.class.getName());
    }
}
