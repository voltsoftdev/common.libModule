package com.dev.voltsoft.lib.view;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

public class VisibleTextWatcher implements TextWatcher
{
    private final TextView mRoot;

    public VisibleTextWatcher(TextView textView)
    {
        mRoot = textView;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
        String text = mRoot.getText().toString();

        mRoot.setVisibility((TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE));
    }

    @Override
    public void afterTextChanged(Editable s)
    {

    }
}
