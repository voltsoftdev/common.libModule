package com.dev.voltsoft.root.components.activities.samples;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.dev.voltsoft.lib.IResponseListener;
import com.dev.voltsoft.lib.RequestHandler;
import com.dev.voltsoft.lib.component.CommonActivity;
import com.dev.voltsoft.lib.firebase.db.FireBaseDBRequest;
import com.dev.voltsoft.lib.firebase.db.FireBaseDBResponse;
import com.dev.voltsoft.lib.firebase.db.RequestType;
import com.dev.voltsoft.lib.model.BaseResponse;
import com.dev.voltsoft.root.R;
import com.dev.voltsoft.root.model.Member;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SampleRegistrationPage extends CommonActivity
{

    private Button mBottomButton1;

    @Override
    protected void init(Bundle savedInstanceState) throws Exception
    {
        setContentView(R.layout.sample_page_registration);

        mBottomButton1 = findViewById(R.id.registrationButton);
        mBottomButton1.setOnClickListener(this);
    }

    @Override
    protected void onClickEvent(View v)
    {
        super.onClickEvent(v);

        switch (v.getId())
        {
            case R.id.registrationButton:
            {
                String memberId = "woozie34";

                String memberPassword = "qwer123";

                postMemberData(memberId, memberPassword);

                break;
            }
        }
    }

    private void postMemberData(String memberId, String memberPassword)
    {
        Member member = new Member();
        member.Id = memberId;
        member.Password = memberPassword;

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        FireBaseDBRequest fireBaseDBRequest = new FireBaseDBRequest();
        fireBaseDBRequest.setType(RequestType.POST);
        fireBaseDBRequest.setReference(databaseReference);
        fireBaseDBRequest.setPostInstance(member.Id, member);
        fireBaseDBRequest.mappingTarget(Member.class, "TBL_MEMBER_LIST");
        fireBaseDBRequest.setResponseListener(new IResponseListener()
        {
            @Override
            public void onResponseListen(BaseResponse response)
            {
                FireBaseDBResponse dbResponse = (FireBaseDBResponse) response;

                if (dbResponse.isResponseSuccess())
                {
                    Member registeredMember = dbResponse.getFirstResult();

                    Toast.makeText(SampleRegistrationPage.this, "success", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(SampleRegistrationPage.this, "fail", Toast.LENGTH_LONG).show();
                }
            }
        });

        RequestHandler.getInstance().handle(fireBaseDBRequest);
    }
}
