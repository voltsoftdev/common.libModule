package com.dev.voltsoft.root.components.activities.samples

import android.content.Intent
import android.os.Bundle
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
import com.dev.voltsoft.lib.view.insert.InsertForm
import com.dev.voltsoft.root.R
import com.dev.voltsoft.root.model.Member
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

open class KSampleRegistrationPage : CommonActivity(),  IResponseListener {


    lateinit var insertForm0 : InsertForm
    lateinit var insertForm1 : InsertForm
    lateinit var insertForm2 : InsertForm
    lateinit var insertForm3 : InsertForm

    lateinit var button0 : Button
    lateinit var button1 : Button

    override fun init(savedInstanceState: Bundle?)
    {
        setContentView(R.layout.sample_page_registration)

//        insertForm0 = findViewById(R.id.insertFormId)
//        insertForm1 = findViewById(R.id.insertFormNickName)
//        insertForm2 = findViewById(R.id.insertFormPassword1)
//        insertForm3 = findViewById(R.id.insertFormPassword2)
//
//        button0 = findViewById(R.id.confirmButton)
//        button0.setOnClickListener(this)
//        button1 = findViewById(R.id.registrationCancel)
//        button1.setOnClickListener(this)
    }

//    override fun onEditorAction(v: TextView?, action: Int, event: KeyEvent?): Boolean
//    {
//        if (v == insertForm0.insertView && action == EditorInfo.IME_ACTION_NEXT)
//        {
//            insertForm1.insertView.requestFocus()
//        }
//        else if (v == insertForm1.insertView && action == EditorInfo.IME_ACTION_NEXT)
//        {
//            insertForm2.insertView.requestFocus()
//        }
//        else if (v == insertForm2.insertView && action == EditorInfo.IME_ACTION_NEXT)
//        {
//            insertForm3.insertView.requestFocus()
//        }
//        else if (v == insertForm3.insertView && action == EditorInfo.IME_ACTION_DONE)
//        {
//            UtilityUI.setForceKeyboardDown(this, insertForm3.insertView)
//
//            button0.performClick()
//        }
//
//        return false
//    }

    @Override
    override fun onClickEvent(v: View?)
    {
        super.onClickEvent(v)

        if (v == button0)
        {
            val id : String = insertForm0.insertedText
            val nickName : String = insertForm1.insertedText
            val passWord : String = insertForm2.insertedText
            val searchKey = id + "_" + passWord

            val member = Member()

            member.Id = id
            member.NickName = nickName
            member.Password = passWord
            // member.Id_Password = searchKey

            val ref : DatabaseReference = FirebaseDatabase.getInstance().reference

            val request = FireBaseDBRequest()

            request.reference = ref
            request.type = RequestType.POST
            request.mappingTarget(Member::class.java, "memberList")
            request.setPostInstance(id, member)
            request.responseListener = this

            RequestHandler.getInstance().handle(request)
        }
        else if (v == button1)
        {
            val id : String = insertForm0.insertedText

            val ref : DatabaseReference = FirebaseDatabase.getInstance().reference

            val request = FireBaseDBRequest()

            request.reference = ref
            request.type = RequestType.UPDATE
            request.mappingTarget(Member::class.java, "memberList")
            request.update(id, "NickName", "woozie123124")

            RequestHandler.getInstance().handle(request)
        }
    }

    @SuppressWarnings("unchecked")
    override fun onResponseListen(response: BaseResponse?)
    {
        if (response is FireBaseDBResponse)
        {
//            val responseData = response
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
//            }
//            else
//            {
//                Toast.makeText(this, "회원가입에 실패하였습니다", Toast.LENGTH_SHORT).show()
//            }
        }
    }

}