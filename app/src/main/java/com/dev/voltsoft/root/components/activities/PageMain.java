package com.dev.voltsoft.root.components.activities;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.dev.voltsoft.lib.component.CommonActivity;
import com.dev.voltsoft.lib.utility.UtilityUI;
import com.dev.voltsoft.lib.view.list.CompositeViewHolder;
import com.dev.voltsoft.lib.view.list.ICommonItem;
import com.dev.voltsoft.lib.view.list.simple.ISimpleListStrategy;
import com.dev.voltsoft.lib.view.list.simple.SimpleRecyclerView;
import com.dev.voltsoft.root.R;
import com.dev.voltsoft.root.model.Lion;
import com.dev.voltsoft.root.model.Monkey;

import java.util.ArrayList;

public class PageMain extends CommonActivity
{
    private SimpleRecyclerView mMainListView;

    @Override
    protected void init(Bundle savedInstanceState) throws Exception
    {
        setContentView(R.layout.page_main);

        ArrayList<ICommonItem> lionList = new ArrayList<>();

        for (int i = 0 ; i < 100 ; i++)
        {
            Lion lion = new Lion();
            lion.Name = "가짜 사자 " + i;
            lion.Gender = "남성";
            lion.BirthDate = "1988-00-00";
            lion.imagePath = "http://voltsoftware.co.kr:8080/examples/lionKingSample1.jpg";

            lionList.add(lion);
        }

        for (int i = 0 ; i < 50 ; i++)
        {
            Monkey monkey = new Monkey();
            monkey.Name = "가짜 원숭이 " + i;
            monkey.Gender = "남성";
            monkey.BirthDate = "1988-00-00";
            monkey.imagePath = "http://voltsoftware.co.kr:8080/examples/lionKingSample4.jpg";

            lionList.add(monkey);
        }

        mMainListView = findViewById(R.id.mainListView);
        mMainListView.addItemList(lionList); // (1) 데이터 리스트 넣기
        mMainListView.setSimpleListStrategy(new ISimpleListStrategy()
        {
            @Override
            public View createItemView(ViewGroup parent, int viewType)
            {
                // (2) -1 , 재활용할 아이템 뷰를 생성하는 부분

                if (viewType == 100)
                {
                    return inflate(R.layout.view_list_item_monkey, parent);
                }
                else if (viewType == 200)
                {
                    return inflate(R.layout.view_list_item_lion, parent);
                }
                else
                {
                    return null;
                }
            }

            @Override
            public void drawItemView(CompositeViewHolder holder, int position, int viewType, ICommonItem item)
            {
                // TODO (2) -2 , position (N) 번쨰에 해당 하는 객체 정보를 가져와서 , 뷰에 세팅 해주는 부분

                ImageView imageView = holder.find(R.id.picImageView);

                TextView textView1 = holder.find(R.id.textView01);

                TextView textView2 = holder.find(R.id.textView02);

                TextView textView3 = holder.find(R.id.textView03);

                if (viewType == 200)
                {
                    Lion lion = (Lion) item;

                    textView1.setText(lion.Name);

                    textView2.setText(lion.Gender);

                    textView3.setText(lion.BirthDate);

                    UtilityUI.setThumbNailImageView(PageMain.this, imageView, lion.imagePath);
                }
                else if (viewType == 100)
                {
                    Monkey monkey = (Monkey) item;

                    textView1.setText(monkey.Name);

                    textView2.setText(monkey.Gender);

                    textView3.setText(monkey.AppleName);
                }
            }
        });  // (2) 리스트 아이템뷰를 어떻게 구성하고 데이터를 매핑 할것인지 정하는 부분
    }
}
