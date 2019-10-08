package com.dev.voltsoft.lib.view.video.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.core.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class PlayerUtil {

    private static float SECOND = 1000;
    private static float MINUTE = 60 * SECOND;
    private static float HOUR = 60 * MINUTE;

    public static void setBackgroundBitmapDrawable(ImageView imageView , Bitmap bitmap) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN){
            imageView.setBackground(new BitmapDrawable(imageView.getResources() , bitmap));
        } else {
            imageView.setBackgroundDrawable(new BitmapDrawable(imageView.getResources() , bitmap));
        }
    }

    public static void setBackGroundDrawable(View view , int resourceId) {
        if (view != null) {
            try {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN){
                    view.setBackground(ContextCompat.getDrawable(view.getContext(), resourceId));
                } else {
                    view.setBackgroundDrawable(ContextCompat.getDrawable(view.getContext(), resourceId));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void setBackGroundDrawable(View view , Drawable drawable) {
        if (view != null) {
            try {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN){
                    view.setBackground(drawable);
                } else {
                    view.setBackgroundDrawable(drawable);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String getTimeStampString(long time) {
        String timeStampFormat = "mm:ss";

        if (time > HOUR) {
            timeStampFormat = "h:mm:ss";
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeStampFormat, Locale.KOREA);
        return simpleDateFormat.format(time);
    }
}
