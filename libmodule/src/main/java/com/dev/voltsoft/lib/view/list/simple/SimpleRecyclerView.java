package com.dev.voltsoft.lib.view.list.simple;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
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
    public static final int VERTICAL_LIST = 1;
    public static final int HORIZONTAL_LIST = 2;
    public static final int GRID_LIST = 3;

    private RecyclerView        mCommonRecyclerView;

    private SimpleListAdapter   mCommonRecyclerAdapter;

    private LinearLayoutManager mLayoutManager0;

    private GridLayoutManager   mLayoutManager1;

    private ISimpleListStrategy SimpleListStrategy;

    private int mListType;

    private int mGridColCount;

    public SimpleRecyclerView(Context context)
    {
        super(context);

        init(context, null, -1);
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
        TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.SimpleRecyclerView, defStyle, 0);

        mListType = a.getInt(R.styleable.SimpleRecyclerView_listType, VERTICAL_LIST);

        mGridColCount = a.getInt(R.styleable.SimpleRecyclerView_gridColCount, 2);

        LayoutInflater.from(c).inflate(R.layout.view_simple_listview, this);

        mLayoutManager0 = new LinearLayoutManager(c);
        mLayoutManager1 = new GridLayoutManager(c, mGridColCount);

        mCommonRecyclerAdapter = new SimpleListAdapter(c);

        mCommonRecyclerView = findViewById(R.id.rootListView);
        mCommonRecyclerView.setHasFixedSize(true);
        mCommonRecyclerView.setAdapter(mCommonRecyclerAdapter);

        setWillNotDraw(false);

        a.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        RecyclerView.LayoutManager layoutManager = null;

        switch (mListType)
        {
            case VERTICAL_LIST:
            {
                mLayoutManager0.setOrientation(LinearLayoutManager.VERTICAL);

                layoutManager = mLayoutManager0;

                break;
            }

            case HORIZONTAL_LIST:
            {
                mLayoutManager0.setOrientation(LinearLayoutManager.HORIZONTAL);

                layoutManager = mLayoutManager0;

                break;
            }

            case GRID_LIST:
            {
                mLayoutManager1.setSpanCount(mGridColCount);

                layoutManager = mLayoutManager1;
                break;
            }
        }

        if (layoutManager != null)
        {
            mCommonRecyclerView.setLayoutManager(layoutManager);

            mCommonRecyclerAdapter.notifyDataSetChanged();

            setWillNotDraw(true);
        }

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

    public void setListType(int i)
    {
        mListType = i;

        setWillNotDraw(false);

        this.requestLayout();
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
            View view = (SimpleListStrategy != null ? SimpleListStrategy.createItemView(parent, viewType) : null);

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

    public RecyclerView getRecyclerView()
    {
        return mCommonRecyclerView;
    }

    public CommonRecyclerAdapter getAdapter()
    {
        return mCommonRecyclerAdapter;
    }

    public RecyclerView.LayoutManager getLayoutManager()
    {
        return (mListType == GRID_LIST ? mLayoutManager1 : mLayoutManager0);
    }
}
