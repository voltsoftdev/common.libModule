package com.dev.voltsoft.root.components.activities.samples;

import android.os.Bundle;
import android.widget.Button;
import com.dev.voltsoft.lib.component.CommonActivity;
import com.dev.voltsoft.root.R;

public class SampleDiaryPage extends CommonActivity
{

    private Button mButton;

    @Override
    protected void init(Bundle savedInstanceState) throws Exception
    {
        setContentView(R.layout.sample_page_diary);

    }
}
