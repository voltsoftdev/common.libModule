package com.dev.voltsoft.lib.view.list.simple;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.dev.voltsoft.lib.R;
import com.dev.voltsoft.lib.view.list.CommonRecyclerAdapter;
import com.dev.voltsoft.lib.view.list.CommonRecyclerView;
import com.dev.voltsoft.lib.view.list.CompositeViewHolder;
import com.dev.voltsoft.lib.view.list.ICommonItem;

import java.util.ArrayList;

public class SimpleRecyclerView extends FrameLayout
{
    private RecyclerView        mCommonRecyclerView;
    private SimpleListAdapter   mCommonRecyclerAdapter;

    private ISimpleListStrategy SimpleListStrategy;

    public SimpleRecyclerView(Context context)
    {
        super(context);

        init(context, null, 0);
    }

    public SimpleRecyclerView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);

        init(context, attrs, 0);
    }

    public SimpleRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);

        init(context, attrs, defStyle);
    }

    private void init(Context c, AttributeSet attrs, int defStyle)
    {
        LayoutInflater.from(c).inflate(R.layout.view_simple_listview, this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(c);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mCommonRecyclerAdapter = new SimpleListAdapter(c);

        mCommonRecyclerView = findViewById(R.id.rootListView);
        mCommonRecyclerView.setHasFixedSize(true);
        mCommonRecyclerView.setLayoutManager(linearLayoutManager);
        mCommonRecyclerView.setAdapter(mCommonRecyclerAdapter);

        setWillNotDraw(false);
    }

    public ISimpleListStrategy getSimpleListStrategy()
    {
        return SimpleListStrategy;
    }

    public void setSimpleListStrategy(ISimpleListStrategy listStrategy)
    {
        SimpleListStrategy = listStrategy;

        mCommonRecyclerAdapter.notifyDataSetChanged();
    }

    public void addItemList(ArrayList<ICommonItem> itemList)
    {
        if (mCommonRecyclerAdapter != null)
        {
            mCommonRecyclerAdapter.setItemList(itemList);
            mCommonRecyclerAdapter.notifyDataSetChanged();
        }
    }

    private class SimpleListAdapter extends CommonRecyclerAdapter
    {

        public SimpleListAdapter(Context context)
        {
            super(context);
        }

        @Override
        protected CompositeViewHolder createBodyViewHolder(ViewGroup parent, int viewType)
        {
            View view = (SimpleListStrategy != null ?
                    SimpleListStrategy.createItemView(parent, viewType) : null);

            return new CompositeViewHolder(view);
        }

        @Override
        protected void bindBodyItemView
                (CompositeViewHolder holder, int position, int viewType, ICommonItem item) throws Exception
        {
            if (SimpleListStrategy != null)
            {
                SimpleListStrategy.drawItemView(holder, position, viewType, item);
            }
        }
    }
}
