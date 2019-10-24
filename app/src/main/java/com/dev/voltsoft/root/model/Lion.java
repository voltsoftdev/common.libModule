package com.dev.voltsoft.root.model;

import com.dev.voltsoft.lib.view.list.ICommonItem;

import java.io.Serializable;

public class Lion extends Animal implements Serializable
{
    public String HairColor;

    @Override
    public int listItemType()
    {
        return 200;
    }
}
