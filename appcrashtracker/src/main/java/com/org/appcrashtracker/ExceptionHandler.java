package com.org.appcrashtracker;

import com.org.appcrashtracker.data.ExceptionData;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.app.Context;
import ohos.app.Environment;
import ohos.global.resource.NotExistException;
import ohos.global.resource.WrongTypeException;
import ohos.hiviewdfx.HiChecker;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.utils.zson.ZSONException;
import ohos.utils.zson.ZSONObject;

import java.io.*;

public class ExceptionHandler implements java.lang.Thread.UncaughtExceptionHandler {
    private final Ability ability;
    private final HiLogLabel label = new HiLogLabel(HiLog.LOG_APP, 0x00201, getClass().getName());
    ExceptionData data = new ExceptionData();

    public ExceptionHandler(Ability ability) throws NotExistException, WrongTypeException, IOException {
        this.ability = ability;
        data.setBoolean(ability);
    }

    public void uncaughtException(Thread thread, Throwable exception) {
        StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));

        HiChecker.removeAllRules();
        try {
            data.populateJSONObject(exception, stackTrace, ability);
        } catch (ZSONException e) {
            HiLog.error(label, "JSON Exception");
        }

        /* To-Do: Add UploadToServer() method here */
        // check issue in github: https://github.com/applibgroup/AppCrashTracker/issues/6
        writeToDocuments(ability, data.getJsonObjectData());

        Intent intent = new Intent();
        Operation operation = new Intent.OperationBuilder()
                .withBundleName(ability.getBundleName())
                .withAbilityName(ability.getAbilityName())
                .build();
        intent.setOperation(operation);
        ability.startAbility(intent);
    }

    private void writeToDocuments(Context context, ZSONObject jsondata) {
        File storageFolder = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(storageFolder, "crash.txt");
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            fileOutputStream.write(jsondata.toString().getBytes());
        } catch (Exception e) {
            HiLog.error(label, "Text file Error ");
        }
    }
}