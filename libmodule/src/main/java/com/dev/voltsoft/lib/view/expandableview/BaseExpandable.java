package com.dev.voltsoft.lib.view.expandableview;

import com.dev.voltsoft.lib.view.list.ICommonItem;

/**
 * @author JJShin
 */
public abstract class BaseExpandable implements ICommonItem {

    public static final int STATIC_ITEM = -10;
    public static final int EXPANDED_CHILD = 10;
    public static final int EXPANDABLE_PARENT = 20;

    public abstract Object getRootObject();

    public abstract int getExpandableItemType();
}
