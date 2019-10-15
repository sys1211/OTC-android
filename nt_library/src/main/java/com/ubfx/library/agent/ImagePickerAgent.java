package com.ubfx.library.agent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.ubfx.library.utils.PermissionUtils;

import java.lang.ref.WeakReference;

/**
 * Created by yangchuanzhe on 2019/4/2.
 */
public class ImagePickerAgent {

    private WeakReference<Activity> weakActivity;

    private static final int REQUEST_SELECT_PICTURE = 0x01;

    public interface OnImagePickerListener {
        void onPickImage(Uri imageUri, String imagePath);

        void onError();
    }

    private OnImagePickerListener mImagePickerListener;

    public ImagePickerAgent(Activity activity, OnImagePickerListener listener) {
        this.mImagePickerListener = listener;
        this.weakActivity = new WeakReference<>(activity);
    }

    public boolean show() {
        if (weakActivity == null || weakActivity.get() == null) {
            return false;
        }
        if (!PermissionUtils.checkWriteExternalStorage(weakActivity.get())) {
            return false;
        }
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        weakActivity.get().startActivityForResult(intent, REQUEST_SELECT_PICTURE);
        return true;
    }

    public boolean handleActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SELECT_PICTURE) {
            if (data == null) {
                if (mImagePickerListener != null) {
                    mImagePickerListener.onError();
                }
                mImagePickerListener = null;
                return true;
            }
            Uri uri = data.getData();
            if (uri == null) {
                if (mImagePickerListener != null) {
                    mImagePickerListener.onError();
                }
                mImagePickerListener = null;
                return true;
            }
            String path = "";
            if (!TextUtils.isEmpty(uri.getAuthority())) {
                Cursor cursor = activity.getContentResolver().query(uri,
                        new String[]{MediaStore.Images.Media.DATA},
                        null, null, null);
                if (null == cursor) {
                    if (mImagePickerListener != null) {
                        mImagePickerListener.onError();
                    }
                    mImagePickerListener = null;
                    return true;
                }
                cursor.moveToFirst();
                path = cursor.getString(cursor
                        .getColumnIndex(MediaStore.Images.Media.DATA));
                cursor.close();
            } else {
                path = data.getData().getPath();
            }
            if (TextUtils.isEmpty(path)) {
                if (mImagePickerListener != null) {
                    mImagePickerListener.onError();
                }
                mImagePickerListener = null;
                return true;
            }
            if (mImagePickerListener != null) {
                mImagePickerListener.onPickImage(uri, path);
            }
            mImagePickerListener = null;
            return true;
        }
        return false;
    }

}
