package com.dev.voltsoft.lib.firebase.db;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.dev.voltsoft.lib.IResponseListener;
import com.dev.voltsoft.lib.model.BaseRequest;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.Arrays;

public class FireBaseDBRequest<T> extends BaseRequest implements Runnable
{
    private DatabaseReference Reference;

    private RequestType mRequestType;

    private ArrayList<String> ChildNameList = new ArrayList<>();

    private Class<T> targetClass;

    private T postInstance;

    private String WhereClause;

    private String InstanceKey;

    private String EqualValue;

    private String EqualStartStr;

    private double EqualStartValue = -1;

    private String limitStartValue;

    private String limitEndValue;

    private Object UpdateValue;

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

                    FireBaseDBResponse<T> fireBaseDBResponse = new FireBaseDBResponse<>();

                    if (dataSnapshot.exists() && responseListener != null)
                    {
                        String key = dataSnapshot.getKey();

                        T t = dataSnapshot.getValue(targetClass);

                        fireBaseDBResponse.addResult(key, t);
                        fireBaseDBResponse.setResponseSuccess(true);

                        responseListener.onResponseListen(fireBaseDBResponse);

                        setResponseListener(null);

                        Reference.removeEventListener(this);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {
                    IResponseListener responseListener = getResponseListener();

                    if (responseListener != null)
                    {
                        FireBaseDBResponse<T> fireBaseDBResponse = new FireBaseDBResponse<>();
                        fireBaseDBResponse.setResponseSuccess(false);

                        responseListener.onResponseListen(fireBaseDBResponse);

                        setResponseListener(null);

                        Reference.removeEventListener(this);
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
                        FireBaseDBResponse<T> fireBaseDBResponse = new FireBaseDBResponse<>();

                        fireBaseDBResponse.setResponseSuccess(true);

                        responseListener.onResponseListen(fireBaseDBResponse);

                        setResponseListener(null);

                        Reference.removeEventListener(this);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {
                    IResponseListener responseListener = getResponseListener();

                    if (responseListener != null)
                    {
                        FireBaseDBResponse<T> fireBaseDBResponse = new FireBaseDBResponse<>();
                        fireBaseDBResponse.setResponseSuccess(false);

                        responseListener.onResponseListen(fireBaseDBResponse);

                        setResponseListener(null);

                        Reference.removeEventListener(this);
                    }
                }
            });
            ref.child(InstanceKey).child(WhereClause).setValue(UpdateValue);
        }
    }

    private void runQuery()
    {
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

                if (!TextUtils.isEmpty(EqualValue))
                {
                    query = query.equalTo(EqualValue);
                }
                else if (!TextUtils.isEmpty(EqualStartStr))
                {
                    query = query.startAt(EqualStartStr);
                }
                else if (EqualStartValue != -1)
                {
                    query = query.startAt(EqualStartValue);
                }
                else if (!TextUtils.isEmpty(limitStartValue))
                {
                    query = query.limitToFirst(Integer.parseInt(limitStartValue));
                }
                else if (!TextUtils.isEmpty(limitEndValue))
                {
                    query = query.limitToLast(Integer.parseInt(limitEndValue));
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
                        FireBaseDBResponse<T> fireBaseDBResponse = new FireBaseDBResponse<>();

                        if (dataSnapshot.exists())
                        {
                            for (DataSnapshot child : dataSnapshot.getChildren())
                            {
                                try
                                {
                                    String key = child.getKey();

                                    T t = child.getValue(targetClass);

                                    fireBaseDBResponse.addResult(key, t);
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

                        setResponseListener(null);

                        Reference.removeEventListener(this);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {
                    IResponseListener responseListener = getResponseListener();

                    if (responseListener != null)
                    {
                        FireBaseDBResponse<T> fireBaseDBResponse = new FireBaseDBResponse<>();
                        fireBaseDBResponse.setResponseSuccess(false);

                        responseListener.onResponseListen(fireBaseDBResponse);

                        setResponseListener(null);

                        Reference.removeEventListener(this);
                    }
                }
            });
        }
    }

    public FireBaseDBRequest mappingTarget(Class<T> targetClass, String ... s)
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

    public T getPostInstance()
    {
        return postInstance;
    }

    public void setPostInstance(T t)
    {
        this.postInstance = t;
    }

    public void setPostInstance(String key, T t)
    {
        this.InstanceKey = key;

        this.postInstance = t;
    }

    public void equalToValue(String ... s)
    {
        WhereClause = (s != null && s.length > 0 ? s[0] : null);

        EqualValue = (s != null && s.length > 1 ? s[1] : null);
    }

    public void startAt(Object ... o)
    {
        Object param1 = (o != null && o.length > 0 ? o[0] : null);

        Object param2 = (o != null && o.length > 1 ? o[1] : null);

        if (param1 instanceof String)
        {
            WhereClause = (String) param1;
        }

        if (param2 instanceof String)
        {
            EqualStartStr = (String) param2;
        }
        else
        {
            EqualStartValue = (double) param2;
        }
    }

    public void linmitToLast(String ... s)
    {
        WhereClause = (s != null && s.length > 0 ? s[0] : null);

        limitEndValue = (s != null && s.length > 1 ? s[1] : null);
    }

    public void linmitToStart(String ... s)
    {
        WhereClause = (s != null && s.length > 0 ? s[0] : null);

        limitStartValue = (s != null && s.length > 1 ? s[1] : null);
    }

    public void update(String key, String w, Object d)
    {
        InstanceKey = key;

        WhereClause = w;

        UpdateValue = d;
    }
}
