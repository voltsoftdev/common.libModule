package com.dev.voltsoft.root.model;

import com.dev.voltsoft.lib.model.BaseModel;
import com.dev.voltsoft.lib.view.list.ICommonItem;

public class Animal extends BaseModel implements ICommonItem
{

    public String Name;

    public String Gender;

    public String BirthDate;

    public String imagePath;

    public int ItemType;

    @Override
    public int listItemType()
    {
        return ItemType;
    }
}
