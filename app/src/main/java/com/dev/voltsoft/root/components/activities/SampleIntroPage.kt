package com.dev.voltsoft.root.components.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.dev.voltsoft.lib.component.CommonActivity
import com.dev.voltsoft.root.R

open class SampleIntroPage : CommonActivity()
{
    lateinit var button0 : Button
    lateinit var button1 : Button
    lateinit var button2 : Button

    override fun init(savedInstanceState: Bundle?)
    {
        setContentView(R.layout.page_intro)

        button0 = findViewById(R.id.introButton01)
        button0.setOnClickListener(this)

        button1 = findViewById(R.id.introButton02)
        button1.setOnClickListener(this)
    }

    @Override
    override fun onClickEvent(v: View?)
    {
        super.onClickEvent(v)

        if (v == button0)
        {
            val intent = Intent(this, SampleMainPage::class.java)

            startActivity(intent)
        }
        else if (v == button1)
        {
            val intent = Intent(this, SampleRegistrationPage::class.java)

            startActivity(intent)
        }
    }
}