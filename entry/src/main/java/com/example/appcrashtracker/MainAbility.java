package com.example.appcrashtracker;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.global.resource.NotExistException;
import ohos.global.resource.WrongTypeException;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import com.example.appcrashtracker.slice.MainAbilitySlice;
import com.org.appcrashtracker.ACT;
import java.io.IOException;

public class MainAbility extends Ability {
    private static final String constString = "Exception";
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        try {
            ACT.init(this, MainAbility2.class);
        } catch (NotExistException e) {
            HiLog.error(new HiLogLabel(HiLog.LOG_APP, 0x00201 , constString), "NotExistException");
        } catch (WrongTypeException e) {
            HiLog.error(new HiLogLabel(HiLog.LOG_APP, 0x00201 , constString), "WrongTypeException");
        } catch (IOException e) {
            HiLog.error(new HiLogLabel(HiLog.LOG_APP, 0x00201 , constString), "IOException");
        }
        super.setMainRoute(MainAbilitySlice.class.getName());
    }
}
