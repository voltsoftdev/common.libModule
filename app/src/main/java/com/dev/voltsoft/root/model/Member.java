package com.dev.voltsoft.root.model;

import com.dev.voltsoft.lib.db.Unique;
import com.dev.voltsoft.lib.model.BaseModel;

import java.io.Serializable;

public class Member extends BaseModel implements Serializable
{
    @Unique
    public String Id;

    public String Password;

    public String NickName;

    public String Id_Password;

    public int Type; // 1 2 3
}
