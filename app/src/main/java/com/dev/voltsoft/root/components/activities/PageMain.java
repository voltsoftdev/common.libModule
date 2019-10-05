package com.dev.voltsoft.root.components.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.dev.voltsoft.lib.component.CommonActivity;
import com.dev.voltsoft.root.R;
import com.dev.voltsoft.root.model.Member;

public class PageMain extends CommonActivity
{
    public static final String MEMBER_DATA = "MEMBER_INFO";

    private TextView mMemberTextView1;

    private TextView mMemberTextView2;

    private TextView mMemberTextView3;

    @Override
    protected void init(Bundle savedInstanceState) throws Exception
    {
        setContentView(R.layout.sample_page_main);

        Intent intent = getIntent();

        Member member = (Member) intent.getSerializableExtra("memberData");

        setMemberData(member);
    }

    private void setMemberData(Member member)
    {
        mMemberTextView1.setText("id : " + member.Id);

        mMemberTextView2.setText("password : " + member.Password);

        mMemberTextView3.setText("NickName : " + member.NickName);
    }
}
