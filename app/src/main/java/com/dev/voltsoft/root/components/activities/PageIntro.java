package com.dev.voltsoft.root.components.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.dev.voltsoft.lib.IResponseListener;
import com.dev.voltsoft.lib.RequestHandler;
import com.dev.voltsoft.lib.component.CommonActivity;
import com.dev.voltsoft.lib.db.DBQueryResponse;
import com.dev.voltsoft.lib.model.BaseResponse;
import com.dev.voltsoft.root.R;
import com.dev.voltsoft.root.model.Member;
import com.dev.voltsoft.root.model.request.QueryMember;
import com.dev.voltsoft.root.model.request.RequestLottoPrize;

import java.util.ArrayList;

public class PageIntro extends CommonActivity
{


    @Override
    protected void init(Bundle savedInstanceState) throws Exception
    {
        setContentView(R.layout.sample_page_intro);

        Button button = findViewById(R.id.introButton01);

        button.setOnClickListener(this);

        Button button2 = findViewById(R.id.introButton02);

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

                // queryMemberData("woozie4");

                RequestLottoPrize requestLottoPrize = new RequestLottoPrize();
                requestLottoPrize.number = 1;

                RequestHandler.getInstance().handle(requestLottoPrize);

                break;
            }

            case R.id.introButton02:
            {

                moveToActivity(PageRegistration.class);

                break;
            }
        }
    }

    private void queryMemberData(String id)
    {
        QueryMember queryMember = new QueryMember();
        queryMember.setMemberId(id);
        queryMember.setContext(this);
        queryMember.setResponseListener(new IResponseListener()
        {
            @SuppressWarnings("unchecked")
            @Override
            public void onResponseListen(BaseResponse response)
            {
                DBQueryResponse<Member> queryResponse = (DBQueryResponse<Member>) response;

                if (queryResponse.getResponseCode() == 1)
                {
                    ArrayList<Member> memberList = (ArrayList<Member>) queryResponse.getResponseModel();

                    Member member = memberList.get(0);

                    Toast.makeText(PageIntro.this, ">> Password = " + member.Password, Toast.LENGTH_LONG).show();
                }
            }
        });

        RequestHandler.getInstance().handle(queryMember);
    }
}
