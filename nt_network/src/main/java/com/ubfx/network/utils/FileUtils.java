package com.ubfx.network.utils;

import java.io.File;

/**
 * Created by yangchuanzhe on 2019/9/6.
 */
public class FileUtils {
    public static long getFileSizes(File f) {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSizes(flist[i]);
            } else {
                size = size + getFileSize(flist[i]);
            }
        }
        return size;
    }

    public static long getFileSize(File file) {
        return file.length();
    }
}
