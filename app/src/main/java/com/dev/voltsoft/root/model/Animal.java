package com.dev.voltsoft.root.model;

import com.dev.voltsoft.lib.view.list.ICommonItem;

public class Animal implements ICommonItem
{

    public String Name;

    public String imagePath;

    public int itemType;

    @Override
    public int listItemType()
    {
        return itemType;
    }
}
