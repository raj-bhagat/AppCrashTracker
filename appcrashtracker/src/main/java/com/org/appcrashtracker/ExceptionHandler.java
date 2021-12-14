package com.org.appcrashtracker;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.app.Context;
import ohos.app.Environment;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.utils.zson.ZSONObject;
import com.org.appcrashtracker.data.ExceptionData;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * This is class to handle uncaught  exception.
 */
public class ExceptionHandler implements java.lang.Thread.UncaughtExceptionHandler {
    private final Ability ability;
    private final HiLogLabel label = new HiLogLabel(HiLog.LOG_APP, 0x00201, getClass().getName());
    private static final String FILE_NAME = "crash.txt";

    public ExceptionHandler(Ability ability) {
        this.ability = ability;
    }

    /** Method for handling uncaught exception.
     *
     * @param thread Thread created when uncaught exception is encountered.
     * @param exception exception details.
     */
    public void uncaughtException(Thread thread, Throwable exception) {
        StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));
        ExceptionData exceptionData = new ExceptionData(ability);

        /* To-Do: Add UploadToServer() method here */
        // check issue in github: https://github.com/applibgroup/AppCrashTracker/issues/6
        exceptionData.populateJsonObject(exception, stackTrace);
        writeToDocuments(ability, exceptionData.getJsonObjectData());

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
        File file = new File(storageFolder, FILE_NAME);
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            fileOutputStream.write(jsondata.toString().getBytes());
        } catch (Exception e) {
            HiLog.error(label, "Text file Error ");
        }
    }
}