package com.ubfx.library.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;
import android.support.v4.content.FileProvider;


import com.ubfx.log.LogUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by chuanzheyang on 17/3/10.
 */

public class FileUtils {
    private static final String DIR_EXTERNAL = Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED) ? Environment.getExternalStorageDirectory().getAbsolutePath() : "/mnt/sdcard";
    private static String SAVE_PATH_PIC = DIR_EXTERNAL + "/Ubankfx/pic";

    public static void initExternalCachePath(@NonNull File dir) {
        SAVE_PATH_PIC = new File(dir, "pic").getPath();
        checkDir();
    }

    private static void checkDir() {
        File folder = new File(SAVE_PATH_PIC);
        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                LogUtils.i(SAVE_PATH_PIC + " mkdirs fail!");
            }
        }
    }

    public static File saveFile(Bitmap bm, String fileName) throws IOException {
        checkDir();
        File myCaptureFile = new File(SAVE_PATH_PIC, fileName);
        if (!myCaptureFile.exists()) {
            myCaptureFile.createNewFile();
        }
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        bos.flush();
        bos.close();
        bm.recycle();

        return myCaptureFile;
    }

    public static String loadJSONFromAsset(Context context, String file) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(file);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static String loadJSONFromRaw(Context context, @RawRes int resId) {
        String json = null;
        try {
            InputStream is = context.getResources().openRawResource(resId);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    /**
     * used for inner
     * @param activity
     * @return
     */
    @Nullable
    public static File getCaptureFile(@NonNull Activity activity) {
        if (!PermissionUtils.checkWriteExternalStorage(activity)) {
            return null;
        }
        checkDir();
        File myCaptureFile = new File(SAVE_PATH_PIC, System.currentTimeMillis() + ".jpg");
        if (!myCaptureFile.exists()) {
            try {
                myCaptureFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return myCaptureFile;
    }

    @Nullable
    public static Uri getCaptureUri(@NonNull Activity activity) {
        File file = getCaptureFile(activity);
        if(file != null){
            return getCaptureUriFromFile(activity, file);
        }
        return null;
    }

    @Nullable
    public static Uri getCaptureUriFromFile(@NonNull Activity activity, File file) {
        if(file == null){
            return null;
        }
        String FILE_AUTHORITY = activity.getApplicationInfo().packageName + ".fileProvider";
        return FileProvider.getUriForFile(activity, FILE_AUTHORITY, file);
    }

}
