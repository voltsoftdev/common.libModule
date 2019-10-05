package com.dev.voltsoft.root.components.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.dev.voltsoft.lib.component.CommonActivity;
import com.dev.voltsoft.root.R;

public class PageIntro extends CommonActivity
{


    @Override
    protected void init(Bundle savedInstanceState) throws Exception
    {
        setContentView(R.layout.sample_page_intro);

        Button button = findViewById(R.id.introButton01);

        button.setOnClickListener(this);

        Button button2 = findViewById(R.id.introButton01);

        button2.setOnClickListener(this);
    }

    @Override
    protected void onClickEvent(View v)
    {
        super.onClickEvent(v);

        switch (v.getId())
        {
            case R.id.introButton01:
            {
                Intent intent = new Intent(this, PageMain.class);

                intent.putExtra(PageMain.MEMBER_DATA, "");

                startActivity(intent);

                break;
            }

            case R.id.introButton02:
            {
                break;
            }
        }
    }
}
