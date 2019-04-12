package com.dev.voltsoft.lib.view.insert.model;

import com.dev.voltsoft.lib.view.list.ICommonItem;

import java.util.Observable;

public class ItemInsert<D extends Object> extends Observable implements ICommonItem
{
    public static final int SELECT_HORIZONTAL_OPTIONS   = 1017;
    public static final int SELECT_VERTICAL_OPTIONS     = 1018;
    public static final int SELECT_SCROLLABLE_OPTION    = 1019;
    public static final int SELECT_BOOLEAN_OPTION       = 1020;

    private int mItemType;

    private String InsertTitle;

    protected D  InsertedData;

    @Override
    public int listItemType() {
        return mItemType;
    }

    public void setItemType(int type)
    {
        this.mItemType = type;
    }

    public String getInsertTitle() {
        return InsertTitle;
    }

    public void setInsertTitle(String title) {
        InsertTitle = title;
    }

    public D getInsertedData()
    {
        return InsertedData;
    }

    public void setInsertedData(D data)
    {
        InsertedData = data;

        setChanged();

        notifyObservers(data);
    }
}