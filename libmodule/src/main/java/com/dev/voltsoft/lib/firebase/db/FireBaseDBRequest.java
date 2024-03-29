package com.dev.voltsoft.lib.firebase.db;

import androidx.annotation.NonNull;
import android.text.TextUtils;
import com.dev.voltsoft.lib.IResponseListener;
import com.dev.voltsoft.lib.model.BaseModel;
import com.dev.voltsoft.lib.model.BaseRequest;
import com.dev.voltsoft.lib.network.base.INetworkProgressView;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.Arrays;

public class FireBaseDBRequest extends BaseRequest implements Runnable
{
    private DatabaseReference Reference;

    private RequestType mRequestType;

    private ArrayList<String> ChildNameList = new ArrayList<>();

    private Class targetClass;

    private Object postInstance;

    private String WhereClause;

    private String InstanceKey;

    private Object EqualValue;

    private Object EqualStartValue;

    private Object EqualEndValue;

    private int limitStart;

    private int limitEnd;

    private Object UpdateValue;

    private INetworkProgressView ProgressView;

    public DatabaseReference getReference()
    {
        return Reference;
    }

    public void setReference(DatabaseReference reference)
    {
        Reference = reference;
    }

    public RequestType getType()
    {
        return mRequestType;
    }

    public void setType(RequestType type)
    {
        mRequestType = type;
    }

    @Override
    public void run()
    {
        if (mRequestType != null && Reference != null)
        {
            switch (mRequestType)
            {
                case POST:
                {
                    runPost();

                    break;
                }

                case DELETE:
                {
                    break;
                }

                case BIND:
                case GET:
                {
                    runQuery();

                    break;
                }

                case UPDATE:
                {
                    runUpdate();

                    break;
                }
            }
        }
    }

    private void runPost()
    {
        if (ProgressView != null)
        {
            ProgressView.onLoading();
        }

        DatabaseReference ref = null;

        for (String child : ChildNameList)
        {
            ref = (ref == null ? Reference.child(child) : ref.child(child));
        }

        if (ref != null)
        {
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    IResponseListener responseListener = getResponseListener();

                    FireBaseDBResponse fireBaseDBResponse = new FireBaseDBResponse();

                    if (dataSnapshot.exists() && responseListener != null)
                    {
                        String key = dataSnapshot.getKey();

                        Object o = dataSnapshot.getValue(targetClass);

                        fireBaseDBResponse.addResult(key, (BaseModel) o);
                        fireBaseDBResponse.setResponseSuccess(true);

                        responseListener.onResponseListen(fireBaseDBResponse);

                        setResponseListener(null);

                        Reference.removeEventListener(this);
                    }

                    if (ProgressView != null)
                    {
                        ProgressView.onLoadingEnd();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {
                    IResponseListener responseListener = getResponseListener();

                    if (responseListener != null)
                    {
                        FireBaseDBResponse fireBaseDBResponse = new FireBaseDBResponse();
                        fireBaseDBResponse.setResponseSuccess(false);

                        responseListener.onResponseListen(fireBaseDBResponse);

                        setResponseListener(null);

                        Reference.removeEventListener(this);
                    }

                    if (ProgressView != null)
                    {
                        ProgressView.onLoadingEnd();
                    }
                }
            });


            if (TextUtils.isEmpty(InstanceKey))
            {
                ref.push().setValue(postInstance);
            }
            else
            {
                ref.child(InstanceKey).setValue(postInstance);
            }
        }
    }

    private void runUpdate()
    {
        if (ProgressView != null)
        {
            ProgressView.onLoading();
        }

        DatabaseReference ref = null;

        for (String child : ChildNameList)
        {
            ref = (ref == null ? Reference.child(child) : ref.child(child));
        }

        if (ref != null)
        {
            ref.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    IResponseListener responseListener = getResponseListener();

                    if (dataSnapshot.exists() && responseListener != null)
                    {
                        FireBaseDBResponse fireBaseDBResponse = new FireBaseDBResponse();

                        fireBaseDBResponse.setResponseSuccess(true);

                        responseListener.onResponseListen(fireBaseDBResponse);

                        setResponseListener(null);

                        Reference.removeEventListener(this);
                    }

                    if (ProgressView != null)
                    {
                        ProgressView.onLoadingEnd();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {
                    IResponseListener responseListener = getResponseListener();

                    if (responseListener != null)
                    {
                        FireBaseDBResponse fireBaseDBResponse = new FireBaseDBResponse();
                        fireBaseDBResponse.setResponseSuccess(false);

                        responseListener.onResponseListen(fireBaseDBResponse);

                        setResponseListener(null);

                        Reference.removeEventListener(this);
                    }

                    if (ProgressView != null)
                    {
                        ProgressView.onLoadingEnd();
                    }
                }
            });
            ref.child(InstanceKey).child(WhereClause).setValue(UpdateValue);
        }
    }

    private void runQuery()
    {
        if (ProgressView != null)
        {
            ProgressView.onLoading();
        }

        DatabaseReference ref = null;

        for (String child : ChildNameList)
        {
            ref = (ref == null ? Reference.child(child) : ref.child(child));
        }


        if (ref != null)
        {
            Query query = null;

            if (TextUtils.isEmpty(WhereClause))
            {
                query = ref.orderByKey();
            }
            else
            {
                query = ref.orderByChild(WhereClause);

                if (EqualValue instanceof String)
                {
                    query = query.equalTo((String) EqualValue);
                }
                else if (EqualValue instanceof Integer)
                {
                    query = query.equalTo((int) EqualValue);
                }
                else if (EqualStartValue instanceof String)
                {
                    query = query.startAt((String) EqualStartValue);
                }
                else if (EqualStartValue instanceof Integer)
                {
                    query = query.startAt((int) EqualStartValue);
                }


                if (EqualEndValue instanceof String)
                {
                    query = query.endAt((String) EqualStartValue);
                }
                else if (EqualEndValue instanceof Integer)
                {
                    query = query.endAt((int) EqualStartValue);
                }

                if (limitStart > 0)
                {
                    query = query.limitToFirst(limitStart);
                }
                else if (limitEnd > 0)
                {
                    query = query.limitToLast(limitEnd);
                }
            }


            query.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    IResponseListener responseListener = getResponseListener();

                    if (responseListener != null)
                    {
                        FireBaseDBResponse fireBaseDBResponse = new FireBaseDBResponse();

                        if (dataSnapshot.exists())
                        {
                            for (DataSnapshot child : dataSnapshot.getChildren())
                            {
                                try
                                {
                                    String key = child.getKey();

                                    Object o = child.getValue(targetClass);

                                    fireBaseDBResponse.addResult(key, (BaseModel) o);
                                    fireBaseDBResponse.setResponseSuccess(true);
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }

                            responseListener.onResponseListen(fireBaseDBResponse);
                        }
                        else
                        {
                            fireBaseDBResponse.setResponseSuccess(false);

                            responseListener.onResponseListen(fireBaseDBResponse);
                        }

                        if (mRequestType != RequestType.BIND)
                        {
                            setResponseListener(null);

                            Reference.removeEventListener(this);
                        }
                    }

                    if (ProgressView != null)
                    {
                        ProgressView.onLoadingEnd();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {
                    IResponseListener responseListener = getResponseListener();

                    if (responseListener != null)
                    {
                        FireBaseDBResponse fireBaseDBResponse = new FireBaseDBResponse();
                        fireBaseDBResponse.setResponseSuccess(false);

                        responseListener.onResponseListen(fireBaseDBResponse);

                        setResponseListener(null);

                        Reference.removeEventListener(this);
                    }

                    if (ProgressView != null)
                    {
                        ProgressView.onLoadingEnd();
                    }
                }
            });
        }
    }

    public FireBaseDBRequest mappingTarget(Class targetClass, String ... s)
    {
        this.targetClass = targetClass;

        ChildNameList.addAll(Arrays.asList(s));

        return this;
    }

    public String getWhereClause()
    {
        return WhereClause;
    }


    public String getValue()
    {
        return WhereClause;
    }

    public Object getPostInstance()
    {
        return postInstance;
    }

    public void setPostInstance(Object t)
    {
        this.postInstance = t;
    }

    public void setPostInstance(String key, Object t)
    {
        this.InstanceKey = key;

        this.postInstance = t;
    }

    public void equalToValue(String key, Object o)
    {
        WhereClause = key;

        EqualValue = o;
    }

    public void orderBy(String key)
    {
        WhereClause = key;
    }

    public void startAt(String key, Object o)
    {
        WhereClause = key;

        EqualStartValue = o;
    }

    public void endAt(String key, Object o)
    {
        WhereClause = key;

        EqualEndValue = o;
    }

    public void range(String key, Object ... params)
    {
        WhereClause = key;

        EqualStartValue = (params != null && params.length > 0 ? params[0] : null);

        EqualEndValue = (params != null && params.length > 1 ? params[1] : null);
    }

    public void linmitToLast(String s, int limit)
    {
        WhereClause = s;

        limitEnd = limit;
    }

    public void linmitToStart(String s, int limit)
    {
        WhereClause = s;

        limitStart = limit;
    }

    public void update(String key, String w, Object d)
    {
        InstanceKey = key;

        WhereClause = w;

        UpdateValue = d;
    }

    public INetworkProgressView getProgressView()
    {
        return ProgressView;
    }

    public void setProgressView(INetworkProgressView progressView)
    {
        ProgressView = progressView;
    }
}
