package com.dev.voltsoft.root.components.activities.samples

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.dev.voltsoft.lib.IResponseListener
import com.dev.voltsoft.lib.RequestHandler
import com.dev.voltsoft.lib.component.CommonActivity
import com.dev.voltsoft.lib.db.query.DBQueryInsert
import com.dev.voltsoft.lib.firebase.db.FireBaseDBRequest
import com.dev.voltsoft.lib.firebase.db.RequestType
import com.dev.voltsoft.lib.model.BaseResponse
import com.dev.voltsoft.lib.utility.UtilityUI
import com.dev.voltsoft.lib.view.CircleImageView
import com.dev.voltsoft.lib.view.ImageLoaderView
import com.dev.voltsoft.lib.view.list.CompositeViewHolder
import com.dev.voltsoft.lib.view.list.ICommonItem
import com.dev.voltsoft.lib.view.list.simple.ISimpleListStrategy
import com.dev.voltsoft.lib.view.list.simple.SimpleRecyclerView
import com.dev.voltsoft.lib.view.menudrawer.MenuDrawer
import com.dev.voltsoft.lib.view.menudrawer.Position
import com.dev.voltsoft.root.R
import com.dev.voltsoft.root.model.Animal
import com.dev.voltsoft.root.model.Diary
import com.dev.voltsoft.root.model.Member
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

open class KSampleMainPage : CommonActivity(), ISimpleListStrategy, IResponseListener {

    lateinit var menuDrawer : MenuDrawer
    lateinit var menuButton : Button
    lateinit var listView   : SimpleRecyclerView

    override fun init(savedInstanceState: Bundle?)
    {
        val menuDrawerSize : Float = UtilityUI.getScreenWidth(this) * (0.8f)

        menuDrawer = MenuDrawer.attach(this, MenuDrawer.Type.OVERLAY, Position.LEFT, MenuDrawer.MENU_DRAG_WINDOW)
        menuDrawer.setContentView(R.layout.sample_page_main)
        menuDrawer.setMenuView(R.layout.sample_view_side_bar)
        menuDrawer.setDropShadowSize(1)
        menuDrawer.menuSize = menuDrawerSize.toInt()

        menuButton = findViewById(R.id.mainMenuButton)
        menuButton.setOnClickListener(this)

        listView = findViewById(R.id.mainList)
        listView.simpleListStrategy = this

        val button0 : Button = findViewById(R.id.sideBarButton1)
        val button1 : Button = findViewById(R.id.sideBarButton2)
        val button2 : Button = findViewById(R.id.sideBarButton3)
        val button3 : Button = findViewById(R.id.sideBarButton4)
        val button4 : Button = findViewById(R.id.sideBarButton5)
        val button5 : Button = findViewById(R.id.sideBarButton6)

        button0.setOnClickListener(this)
        button1.setOnClickListener(this)
        button2.setOnClickListener(this)
        button3.setOnClickListener(this)
        button4.setOnClickListener(this)
        button5.setOnClickListener(this)

        val circleImageView : CircleImageView = find(R.id.profileImage)

        circleImageView.loadImage("https://i.pinimg.com/originals/40/ae/cd/40aecd3a61715fb9ba210158a66e0efd.jpg")

        val textView : TextView = findViewById(R.id.profileName)

        val intent : Intent = this.intent

        val member : Member = intent.getSerializableExtra("member") as Member

        textView.text = member.NickName

        val ref : DatabaseReference = FirebaseDatabase.getInstance().reference

        val request : FireBaseDBRequest = FireBaseDBRequest()

        request.reference = ref
        request.type = RequestType.GET
        request.mappingTarget(Animal::class.java, "animalList")
        request.responseListener = this

        RequestHandler.getInstance().handle(request)

    }

    @Override
    override fun onClickEvent(v: View?)
    {
        super.onClickEvent(v)

        when(v?.id)
        {
            R.id.mainMenuButton ->
            {
                menuDrawer.openMenu()
            }

            R.id.sideBarButton1 ->
            {
                listView.setListType(SimpleRecyclerView.VERTICAL_LIST)

                menuDrawer.closeMenu()
            }

            R.id.sideBarButton2 ->
            {
                listView.setListType(SimpleRecyclerView.HORIZONTAL_LIST)

                menuDrawer.closeMenu()
            }

            R.id.sideBarButton3 ->
            {
                listView.setListType(SimpleRecyclerView.GRID_LIST)

                menuDrawer.closeMenu()
            }

            R.id.sideBarButton4 ->
            {
                val intent = Intent(this, SampleVideoPage::class.java)

                startActivity(intent)
            }

            R.id.sideBarButton5 ->
            {
//                val dbQuery : DBQuerySelect = DBQuerySelect(Diary::class.java)
//
//                dbQuery.context = this
//                dbQuery.responseListener = this
//
//                RequestHandler.getInstance().handle(dbQuery)
            }

            R.id.sideBarButton6 ->
            {
                val diary = Diary()

                val id : String = System.currentTimeMillis().toString()

                diary.Content = "샘플데이터"
                diary.Date = "2019-04-20"
                diary.Title = "제목"
                diary.Id = id

                val dbQuery : DBQueryInsert<Diary> = DBQueryInsert()

                dbQuery.addInstance(diary)
                dbQuery.context = this
                dbQuery.responseListener = this

                RequestHandler.getInstance().handle(dbQuery)
            }
        }
    }

    @SuppressWarnings("unchecked")
    override fun onResponseListen(response: BaseResponse?)
    {
//        if (response is FireBaseDBResponse<*>)
//        {
//            val responseData : FireBaseDBResponse<Animal> = response as FireBaseDBResponse<Animal>
//
//            val itemList : ArrayList<ICommonItem> = arrayListOf()
//
//            itemList.addAll(responseData.resultList())
//
//            listView.addItemList(itemList)
//        }
//        else
//        {
//            Toast.makeText(this, "!!!!" , Toast.LENGTH_SHORT).show()
//        }
    }

    @Override
    override fun createItemView(parent: ViewGroup?, viewType: Int): View
    {

        var v : View = View(this)

        when(viewType)
        {
            100 ->
            {
                v = inflate(R.layout.sample_view_item_animal_lion, parent)
            }

            200 ->
            {
                v = inflate(R.layout.sample_view_item_animal_monkey, parent)
            }
        }


        return v
    }

    @Override
    override fun drawItemView(holder: CompositeViewHolder?, position: Int, viewType: Int, item: ICommonItem?)
    {
        val animal : Animal = item as Animal

        when(viewType)
        {
            100 ->
            {
                val imageView : ImageLoaderView? = holder?.find(R.id.imageView)

                imageView?.loadTopRoundedImage(animal.imagePath)

                val textView : TextView? = holder?.find(R.id.imageName)

                textView?.text = animal.Name
                textView?.setOnClickListener(View.OnClickListener {
                    val intent = Intent(it.context, SampleVideoPage::class.java)

                    startActivity(intent)

                    Toast.makeText(it.context, "!!!", Toast.LENGTH_SHORT).show()
                })
            }

            200 ->
            {
                val imageView : ImageLoaderView? = holder?.find(R.id.imageView)

                imageView?.loadTopRoundedImage(animal.imagePath)

                val textView : TextView? = holder?.find(R.id.imageName)

                textView?.text = animal.Name

                val cardView : androidx.cardview.widget.CardView? = holder?.find(R.id.cardItem);

                if (cardView != null)
                {
                    cardView.setOnClickListener(View.OnClickListener {
                        val intent = Intent(it.context, SampleVideoPage::class.java)

                        startActivity(intent)

                        Toast.makeText(it.context, "!!!", Toast.LENGTH_SHORT).show()
                    })

                }
            }
        }
    }
}