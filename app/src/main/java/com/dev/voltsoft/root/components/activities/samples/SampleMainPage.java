package com.dev.voltsoft.root.components.activities.samples;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.dev.voltsoft.lib.IResponseListener;
import com.dev.voltsoft.lib.RequestHandler;
import com.dev.voltsoft.lib.component.CommonActivity;
import com.dev.voltsoft.lib.firebase.db.FireBaseDBRequest;
import com.dev.voltsoft.lib.firebase.db.FireBaseDBResponse;
import com.dev.voltsoft.lib.firebase.db.RequestType;
import com.dev.voltsoft.lib.model.BaseResponse;
import com.dev.voltsoft.lib.utility.UtilityUI;
import com.dev.voltsoft.lib.view.list.CompositeViewHolder;
import com.dev.voltsoft.lib.view.list.ICommonItem;
import com.dev.voltsoft.lib.view.list.simple.ISimpleListStrategy;
import com.dev.voltsoft.lib.view.list.simple.SimpleRecyclerView;
import com.dev.voltsoft.lib.view.menudrawer.MenuDrawer;
import com.dev.voltsoft.lib.view.menudrawer.Position;
import com.dev.voltsoft.root.R;
import com.dev.voltsoft.root.model.Animal;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SampleMainPage extends CommonActivity
{

    private MenuDrawer  mMenuDrawer;
    private Button      mMenuButton;

    private SimpleRecyclerView mSimpleListView;

    @Override
    protected void init(Bundle savedInstanceState) throws Exception
    {
        int menuSize = (int) (UtilityUI.getScreenWidth(this) * 0.8f);

        mMenuDrawer = MenuDrawer.attach(this, MenuDrawer.Type.OVERLAY, Position.LEFT, MenuDrawer.MENU_DRAG_WINDOW);
        mMenuDrawer.setContentView(R.layout.sample_page_main);
        mMenuDrawer.setMenuSize(R.layout.sample_view_side_bar);
        mMenuDrawer.setMenuSize(menuSize);
        mMenuDrawer.setDropShadowSize(1);

        Button sidebarButton1 = findViewById(R.id.sideBarButton1);
        Button sidebarButton2 = findViewById(R.id.sideBarButton2);
        Button sidebarButton3 = findViewById(R.id.sideBarButton3);
        Button sidebarButton4 = findViewById(R.id.sideBarButton4);

        sidebarButton1.setOnClickListener(this);
        sidebarButton2.setOnClickListener(this);
        sidebarButton3.setOnClickListener(this);
        sidebarButton4.setOnClickListener(this);

        mSimpleListView = findViewById(R.id.mainList);
        mSimpleListView.setSimpleListStrategy(new ISimpleListStrategy()
        { // (2) 리스트뷰의 아이템뷰를 어떻게 그릴지 결정
            @Override
            public View createItemView(ViewGroup parent, int viewType)
            {
                return inflate(R.layout.view_item_option, parent);
            }

            @Override
            public void drawItemView(CompositeViewHolder holder, int position, int viewType, ICommonItem item)
            {
                // (4) 아이템 뷰 에 객체 정보 를 붙이기
            }
        });

    }

    @Override
    protected void onClickEvent(View v)
    {
        super.onClickEvent(v);

        // 3주차 과제 답안 부분 START =================================================
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
        // ======================================================================
    }
}
