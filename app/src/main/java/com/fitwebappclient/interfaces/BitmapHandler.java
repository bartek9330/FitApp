package com.fitwebappclient.interfaces;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public interface BitmapHandler {
    default Bitmap getImageFromBase64(String base64){
        try {
            byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        }catch (Exception exception){
            System.out.println(exception.getMessage());
            return null;
        }
    }

    default String getBase64FromImage(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}
