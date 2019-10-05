package com.dev.voltsoft.root.components.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.dev.voltsoft.lib.component.CommonActivity;
import com.dev.voltsoft.lib.view.insert.InsertForm;
import com.dev.voltsoft.root.R;
import com.dev.voltsoft.root.model.Member;

public class PageRegistration extends CommonActivity
{
    private InsertForm mInsertFormId;

    private InsertForm mInsertFormNickName;

    private InsertForm mInsertFormPassword1;

    private InsertForm mInsertFormPassword2;

    private Button mButton1;

    private Button mButton2;

    @Override
    protected void init(Bundle savedInstanceState) throws Exception
    {
        setContentView(R.layout.sample_page_registration);

        mInsertFormId = findViewById(R.id.registration01);

        mInsertFormNickName = findViewById(R.id.registration02);

        mInsertFormPassword1 = findViewById(R.id.registration03);

        mInsertFormPassword2 = findViewById(R.id.registration04);

        mButton1.setOnClickListener(this);
    }

    @Override
    protected void onClickEvent(View v)
    {
        super.onClickEvent(v);// 연타 방지 내용이 들어가 있음

        switch (v.getId())
        {
            case R.id.buttonConfirm:
            {
                String memberId = mInsertFormId.getInsertedText();

                String memberName = mInsertFormNickName.getInsertedText();

                String memberPassword1 = mInsertFormPassword1.getInsertedText();

                String memberPassword2 = mInsertFormPassword2.getInsertedText();

                Member member = new Member();
                member.Id = memberId;
                member.Password = memberPassword1;
                member.NickName = memberName;

                Intent intent = new Intent(this, PageMain.class);

                intent.putExtra(PageMain.MEMBER_DATA, member);

                startActivity(intent);

                break;
            }
        }
    }
}
