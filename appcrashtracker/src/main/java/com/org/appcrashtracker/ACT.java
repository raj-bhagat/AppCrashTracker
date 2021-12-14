package com.org.appcrashtracker;

import ohos.aafwk.ability.Ability;
import ohos.app.Context;

public class ACT {

    private ACT() {
        throw new IllegalArgumentException("ACT Class");
    }

    public static void init(Context context) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler((Ability) context));
    }
}
