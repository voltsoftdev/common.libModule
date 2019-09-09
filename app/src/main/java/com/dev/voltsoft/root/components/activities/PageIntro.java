package com.dev.voltsoft.root.components.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.dev.voltsoft.lib.component.CommonActivity;
import com.dev.voltsoft.root.R;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class PageIntro extends CommonActivity
{
    @Override
    protected void init(Bundle savedInstanceState) throws Exception
    {
        setContentView(R.layout.sample_page_intro);
    }
}
