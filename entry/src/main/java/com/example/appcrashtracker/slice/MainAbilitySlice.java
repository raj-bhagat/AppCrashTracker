package com.example.appcrashtracker.slice;

import com.example.appcrashtracker.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.bundle.IBundleManager;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class MainAbilitySlice extends AbilitySlice {
    private static final String CONST = "Exception";
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        Button bt =(Button)findComponentById(ResourceTable.Id_crashButton);

        bt.setClickedListener(component -> crashme());
    }

    private void crashme() {
        HiLog.debug(new HiLogLabel(HiLog.LOG_APP,
                        0x00201,
                        "" + CONST),
                Integer.parseInt("asdf") + "");
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}
