package com.ubfx.library.utils;

import android.Manifest;
import android.app.Activity;


import com.ubfx.library.R;

import pub.devrel.easypermissions.EasyPermissions;


/**
 * Created by chuanzheyang on 2017/8/22.
 */

public class PermissionUtils {
    public static final int REQUEST_CODE_CAMERA_PERMISSIONS = 1;
    public static final int REQUEST_CODE_EXTERNAL_STORAGE_PERMISSIONS = 2;

    public static boolean checkCamera(Activity activity) {
        String[] perms = {Manifest.permission.CAMERA};
        if (!EasyPermissions.hasPermissions(activity, perms)) {
            EasyPermissions.requestPermissions(activity, activity.getString(R.string.permission_request_rationale), REQUEST_CODE_CAMERA_PERMISSIONS, perms);
            return false;
        } else {
            return true;
        }
    }

    public static boolean checkWriteExternalStorage(Activity activity) {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(activity, perms)) {
            EasyPermissions.requestPermissions(activity, activity.getString(R.string.permission_request_rationale), REQUEST_CODE_EXTERNAL_STORAGE_PERMISSIONS, perms);
            return false;
        } else {
            return true;
        }
    }
}
