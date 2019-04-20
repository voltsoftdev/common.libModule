package com.dev.voltsoft.root.model;

import com.dev.voltsoft.lib.constatns.Unique;
import com.dev.voltsoft.lib.model.BaseModel;

public class Diary extends BaseModel
{
    @Unique
    public String Id;

    public String Title;

    public String Content;

    public String Date;

}
