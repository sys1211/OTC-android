package com.ubfx.library.glide;

import android.graphics.Bitmap;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;

import java.security.MessageDigest;

/**
 * must with center crop !!!
 */
public class GlideCropRoundTransform extends BitmapTransformation {
    private static final String ID = "com.ubfx.library.glide.GlideCropRoundTransform";
    private static final byte[] ID_BYTES = ID.getBytes(CHARSET);

    private int radius = 0;

    /**
     * radius in px
     *
     * @param radius
     */
    public GlideCropRoundTransform(int radius) {
        super();
        this.radius = radius;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        Bitmap bitmap = TransformationUtils.centerCrop(pool, toTransform, outWidth, outHeight);
        return TransformationUtils.roundedCorners(pool, bitmap, radius);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof GlideCropRoundTransform;
    }

    @Override
    public int hashCode() {
        return ID.hashCode();
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
    }

} 