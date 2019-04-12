package com.dev.voltsoft.root;

import android.app.Application;
import com.dev.voltsoft.lib.utility.CommonPreference;

public class Root extends Application
{

    @Override
    public void onCreate() {
        super.onCreate();

        CommonPreference.init(this);
    }
}
