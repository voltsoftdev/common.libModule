package com.dev.voltsoft.root.components.activities;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.dev.voltsoft.lib.IResponseListener;
import com.dev.voltsoft.lib.RequestHandler;
import com.dev.voltsoft.lib.component.CommonActivity;
import com.dev.voltsoft.lib.firebase.db.FireBaseDBRequest;
import com.dev.voltsoft.lib.firebase.db.FireBaseDBResponse;
import com.dev.voltsoft.lib.firebase.db.RequestType;
import com.dev.voltsoft.lib.model.BaseModel;
import com.dev.voltsoft.lib.model.BaseResponse;
import com.dev.voltsoft.lib.utility.UtilityUI;
import com.dev.voltsoft.lib.view.list.CompositeViewHolder;
import com.dev.voltsoft.lib.view.list.ICommonItem;
import com.dev.voltsoft.lib.view.list.simple.ISimpleListStrategy;
import com.dev.voltsoft.lib.view.list.simple.SimpleRecyclerView;
import com.dev.voltsoft.root.R;
import com.dev.voltsoft.root.model.Animal;
import com.dev.voltsoft.root.model.Lion;
import com.dev.voltsoft.root.model.Monkey;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class PageMain extends CommonActivity implements ISimpleListStrategy
{
    private SimpleRecyclerView mMainListView;

    private ArrayList<ICommonItem> mListItemList = new ArrayList<>();

    @Override
    protected void init(Bundle savedInstanceState) throws Exception
    {
        setContentView(R.layout.page_main);

        mMainListView = findViewById(R.id.mainListView);
        mMainListView.setSimpleListStrategy(this);  // (2) 리스트 아이템뷰를 어떻게 구성하고 데이터를 매핑 할것인지 정하는 부분

        requestAnimalList();
    }

    private void requestAnimalList()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        FireBaseDBRequest request = new FireBaseDBRequest();

        request.setType(RequestType.GET);

        request.setReference(reference);

        request.mappingTarget(Animal.class, "TBL_ANIMAL");

        request.setResponseListener(new IResponseListener() {
            @Override
            public void onResponseListen(BaseResponse response)
            {
                FireBaseDBResponse dbResponse = (FireBaseDBResponse) response;

                if (dbResponse.isResponseSuccess())
                {
                    ArrayList<ICommonItem> itemList = new ArrayList<>();

                    ArrayList<Animal> animalList = dbResponse.resultList();

                    itemList.addAll(animalList);

                    mMainListView.addItemList(itemList);
                }
            }
        });

        RequestHandler.getInstance().handle(request);
    }

    private void requestLionList()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        FireBaseDBRequest request = new FireBaseDBRequest();

        request.setType(RequestType.GET);

        request.setReference(reference);

        request.mappingTarget(Animal.class, "TBL_LION");

        request.setResponseListener(new IResponseListener() {
            @Override
            public void onResponseListen(BaseResponse response)
            {
                FireBaseDBResponse dbResponse = (FireBaseDBResponse) response;

                if (dbResponse.isResponseSuccess())
                {
                    ArrayList<Animal> animalList = dbResponse.resultList();

                    mListItemList.addAll(animalList);

                    mMainListView.addItemList(mListItemList);
                }
            }
        });

        RequestHandler.getInstance().handle(request);
    }

    private void requestMonkeyList()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        FireBaseDBRequest request = new FireBaseDBRequest();

        request.setType(RequestType.GET);

        request.setReference(reference);

        request.mappingTarget(Animal.class, "TBL_MONKEY");

        request.setResponseListener(new IResponseListener() {
            @Override
            public void onResponseListen(BaseResponse response)
            {
                FireBaseDBResponse dbResponse = (FireBaseDBResponse) response;

                if (dbResponse.isResponseSuccess())
                {
                    ArrayList<Animal> animalList = dbResponse.resultList();

                    mListItemList.addAll(animalList);

                    mMainListView.addItemList(mListItemList);
                }
            }
        });

        RequestHandler.getInstance().handle(request);
    }

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
        Animal animal = (Animal) item;

        // TODO (2) -2 , position (N) 번쨰에 해당 하는 객체 정보를 가져와서 , 뷰에 세팅 해주는 부분

        ImageView imageView = holder.find(R.id.picImageView);

        TextView textView1 = holder.find(R.id.textView01);

        TextView textView2 = holder.find(R.id.textView02);

        TextView textView3 = holder.find(R.id.textView03);

        if (viewType == 200)
        {


            textView1.setText(animal.Name);

            textView2.setText(animal.Gender);

            textView3.setText(animal.BirthDate);

            UtilityUI.setThumbNailImageView(PageMain.this, imageView, animal.imagePath);
        }
        else if (viewType == 100)
        {
            textView1.setText(animal.Name);

            textView2.setText(animal.Gender);
        }
    }
}
