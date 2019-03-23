package com.dev.voltsoft.root.components.activities

import android.os.Bundle
import com.dev.voltsoft.lib.component.CommonActivity
import com.dev.voltsoft.lib.view.insert.InsertForm
import com.dev.voltsoft.root.R

open class SampleRegistrationPage : CommonActivity()
{
    lateinit var insertForm0 : InsertForm
    lateinit var insertForm1 : InsertForm
    lateinit var insertForm2 : InsertForm
    lateinit var insertForm3 : InsertForm

    override fun init(savedInstanceState: Bundle?)
    {
        setContentView(R.layout.page_registration)
    }

}