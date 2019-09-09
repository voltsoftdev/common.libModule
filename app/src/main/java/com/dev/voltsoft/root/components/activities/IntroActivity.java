package com.dev.voltsoft.root.components.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.dev.voltsoft.root.R;

public class IntroActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sample_page_intro0); // (1)

        Button loginButton = findViewById(R.id.loginButton); // (2)

        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Context c = IntroActivity.this;

                Intent intent = new Intent(c, RegistrationActivity.class);

                intent.putExtra("message", "회원가입으로 이동 하였음 !!");

                startActivity(intent);

            }
        });
    }
}
