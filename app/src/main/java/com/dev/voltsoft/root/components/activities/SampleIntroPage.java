package com.dev.voltsoft.root.components.activities;

import android.os.Bundle;
import android.util.Log;
import com.dev.voltsoft.lib.component.CommonActivity;
import com.dev.voltsoft.root.R;

public class SampleIntroPage extends CommonActivity
{
    @Override
    protected void init(Bundle savedInstanceState) throws Exception
    {
        setContentView(R.layout.page_intro);
    }
}
