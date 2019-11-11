package com.dev.voltsoft.root.components.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.dev.voltsoft.lib.IResponseListener;
import com.dev.voltsoft.lib.RequestHandler;
import com.dev.voltsoft.lib.component.CommonActivity;
import com.dev.voltsoft.lib.db.DBQueryResponse;
import com.dev.voltsoft.lib.firebase.db.FireBaseDBRequest;
import com.dev.voltsoft.lib.firebase.db.FireBaseDBResponse;
import com.dev.voltsoft.lib.firebase.db.RequestType;
import com.dev.voltsoft.lib.model.BaseResponse;
import com.dev.voltsoft.lib.view.insert.InsertForm;
import com.dev.voltsoft.root.R;
import com.dev.voltsoft.root.model.Animal;
import com.dev.voltsoft.root.model.Member;
import com.dev.voltsoft.root.model.request.QueryMember;
import com.dev.voltsoft.root.model.request.RequestLottoPrize;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class PageIntro extends CommonActivity
{
    private InsertForm mInsertFormId;

    private InsertForm mInsertFormPassword;

    @Override
    protected void init(Bundle savedInstanceState) throws Exception
    {
        setContentView(R.layout.sample_page_intro);

        mInsertFormId = findViewById(R.id.introInsertForm1);

        mInsertFormPassword = findViewById(R.id.introInsertForm2);

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
                String userId = mInsertFormId.getInsertedText();

                String userPassword = mInsertFormPassword.getInsertedText();

                login(userId, userPassword);

                break;
            }

            case R.id.introButton02:
            {

                moveToActivity(PageRegistration.class);

                break;
            }
        }
    }

    private void login(String memberId, String password)
    {
        if (!TextUtils.isEmpty(memberId) && !TextUtils.isEmpty(password))
        {
            String searchValue = memberId + "_" + password;

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

            FireBaseDBRequest request = new FireBaseDBRequest();

            request.setType(RequestType.GET);

            request.setReference(reference);

            request.mappingTarget(Member.class,"TBL_MEMBER");

            request.equalToValue("Id_Password", searchValue);

            request.setResponseListener(new IResponseListener()
            {
                @Override
                public void onResponseListen(BaseResponse response)
                {
                    FireBaseDBResponse dbResponse = (FireBaseDBResponse) response;

                    if (dbResponse.isResponseSuccess())
                    {
                        Member member = dbResponse.getFirstResult();

                        Intent intent = new Intent(PageIntro.this, PageMain.class);

                        intent.putExtra("present", member);

                        startActivity(intent);
                    }
                    else
                    {
                        Toast toast = Toast.makeText(PageIntro.this, "입력된 아이디 및 패스워드를 확인해주세요",  Toast.LENGTH_SHORT);

                        toast.show();
                    }
                }
            });

            RequestHandler.getInstance().handle(request);
        }
        else
        {
            Toast toast = Toast.makeText(PageIntro.this, "입력된 아이디 및 패스워드를 확인해주세요",  Toast.LENGTH_SHORT);

            toast.show();
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
