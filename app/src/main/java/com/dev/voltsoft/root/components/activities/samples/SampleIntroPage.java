package com.dev.voltsoft.root.components.activities.samples;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.dev.voltsoft.lib.IResponseListener;
import com.dev.voltsoft.lib.RequestHandler;
import com.dev.voltsoft.lib.component.CommonActivity;
import com.dev.voltsoft.lib.firebase.db.FireBaseDBRequest;
import com.dev.voltsoft.lib.firebase.db.FireBaseDBResponse;
import com.dev.voltsoft.lib.firebase.db.RequestType;
import com.dev.voltsoft.lib.model.BaseResponse;
import com.dev.voltsoft.lib.view.insert.InsertForm;
import com.dev.voltsoft.root.R;
import com.dev.voltsoft.root.model.Member;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SampleIntroPage extends CommonActivity
{
    private InsertForm  mInsertFormId;

    private InsertForm  mInsertFormPassword;

    private Button  mButton1;

    private Button  mButton2;

    @Override
    protected void init(Bundle savedInstanceState) throws Exception
    {
        setContentView(R.layout.sample_page_intro1);

        mInsertFormId = findViewById(R.id.introInsertForm1);

        mInsertFormPassword = findViewById(R.id.introInsertForm2);
    }


    @Override
    protected void onClickEvent(View v)
    {
        super.onClickEvent(v);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        FireBaseDBRequest fireBaseDBRequest = new FireBaseDBRequest();
        // (1) 파이어베이스 타입 지정
        fireBaseDBRequest.setType(RequestType.GET);
        // (2) 파이어베이스 데이터베이스 지정 (목표 주소지 -1)
        fireBaseDBRequest.setReference(databaseReference);
        // (3) 파이어베이스 데이터베이스 내 테이블 지정 (목표 주소지 -2)
        fireBaseDBRequest.mappingTarget(Member.class, "TBL_MEMBER");
        // (4) 파이어베이스 데이터베이스 조회 조건 달기
        fireBaseDBRequest.equalToValue("MemberId", "-------");

        fireBaseDBRequest.setResponseListener(new IResponseListener()
        {
            @Override
            public void onResponseListen(BaseResponse response)
            {
                FireBaseDBResponse dbResponse = (FireBaseDBResponse) response;

                if (dbResponse.isResponseSuccess())
                {
                    // TODO (6) 응답에 대한 처리
                }
            }
        });
        // (5) 파이어베이스 데이터베이스 조회 동작 실행 !
        RequestHandler.getInstance().handle(fireBaseDBRequest);
    }
}
