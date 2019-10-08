package com.dev.voltsoft.lib.view;

import android.content.Context;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import com.dev.voltsoft.lib.utility.UtilityUI;

public class ImageLoaderView extends androidx.appcompat.widget.AppCompatImageView {

    public ImageLoaderView(Context context) {
        super(context);
    }

    public ImageLoaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageLoaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void loadImage(String imageUrl)
    {
        if (!TextUtils.isEmpty(imageUrl))
        {
            UtilityUI.setThumbNailImageView(getContext(), this, imageUrl);
        }
    }

    public void loadTopRoundedImage(String imageUrl)
    {
        if (!TextUtils.isEmpty(imageUrl))
        {
            UtilityUI.setThumbNailTopRoundedImageView(getContext(), this, imageUrl);
        }
    }
}
