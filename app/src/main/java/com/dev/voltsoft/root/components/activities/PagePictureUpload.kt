package com.dev.voltsoft.root.components.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.dev.voltsoft.lib.RequestHandler
import com.dev.voltsoft.lib.utility.UtilityData
import com.dev.voltsoft.lib.utility.UtilityUI
import com.dev.voltsoft.root.R
import com.dev.voltsoft.root.model.request.PostFaceImage
import java.io.File


class PagePictureUpload : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.page_pic_upload)

        findViewById<Button>(R.id.button).setOnClickListener {

            val intent = Intent(Intent.ACTION_GET_CONTENT)

            intent.type = "image/*"

            intent.addCategory(Intent.CATEGORY_OPENABLE)

            val mimeTypes = arrayListOf<String>()

            mimeTypes.add("image/jpeg")

            mimeTypes.add("image/png")

            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)

            startActivityForResult(Intent.createChooser(intent, ""), 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        val resultUri : Uri? = data?.data

        if (resultUri != null)
        {
            val filePath : String = UtilityData.getPath(this, resultUri)

            Log.d("woozie", ">> onActivityResult filePath = " + filePath)

            val postFaceImage = PostFaceImage()

            postFaceImage.filePath = filePath

            postFaceImage.home_id = 1

            postFaceImage.tel_num = "01041172727"

            postFaceImage.user_id = "ninecco"

            postFaceImage.setResponseListener {


            }

            RequestHandler.getInstance().handle(postFaceImage)
        }

    }
}