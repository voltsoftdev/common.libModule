package com.dev.voltsoft.root.model;

import com.dev.voltsoft.lib.view.list.ICommonItem;

public class Animal implements ICommonItem
{

    public String Name;

    public String Gender;

    public String BirthDate;

    public String imagePath;

    @Override
    public int listItemType()
    {
        return 0;
    }
}
