package com.dev.voltsoft.lib.db;

import android.database.Cursor;
import com.dev.voltsoft.lib.model.BaseModel;

public interface DBQueryParcelable<M extends BaseModel>
{
    M parse(Cursor cursor);
}
