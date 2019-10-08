package com.dev.voltsoft.root.components.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.dev.voltsoft.lib.IResponseListener;
import com.dev.voltsoft.lib.RequestHandler;
import com.dev.voltsoft.lib.component.CommonActivity;
import com.dev.voltsoft.lib.db.DBQueryResponse;
import com.dev.voltsoft.lib.db.query.DBQueryInsert;
import com.dev.voltsoft.lib.model.BaseResponse;
import com.dev.voltsoft.lib.view.insert.InsertForm;
import com.dev.voltsoft.root.R;
import com.dev.voltsoft.root.model.Member;

import java.util.ArrayList;
import java.util.Arrays;

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

        mButton1 = findViewById(R.id.registrationButton);
        mButton1.setOnClickListener(this);
    }

    @Override
    protected void onClickEvent(View v)
    {
        super.onClickEvent(v);// 연타 방지 내용이 들어가 있음

        switch (v.getId())
        {
            case R.id.registrationButton:
            {
                String memberId = mInsertFormId.getInsertedText();

                String memberName = mInsertFormNickName.getInsertedText();

                String memberPassword1 = mInsertFormPassword1.getInsertedText();

                String memberPassword2 = mInsertFormPassword2.getInsertedText();

                Member member = new Member();
                member.Id = memberId;
                member.Password = memberPassword1;
                member.NickName = memberName;

                DBQueryInsert<Member> queryInsert = new DBQueryInsert<>();
                queryInsert.addInstance(member);
                queryInsert.setContext(this);
                queryInsert.setResponseListener(new IResponseListener()
                {
                    @SuppressWarnings("unchecked")
                    @Override
                    public void onResponseListen(BaseResponse response)
                    {
                        DBQueryResponse<Member> queryResponse = (DBQueryResponse<Member>) response;

                        if (queryResponse.isInserted())
                        {
                            Toast.makeText(PageRegistration.this, ">> Inserted !!", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                RequestHandler.getInstance().handle(queryInsert);


                break;
            }
        }
    }
}
