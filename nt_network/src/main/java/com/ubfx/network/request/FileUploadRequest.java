package com.ubfx.network.request;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.ubfx.log.LogUtils;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

;

/**
 * Created by chuanzheyang on 17/1/24.
 */

public class FileUploadRequest extends JsonObjRequest {
    private final static String PREFIX = "--";
    private final static String LINEND = "\r\n";
    private final static String BOUNDARY = "apiclient-" + System.currentTimeMillis();

    private Map<String, Object> params;

    public FileUploadRequest(String url, File file, UBSuccessListener<JSONObject> mListener, UBErrorListener listener) {
        super(Method.POST, url, mListener, listener);
        Map<String, Object> params = new HashMap<>();
        params.put("file", file);
        this.params = params;
    }

    public FileUploadRequest(String url, Map<String, Object> params, UBSuccessListener<JSONObject> mListener, UBErrorListener listener) {
        super(Method.POST, url, mListener, listener);
        this.params = params;
    }

    @Override
    public String getBodyContentType() {
        return "multipart/form-data;boundary=" + BOUNDARY;
    }


    @Override
    public byte[] getBody() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        try {
            // 发送文件数据
            if (params != null) {
                appendParams(dos);
            }
            // 请求结束标志
            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
            dos.write(end_data);
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void appendParams(DataOutputStream dos) throws IOException {
        StringBuilder sb1;
        Map<String, Object> params = this.params;
        if (params.size() > 0) {
            Set<String> keys = params.keySet();
            for (String key : keys) {
                sb1 = new StringBuilder();
                Object value = params.get(key);
                if (value instanceof File) {
                    appendFileBytes(dos, key, (File) value);
                } else {
                    sb1.append(PREFIX);
                    sb1.append(BOUNDARY);
                    sb1.append(LINEND);
                    sb1.append("Content-Disposition: form-data; name=\"").append(key).append("\"").append(LINEND);
                    sb1.append(LINEND);
                    sb1.append(value.toString());
                    sb1.append(LINEND);
                    dos.write(sb1.toString().getBytes());
                }
            }
        }
    }

    private void appendFileBytes(DataOutputStream dos, String key, File file) throws IOException {
        StringBuilder sb1 = new StringBuilder();
        LogUtils.d(" file size before : " + file.length());
        Bitmap bitmap = compress(file.getPath());
        ByteArrayOutputStream bitmapOs = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bitmapOs);

        byte[] data = bitmapOs.toByteArray();

        LogUtils.d(" file length : " + data.length);

        sb1.append(PREFIX);
        sb1.append(BOUNDARY);
        sb1.append(LINEND);
        sb1.append("Content-Disposition: form-data; name=\"").append(key).append("\";").append("filename=\"").append(file.getName()).append(".jpg").append("\"").append(LINEND);
//        sb1.append("Content-Type: ").append(contentType).append(";").append(LINEND);
        sb1.append(LINEND);
        dos.write(sb1.toString().getBytes());
        dos.write(data);
        dos.write(LINEND.getBytes());
    }


    private Bitmap compress(String srcPath) {

        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(srcPath, newOpts);

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;

        float hh = 800f;
        float ww = 800f;
        int be = 1;
        if (w > h && w > ww) {
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例

        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

        return bitmap;
    }

}
