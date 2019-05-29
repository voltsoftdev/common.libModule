package com.dev.voltsoft.root.components.activities;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.dev.voltsoft.lib.component.CommonActivity;
import com.dev.voltsoft.lib.utility.UtilityUI;
import com.dev.voltsoft.lib.view.list.CompositeViewHolder;
import com.dev.voltsoft.lib.view.list.ICommonItem;
import com.dev.voltsoft.lib.view.list.simple.ISimpleListStrategy;
import com.dev.voltsoft.lib.view.list.simple.SimpleRecyclerView;
import com.dev.voltsoft.lib.view.menudrawer.MenuDrawer;
import com.dev.voltsoft.lib.view.menudrawer.Position;
import com.dev.voltsoft.root.R;

public class SampleMainPage extends CommonActivity implements ISimpleListStrategy {

    private MenuDrawer  mMenuDrawer;
    private Button      mMenuButton;

    private SimpleRecyclerView mSimpleListView;

    @Override
    protected void init(Bundle savedInstanceState) throws Exception
    {
        int menuSize = (int) (UtilityUI.getScreenWidth(this) * 0.8f);

        mMenuDrawer = MenuDrawer.attach(this, MenuDrawer.Type.OVERLAY, Position.LEFT, MenuDrawer.MENU_DRAG_WINDOW);
        mMenuDrawer.setContentView(R.layout.page_main);
        mMenuDrawer.setMenuSize(R.layout.view_side_bar);
        mMenuDrawer.setMenuSize(menuSize);
        mMenuDrawer.setDropShadowSize(1);

        mSimpleListView = findViewById(R.id.mainList);
        mSimpleListView.setSimpleListStrategy(this);

        Button sidebarButton1 = findViewById(R.id.sideBarButton1);
        Button sidebarButton2 = findViewById(R.id.sideBarButton2);
        Button sidebarButton3 = findViewById(R.id.sideBarButton3);
        Button sidebarButton4 = findViewById(R.id.sideBarButton4);

        sidebarButton1.setOnClickListener(this);
        sidebarButton2.setOnClickListener(this);
        sidebarButton3.setOnClickListener(this);
        sidebarButton4.setOnClickListener(this);

    }

    @Override
    protected void onClickEvent(View v)
    {
        super.onClickEvent(v);

        switch (v.getId())
        {
            case R.id.sideBarButton1:
            {
                mSimpleListView.setListType(SimpleRecyclerView.VERTICAL_LIST);

                mMenuDrawer.closeMenu();

                break;
            }

            case R.id.sideBarButton2:
            {
                mSimpleListView.setListType(SimpleRecyclerView.HORIZONTAL_LIST);

                mMenuDrawer.closeMenu();

                break;
            }

            case R.id.sideBarButton3:
            {
                mSimpleListView.setListType(SimpleRecyclerView.GRID_LIST);

                mMenuDrawer.closeMenu();

                break;
            }

            case R.id.sideBarButton4:
            {
                break;
            }
        }
    }

    @Override
    public View createItemView(ViewGroup parent, int viewType)
    {
        return null;
    }

    @Override
    public void drawItemView(CompositeViewHolder holder, int position, int viewType, ICommonItem item)
    {

    }
}
