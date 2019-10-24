package com.dev.voltsoft.root.model;

import com.dev.voltsoft.lib.view.list.ICommonItem;

import java.io.Serializable;

public class Monkey extends Animal implements Serializable
{
    public String   AppleName;

    @Override
    public int listItemType()
    {

        return 100;
    }
}
