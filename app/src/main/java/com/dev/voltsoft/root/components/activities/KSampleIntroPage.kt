package com.dev.voltsoft.root.components.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.dev.voltsoft.lib.IResponseListener
import com.dev.voltsoft.lib.RequestHandler
import com.dev.voltsoft.lib.component.CommonActivity
import com.dev.voltsoft.lib.firebase.db.FireBaseDBRequest
import com.dev.voltsoft.lib.firebase.db.FireBaseDBResponse
import com.dev.voltsoft.lib.firebase.db.RequestType
import com.dev.voltsoft.lib.model.BaseResponse
import com.dev.voltsoft.lib.utility.CommonPreference
import com.dev.voltsoft.lib.view.insert.InsertForm
import com.dev.voltsoft.root.R
import com.dev.voltsoft.root.model.Member
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

open class KSampleIntroPage : CommonActivity(), IResponseListener {

    lateinit var insertForm0 : InsertForm
    lateinit var insertForm1 : InsertForm

    lateinit var button0 : Button
    lateinit var button1 : Button

    override fun init(savedInstanceState: Bundle?)
    {
        setContentView(R.layout.page_intro)

        CommonPreference.init(application)

        insertForm0 = findViewById(R.id.introInsertForm1)
        insertForm1 = findViewById(R.id.introInsertForm2)

        button0 = findViewById(R.id.introButton01)
        button0.setOnClickListener(this)

        button1 = findViewById(R.id.introButton02)
        button1.setOnClickListener(this)

        val ref : CommonPreference = CommonPreference.getInstance()

        val memberId : String = ref.getSharedValueByString("Id", "")
        val memberPw : String = ref.getSharedValueByString("Password", "")

        if (!TextUtils.isEmpty(memberId))
        {
            insertForm0.insertView.setText(memberId)
            insertForm1.insertView.setText(memberPw)
        }
    }

    @Override
    override fun onClickEvent(v: View?)
    {
        super.onClickEvent(v)

        if (v == button0)
        {
            val id : String = insertForm0.insertedText
            val pw : String = insertForm1.insertedText
            val searchKey = id + "_" + pw

            val ref : DatabaseReference = FirebaseDatabase.getInstance().reference

            val request : FireBaseDBRequest<Member> = FireBaseDBRequest()

            request.reference = ref
            request.type = RequestType.GET
            request.setTargetClass(Member::class.java)
            request.addTargetChild("memberList")
            request.key = "Id_Password"
            request.value = searchKey
            request.responseListener = this

            RequestHandler.getInstance().handle(request)
        }
        else if (v == button1)
        {
            val intent : Intent = Intent(this, KSampleRegistrationPage::class.java)

            startActivity(intent)
        }
    }

    @SuppressWarnings("unchecked")
    override fun onResponseListen(response: BaseResponse?)
    {
        if (response is FireBaseDBResponse<*>)
        {
//            val responseData : FireBaseDBResponse<Member> = response as FireBaseDBResponse<*>
//
//            if (responseData.isResponseSuccess)
//            {
//                val member : Member = responseData.getFirstResult()
//
//                val intent : Intent = Intent(this, KSampleMainPage::class.java)
//
//                intent.putExtra("member", member)
//
//                startActivity(intent)
//
//                val ref : CommonPreference = CommonPreference.getInstance()
//
//                ref.setSharedValueByString("Id", member.Id)
//                ref.setSharedValueByString("NickName", member.NickName)
//                ref.setSharedValueByString("Password", member.Password)
//            }
//            else
//            {
//                Toast.makeText(this, "아이디와 비번을 확인 해주세요", Toast.LENGTH_SHORT).show()
//            }
        }
    }
}