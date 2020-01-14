package com.dev.voltsoft.root;

import android.Manifest;
import com.dev.voltsoft.lib.component.CommonApplication;
import com.dev.voltsoft.lib.utility.CommonPreference;

public class Root extends CommonApplication
{

    @Override
    public void onCreate()
    {
        super.onCreate();
    }

    @Override
    public String[] getRuntimePermissions()
    {
        return new String[] {
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE, // STORAGE 그룹 	// TODO 권한체크
                android.Manifest.permission.READ_PHONE_STATE, // PHONE 그룹			// TODO 권한체크
                android.Manifest.permission.READ_CONTACTS, // PHONE 그룹
                Manifest.permission.ACCESS_FINE_LOCATION
        };

    }

    @Override
    public int getApplicationDBVersion()
    {
        return 10;
    }
}
