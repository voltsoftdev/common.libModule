package com.dev.voltsoft.root.components.activities.samples;

import android.os.Bundle;
import android.widget.Button;
import com.dev.voltsoft.lib.component.CommonActivity;
import com.dev.voltsoft.lib.view.insert.InsertForm;
import com.dev.voltsoft.root.R;

public class SampleIntroPage extends CommonActivity
{
    private InsertForm  mInsertFormId;

    private InsertForm  mInsertFormPassword;

    private Button  mButton1;

    private Button  mButton2;

    @Override
    protected void init(Bundle savedInstanceState) throws Exception
    {
        setContentView(R.layout.sample_page_intro1);

        mInsertFormId = findViewById(R.id.introInsertForm1);

        mInsertFormPassword = findViewById(R.id.introInsertForm2);
    }
}
