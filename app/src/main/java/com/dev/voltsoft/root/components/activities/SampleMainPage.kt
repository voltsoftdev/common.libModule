package com.dev.voltsoft.root.components.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.dev.voltsoft.lib.component.CommonActivity
import com.dev.voltsoft.lib.view.CircleImageView
import com.dev.voltsoft.lib.view.ImageLoaderView
import com.dev.voltsoft.lib.view.list.CompositeViewHolder
import com.dev.voltsoft.lib.view.list.ICommonItem
import com.dev.voltsoft.lib.view.list.simple.ISimpleListStrategy
import com.dev.voltsoft.lib.view.list.simple.SimpleRecyclerView
import com.dev.voltsoft.lib.view.menudrawer.MenuDrawer
import com.dev.voltsoft.lib.view.menudrawer.Position
import com.dev.voltsoft.root.R
import com.dev.voltsoft.root.model.Lecture

open class SampleMainPage : CommonActivity(), ISimpleListStrategy {

    lateinit var menuDrawer : MenuDrawer
    lateinit var menuButton : Button
    lateinit var listView   : SimpleRecyclerView

    override fun init(savedInstanceState: Bundle?)
    {
        menuDrawer = MenuDrawer.attach(this,
            MenuDrawer.Type.OVERLAY, Position.LEFT, MenuDrawer.MENU_DRAG_WINDOW)
        menuDrawer.setContentView(R.layout.page_main)
        menuDrawer.setMenuView(R.layout.view_side_bar)
        menuDrawer.setDropShadowSize(1)

        menuButton = find(R.id.mainMenuButton)
        menuButton.setOnClickListener(this)

        val itemList : ArrayList<ICommonItem> = ArrayList()

        for (i in 1 .. 100)
        {
            val lecture : Lecture = Lecture()

            Log.d("woozie" , ">> Lecture created i = " + i);

            itemList.add(lecture)
        }

        listView = findViewById(R.id.mainList)
        listView.simpleListStrategy = this
        listView.addItemList(itemList)


        var circleImageView : CircleImageView = find(R.id.profileImage)

        circleImageView.loadImage("https://i.pinimg.com/originals/40/ae/cd/40aecd3a61715fb9ba210158a66e0efd.jpg")
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
        }
    }

    @Override
    override fun createItemView(parent: ViewGroup?, viewType: Int): View
    {
        val v : View = inflate(R.layout.view_item_lecture, parent)

        return v
    }

    @Override
    override fun drawItemView
                (holder: CompositeViewHolder?, position: Int, viewType: Int, item: ICommonItem?)
    {
        val imageView : ImageLoaderView? = holder?.find(R.id.imageView)

        imageView?.loadTopRoundedImage("https://i.pinimg.com/originals/40/ae/cd/40aecd3a61715fb9ba210158a66e0efd.jpg")
    }
}