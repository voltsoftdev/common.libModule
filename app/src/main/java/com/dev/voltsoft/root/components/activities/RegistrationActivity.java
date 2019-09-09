package com.dev.voltsoft.root.components.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.dev.voltsoft.root.R;

public class RegistrationActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.page_registration);

        Intent intent = getIntent();

        String s = intent.getStringExtra("message");


    }
}
