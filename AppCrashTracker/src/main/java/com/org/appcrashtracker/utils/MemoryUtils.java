/*
 * Copyright (C) 2020-21 Application Library Engineering Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.org.appcrashtracker.utils;

import com.org.appcrashtracker.FilePath;
import ohos.app.Context;
import ohos.data.usage.MountState;
import ohos.data.usage.StatVfs;
import ohos.hiviewdfx.Debug;

import java.io.File;
import java.text.DecimalFormat;

/**
 * Utility class for obtaining internal / external memory /sdcard related information
 * @author Kanak Sony
 * @Version 1.0.0
 */
public class MemoryUtils {
    private static final String UNKNOWN = "Unknown";

    private MemoryUtils() {
    }

    public static String getSDCardStatus() {
        boolean storage = ohos.data.usage.DataUsage.isSupported();
        if (storage) {
            return "External Storage Not Supported";
        } else {
            MountState isSDPresent = ohos.data.usage.DataUsage.getDiskMountedStatus();
            switch (isSDPresent) {
                case DISK_MOUNTED:
                    return "Mounted";
                case DISK_REMOVED:
                    return "Removed";
                case DISK_UNMOUNTED:
                    return "Unmounted";
                default:
                    return UNKNOWN;
            }
        }
    }

    public static String getAvailableInternalMemorySize(Context con) {
        File path = FilePath.getInternalStorage(con);
        StatVfs stat = new StatVfs(path.getPath());
        long availableBlocks = stat.getFreeSpace();
        return getConvertSize(availableBlocks);
    }

    public static String getTotalInternalMemorySize(Context con) {
        File path = FilePath.getInternalStorage(con);
        StatVfs stat = new StatVfs(path.getPath());
        long totalBlocks = stat.getSpace();
        return getConvertSize(totalBlocks);
    }

    public static String getAvailableExternalMemorySize(Context con) {
        if (ohos.data.usage.DataUsage.getDiskMountedStatus().equals(MountState.DISK_MOUNTED)) {
            File path = FilePath.getExternalStorage(con);
            StatVfs stat = new StatVfs(path.getPath());
            long availableBlocks = stat.getFreeSpace();
            return getConvertSize(availableBlocks);
        } else {
            return "SDCard not present";
        }
    }

    public static String getTotalExternalMemorySize(Context con) {
        if (ohos.data.usage.DataUsage.getDiskMountedStatus().equals(MountState.DISK_MOUNTED)) {
            File path = FilePath.getExternalStorage(con);
            StatVfs stat = new StatVfs(path.getPath());
            long totalBlocks = stat.getSpace();
            return getConvertSize(totalBlocks);
        } else {
            return "SDCard not present";
        }
    }

    public static String getConvertSize(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "kB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public static String getHeapSize() {
        Debug.HeapInfo heapInfo = new Debug.HeapInfo();
        Debug.getNativeHeapInfo(heapInfo);
        long heapSize = heapInfo.nativeHeapSize;
        return getConvertSize(heapSize);
    }
}
