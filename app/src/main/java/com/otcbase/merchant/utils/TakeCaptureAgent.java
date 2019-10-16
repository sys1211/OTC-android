package com.otcbase.merchant.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by yangchuanzhe on 2019/4/2.
 */
public class TakeCaptureAgent {

    public interface OnTakeCaptureResultListener {
        void onTakeCaptureResult(int resultCode, @NonNull Intent data);

        /**
         * capture cancel or other reason
         *
         * @param resultCode
         */
        void onTakeCaptureError(int resultCode);
    }

    private static final int REQUEST_TAKE_CAPTURE = 0x02;
    private WeakReference<Activity> weakActivity;
    private OnTakeCaptureResultListener mListener;

    public TakeCaptureAgent(Activity activity, OnTakeCaptureResultListener listener) {
        this.weakActivity = new WeakReference<>(activity);
        this.mListener = listener;
    }


    public boolean show(Uri uri) {
        if (weakActivity == null || weakActivity.get() == null) {
            return false;
        }
        Activity activity = weakActivity.get();
        if (!PermissionUtils.checkCamera(activity)) {
            return false;
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            List<ResolveInfo> resInfoList = activity.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                activity.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
        }
        weakActivity.get().startActivityForResult(intent, REQUEST_TAKE_CAPTURE);
        return true;
    }

    public boolean handleActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                if (mListener != null) {
                    mListener.onTakeCaptureResult(resultCode, data);
                }
            } else {
                if (mListener != null) {
                    mListener.onTakeCaptureError(resultCode);
                }
            }
            mListener = null;
        }

        return false;
    }
}
