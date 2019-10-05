package com.dev.voltsoft.root.components.activities;

import android.os.Bundle;
import android.widget.Button;
import com.dev.voltsoft.lib.component.CommonActivity;
import com.dev.voltsoft.root.R;

public class PageRegistration extends CommonActivity
{


    private Button mButton1;

    private Button mButton2;

    @Override
    protected void init(Bundle savedInstanceState) throws Exception
    {
        setContentView(R.layout.sample_page_registration);
    }
}
