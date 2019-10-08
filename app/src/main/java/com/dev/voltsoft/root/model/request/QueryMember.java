package com.dev.voltsoft.root.model.request;

import com.dev.voltsoft.lib.db.query.DBQuerySelect;
import com.dev.voltsoft.root.model.Member;

public class QueryMember extends DBQuerySelect<Member>
{

    public QueryMember()
    {
        TargetClass = Member.class;
    }

    public void setMemberId(String id)
    {
        WhereClause.put("Id", id);
    }
}
