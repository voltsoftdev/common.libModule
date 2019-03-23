package com.dev.voltsoft.lib.view.list.simple;

import android.view.View;
import android.view.ViewGroup;
import com.dev.voltsoft.lib.view.list.CompositeViewHolder;
import com.dev.voltsoft.lib.view.list.ICommonItem;

public interface ISimpleListStrategy
{
    View createItemView(ViewGroup parent, int viewType);

    void drawItemView(CompositeViewHolder holder, int position, int viewType, ICommonItem item);
}
